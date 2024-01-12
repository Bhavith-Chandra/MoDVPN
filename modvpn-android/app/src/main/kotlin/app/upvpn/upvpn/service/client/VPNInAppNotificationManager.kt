package app.modvpn.modvpn.service.client

import app.modvpn.modvpn.model.VPNNotification
import app.modvpn.modvpn.service.ipc.Event
import app.modvpn.modvpn.service.ipc.MessageHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update   

class VPNInAppNotificationManager(private val eventHandler: MessageHandler<Event>) {

    private val vpnNotificationState = MutableStateFlow(setOf<VPNNotification>())

    val vpnNotificationFlow = vpnNotificationState.asStateFlow()

    init {
        eventHandler.registerHandler(Event.VpnNotification::class) {
            onVpnNotification(it.notification)
        }
    }

    private fun onVpnNotification(vpnNotification: VPNNotification) {
        vpnNotificationState.update {
            it + vpnNotification
        }
    }

    fun ack(vpnNotification: VPNNotification) {
        vpnNotificationState.update { it ->
            val copy = it.toMutableSet()
            copy.remove(vpnNotification)
            copy.toSet()
        }
    }

}
