



package app.modvpn.modvpn.data.db

import app.modvpn.modvpn.model.DeviceInfo
import app.modvpn.modvpn.model.DeviceType
import com.wireguard.crypto.Key
import com.wireguard.crypto.KeyPair

fun Device.toDeviceInfo(): DeviceInfo {
    val privateKey = Key.fromBase64(this.privateKey)
    val keyPair = KeyPair(privateKey)

    return DeviceInfo(
        name = this.name,
        version = this.version,
        arch = this.arch,
        publicKey = keyPair.publicKey.toBase64(),
        uniqueId = this.uniqueId,
        deviceType = DeviceType.Android
    )
}
