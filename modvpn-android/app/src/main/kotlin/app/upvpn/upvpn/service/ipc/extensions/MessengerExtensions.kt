package app.modvpn.modvpn.service.ipc.extensions

import android.os.DeadObjectException
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import app.modvpn.modvpn.service.ipc.Event
import app.modvpn.modvpn.service.ipc.Request

fun Messenger.sendRequest(request: Request) = this.trySend(request.toMessage())
fun Messenger.sendEvent(event: Event) = this.trySend(event.toMessage())

private fun Messenger.trySend(msg: Message): Boolean {
    return try {
        this.send(msg)
        true
    } catch (e: RemoteException) {
        Log.e("modvpn", "Failed to send message $e")
        false
    } catch (e: DeadObjectException) {
        Log.e("modvpn", "Failed to send message $e")
        false
    }
}

