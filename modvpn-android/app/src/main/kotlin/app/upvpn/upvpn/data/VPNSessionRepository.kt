package app.modvpn.modvpn.data

import android.util.Log
import app.modvpn.modvpn.data.db.VPNDatabase
import app.modvpn.modvpn.data.db.VpnSession
import app.modvpn.modvpn.data.db.toDbLocation
import app.modvpn.modvpn.model.Accepted
import app.modvpn.modvpn.model.EndSessionApi
import app.modvpn.modvpn.model.Location
import app.modvpn.modvpn.model.NewSession
import app.modvpn.modvpn.model.VpnSessionStatus
import app.modvpn.modvpn.model.VpnSessionStatusRequest
import app.modvpn.modvpn.network.VPNApiService
import app.modvpn.modvpn.network.toResult
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.fold
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.wireguard.config.InetAddresses
import com.wireguard.config.InetNetwork
import com.wireguard.config.Interface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

interface VPNSessionRepository {
    // manage VPN Session
    fun newVpnSession(
        newSessionScope: CoroutineScope,
        requestId: UUID,
        location: Location,
        onConnectResponseCallback: (Result<Pair<Accepted, Interface>, String>) -> Unit,
        onVpnSessionUpdateCallback: (VpnSessionStatus) -> Unit
    )

    suspend fun getVpnSessionStatus(request: VpnSessionStatusRequest): Result<VpnSessionStatus, String>

    fun endVpnSession(scope: CoroutineScope, requestId: UUID, reason: String)

    fun runReclaimer(scope: CoroutineScope)

}


class DefaultVPNSessionRepository(
    private val vpnApiService: VPNApiService,
    private val vpnDatabase: VPNDatabase,
) : VPNSessionRepository {

    private val tag = "DefaultVPNSessionRepository"

    private var sessionReclaimer: VpnSessionReclaimer? = null

    override fun runReclaimer(scope: CoroutineScope) {
        synchronized(this) {
            val reclaimer = sessionReclaimer
            if (reclaimer == null || reclaimer.running().not()) {
                val newReclaimer = VpnSessionReclaimer(vpnDatabase, this)
                newReclaimer.reclaim(scope)
                sessionReclaimer = newReclaimer
            }
        }
    }

    override fun newVpnSession(
        newSessionScope: CoroutineScope,
        requestId: UUID,
        location: Location,
        onConnectResponseCallback: (Result<Pair<Accepted, Interface>, String>) -> Unit,
        onVpnSessionUpdateCallback: (VpnSessionStatus) -> Unit
    ) {
        newSessionScope.launch(Dispatchers.IO) {
            val vpnSessionDb = VpnSession(requestId, location.toDbLocation())
            vpnDatabase.vpnSessionDao().insert(vpnSessionDb)
            // TODO init if device is null? or return error?
            val deviceDb = vpnDatabase.deviceDao().getDevice()

            val newSession =
                NewSession(vpnSessionDb.requestId, deviceDb!!.uniqueId, vpnSessionDb.code)

            val acceptedResult =
                vpnApiService.newVpnSession(newSession).toResult().mapError { e -> e.message }

            val response = acceptedResult.map {
                val updatedVpnSessionDb = vpnSessionDb.copy(sessionUUID = it.vpnSessionUuid)
                vpnDatabase.vpnSessionDao().update(updatedVpnSessionDb)

                // TODO: wild !!, is it safe assumption?
                val interFace = Interface.Builder()
                    .addAddress(InetNetwork.parse(deviceDb.ipv4Address!!))
                    .addDnsServer(InetAddresses.parse("1.1.1.1"))
                    .parsePrivateKey(deviceDb.privateKey)
                    .setMtu(1280)
                    .build()

                it to interFace
            }

            // first call callback so that orchestrator can update the state before first
            // vpn session update arrives from network
            onConnectResponseCallback(response)

            // update last access for recent locations
            newSessionScope.launch(Dispatchers.IO) {
                vpnDatabase.locationDao()
                    .updateLastAccess(location.code, System.currentTimeMillis() / 1000)
            }

            response.onSuccess { (accepted, _) ->
                // start vpn session watch
                newSessionScope.launch(Dispatchers.IO) {
                    val watcher = VPNSessionWatcher(
                        VpnSessionStatusRequest(
                            accepted.requestId,
                            deviceDb.uniqueId,
                            accepted.vpnSessionUuid
                        ), vpnDatabase, this@DefaultVPNSessionRepository
                    )
                    watcher.watch(onVpnSessionUpdateCallback)
                }
            }

            response.onFailure {
                // remove vpn session
                vpnDatabase.vpnSessionDao().delete(vpnSessionDb)
            }

        }
    }

    override suspend fun getVpnSessionStatus(request: VpnSessionStatusRequest): Result<VpnSessionStatus, String> {
        return vpnApiService.getVpnSessionStatus(request).toResult().mapError { e -> e.message }
    }

    override fun endVpnSession(scope: CoroutineScope, requestId: UUID, reason: String) {
        scope.launch(Dispatchers.IO) {
            Log.i(tag, "endVpnSession: reason: $reason, requestId: $requestId")
            val device = vpnDatabase.deviceDao().getDevice()

            val vpnSession = vpnDatabase.vpnSessionDao().withRequestId(requestId)

            val request = device?.let {
                EndSessionApi(
                    requestId = requestId,
                    reason = reason,
                    deviceUniqueId = it.uniqueId,
                    vpnSessionUuid = vpnSession?.sessionUUID
                )
            }

            if (request != null) {
                Log.i(tag, "Ending session for $requestId: $request \n vpnSession: $vpnSession")
                val apiResult = vpnApiService.endVpnSession(request).toResult()

                apiResult.fold(
                    success = {
                        vpnSession?.let {
                            vpnDatabase.vpnSessionDao().delete(it)
                        }
                    },
                    failure = { it ->
                        if (it.errorType == "not_found") {
                            vpnSession?.let { vpnSession ->
                                vpnDatabase.vpnSessionDao().delete(vpnSession)
                            }
                        } else {
                            vpnSession?.let {
                                vpnDatabase.vpnSessionDao().markAllForDeletion()
                            }

                            runReclaimer(scope)
                        }
                    }
                )

                Log.i(tag, "End session response: $apiResult")
            }
        }
    }

    companion object {
        var globalIndex = 0
    }


}
