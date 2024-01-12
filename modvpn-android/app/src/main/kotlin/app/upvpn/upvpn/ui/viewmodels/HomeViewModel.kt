package app.modvpn.modvpn.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.modvpn.modvpn.data.VPNRepository
import app.modvpn.modvpn.model.Location
import app.modvpn.modvpn.model.VPNNotification
import app.modvpn.modvpn.service.VPNState
import app.modvpn.modvpn.service.client.VPNServiceConnectionManager
import app.modvpn.modvpn.ui.state.HomeUiState
import app.modvpn.modvpn.ui.state.VpnUiState
import app.modvpn.modvpn.ui.state.toVPNUiState
import app.modvpn.modvpn.ui.state.transitionToDisconnecting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val serviceConnectionManager: VPNServiceConnectionManager,
    private val vpnRepository: VPNRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val tag = "HomeViewModel"

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _vpnNotificationState = MutableStateFlow(setOf<VPNNotification>())
    val vpnNotificationState = _vpnNotificationState.asStateFlow()

    init {
        getUpdates()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getUpdates() {
        viewModelScope.launch(dispatcher) {
            serviceConnectionManager.vpnServiceClientFlow
                .filterNotNull()
                .flatMapLatest { vpnServiceClient ->
                    vpnServiceClient.vpnManager.vpnStateFlow
                }.collect {
                    when (it) {
                        is VPNState -> _uiState.update { value -> value.copy(vpnUiState = it.toVPNUiState()) }
                        else -> Unit
                    }
                }
        }

        viewModelScope.launch(dispatcher) {
            serviceConnectionManager.vpnServiceClientFlow
                .filterNotNull()
                .flatMapLatest { vpnServiceClient ->
                    vpnServiceClient.vpnInAppNotificationManager.vpnNotificationFlow
                }
                .collect { newVpnNotifications ->
                    _vpnNotificationState.update { newVpnNotifications }
                }
        }
    }

    fun connectPreVpnPermission(selectedLocation: Location?) {
        selectedLocation?.let {
            _uiState.update { value -> value.copy(vpnUiState = VpnUiState.Requesting(it)) }
        }
    }

    fun connectPostVpnPermission(granted: Boolean, selectedLocation: Location?) {
        if (granted) {
            selectedLocation?.let {
                serviceConnectionManager.vpnManager()?.connect(it)
            }
        } else {
            _uiState.update { value -> value.copy(vpnUiState = VpnUiState.Disconnected) }
        }
    }

    fun disconnect() {
        val newVpnUiState = _uiState.value.vpnUiState.transitionToDisconnecting()
        newVpnUiState?.let { vpnUiState ->
            _uiState.update { value -> value.copy(vpnUiState = vpnUiState) }
        }

        serviceConnectionManager.vpnManager()?.disconnect()
    }

    fun ackVpnNotification(vpnNotification: VPNNotification) {
        serviceConnectionManager.vpnInAppNotificationManager()?.ack(vpnNotification)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(tag, "HomeViewModel::onCleared")
    }
}
