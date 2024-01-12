package app.modvpn.modvpn.service.client

import android.os.Looper
import android.os.Messenger
import android.util.Log
import app.modvpn.modvpn.service.ipc.Event
import app.modvpn.modvpn.service.ipc.MessageHandler
import app.modvpn.modvpn.service.ipc.Request
import app.modvpn.modvpn.service.ipc.extensions.sendRequest
import app.modvpn.modvpn.service.ipc.extensions.toEvent

class VPNServiceClient(private val serviceMessenger: Messenger, private val looper: Looper) {
    private val tag = "VPNServiceClient"
    private val messageHandler = MessageHandler(looper) { msg -> msg.toEvent() }

    val vpnManager = VPNManager(serviceMessenger, messageHandler)
    val vpnInAppNotificationManager = VPNInAppNotificationManager(messageHandler)

    private val clientMessenger = Messenger(messageHandler)
    private var listenerId: Int? = null

    init {
        messageHandler.registerHandler(Event.ListenerRegistered::class) { event ->
            listenerId = event.id
            Log.i(tag, "Listener id: ${event.id}")
        }
        registerClientAsListener()
    }

    private fun registerClientAsListener() {
        serviceMessenger.sendRequest(Request.RegisterListener(clientMessenger))
    }

    private fun deregisterClientAsListener() {
        listenerId?.let {
            serviceMessenger.sendRequest(Request.DeregisterListener(it))
        }
    }

    fun cleanup() {
        deregisterClientAsListener()
    }

}
