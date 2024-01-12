package app.modvpn.modvpn.network

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(val errorType: String, val message: String)
