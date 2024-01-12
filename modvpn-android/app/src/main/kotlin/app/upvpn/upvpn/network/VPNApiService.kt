package app.modvpn.modvpn.network

import app.modvpn.modvpn.model.Accepted
import app.modvpn.modvpn.model.AddDeviceRequest
import app.modvpn.modvpn.model.AddDeviceResponse
import app.modvpn.modvpn.model.EndSessionApi
import app.modvpn.modvpn.model.Ended
import app.modvpn.modvpn.model.Location
import app.modvpn.modvpn.model.NewSession
import app.modvpn.modvpn.model.VpnSessionStatus
import app.modvpn.modvpn.model.VpnSessionStatusRequest
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface VPNApiService {
    @POST("devices")
    suspend fun addDevice(@Body request: AddDeviceRequest): ApiResponse<AddDeviceResponse>

    @POST("sign-out")
    suspend fun signOut(): ApiResponse<Unit>

    @GET("locations")
    suspend fun getLocations(): ApiResponse<List<Location>>

    @POST("new-vpn-session")
    suspend fun newVpnSession(@Body request: NewSession): ApiResponse<Accepted>

    @POST("vpn-session-status")
    suspend fun getVpnSessionStatus(@Body request: VpnSessionStatusRequest): ApiResponse<VpnSessionStatus>

    @POST("end-vpn-session")
    suspend fun endVpnSession(@Body request: EndSessionApi): ApiResponse<Ended>
}
