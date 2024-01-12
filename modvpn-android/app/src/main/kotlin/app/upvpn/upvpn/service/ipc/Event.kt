package app.modvpn.modvpn.service.ipc

import android.os.Parcelable
import app.modvpn.modvpn.model.VPNNotification
import app.modvpn.modvpn.service.VPNState
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class Event : Parcelable {
    data class ListenerRegistered(val id: Int) : Event()
    data class VpnState(val vpnState: VPNState) : Event()
    data class VpnNotification(val notification: VPNNotification) : Event()
}
