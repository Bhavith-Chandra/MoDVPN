package app.modvpn.modvpn.service.ipc

import android.os.Parcelable
import app.modvpn.modvpn.model.Accepted
import app.modvpn.modvpn.model.Location
import app.modvpn.modvpn.model.VpnSessionStatus
import com.github.michaelbull.result.Result
import com.wireguard.config.Interface
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
sealed class OrchestratorMessage() : Parcelable {
    data class ConnectResponse(
        val location: Location,
        val result: @RawValue Result<Pair<Accepted, Interface>, String>
    ) : OrchestratorMessage()

    data class VpnSessionUpdate(val location: Location, val status: @RawValue VpnSessionStatus) :
        OrchestratorMessage()
}
