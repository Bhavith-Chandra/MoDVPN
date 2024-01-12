package app.modvpn.modvpn.service.ipc

import android.os.Messenger
import android.os.Parcelable
import app.modvpn.modvpn.model.Location
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class Request : Parcelable {
    data class Connect(val location: Location) : Request()
    data object Disconnect : Request()
    data class RegisterListener(val messenger: Messenger) : Request()
    data class DeregisterListener(val id: Int) : Request()
}
