package app.modvpn.modvpn.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VPNNotification(val msg: String) : Parcelable
