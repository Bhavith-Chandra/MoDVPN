package app.modvpn.modvpn.service.client

import android.os.Messenger
import android.util.Log
import app.modvpn.modvpn.BuildConfig
import app.modvpn.modvpn.model.Location
import app.modvpn.modvpn.service.VPNState
import app.modvpn.modvpn.service.ipc.Event
import app.modvpn.modvpn.service.ipc.MessageHandler
import app.modvpn.modvpn.service.ipc.Request
import app.modvpn.modvpn.service.ipc.extensions.sendRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class VPNManager(
    private val vpnServiceMessenger: Messenger,
    private val eventHandler: MessageHandler<Event>
) {
    private val tag = "VPNManager"
    private val vpnState = MutableStateFlow<VPNState?>(null)

    val vpnStateFlow = vpnState.asStateFlow()

    init {
        eventHandler.registerHandler(Event.VpnState::class) {
            onVpnStateUpdate(it.vpnState)
        }
    }

    private fun onVpnStateUpdate(newVPNState: VPNState) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, "Received $newVPNState")
        }
        vpnState.value = newVPNState
    }

    fun connect(location: Location) {
        vpnServiceMessenger.sendRequest(Request.Connect(location))
    }

    fun disconnect() {
        vpnServiceMessenger.sendRequest(Request.Disconnect)
    }
}
