package app.modvpn.modvpn.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.modvpn.modvpn.data.VPNRepository
import app.modvpn.modvpn.model.Location
import app.modvpn.modvpn.ui.state.LocationState
import app.modvpn.modvpn.ui.state.LocationUiState
import com.github.michaelbull.result.fold
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LocationViewModel(
    private val vpnRepository: VPNRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val tag = "LocationViewModel"

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val recentLocations = vpnRepository.getRecentLocations(5)
        .mapLatest { list ->
            // map to get estimates from location
            when (_uiState.value.locationState) {
                is LocationState.Locations -> {
                    val locations =
                        (_uiState.value.locationState as LocationState.Locations).locations;

                    list.map { location ->
                        val estimate = locations.find { it.code == location.code }
                        location.copy(estimate = estimate?.estimate)
                    }
                }

                else -> list
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf<Location>())

    private suspend fun getLocations() {
        _uiState.update { value -> value.copy(locationState = LocationState.Loading) }
        val locations = vpnRepository.getLocations()
        locations.fold(
            success = { locations ->
                _uiState.update { value ->

                    if (value.selectedLocation == null && locations.isEmpty().not()) {
                        value.copy(
                            locationState = LocationState.Locations(
                                locations
                            ),
                            selectedLocation = locations.find {
                                it.city.lowercase().contains("ashburn")
                            } ?: locations.first()
                        )
                    } else {
                        value.copy(
                            locationState = LocationState.Locations(
                                locations
                            )
                        )
                    }
                }
            },
            failure = { error ->
                _uiState.update { value ->
                    value.copy(locationState = LocationState.Error(error))
                }
            }
        )
    }

    init {
        viewModelScope.launch(dispatcher) {
            getLocations()
        }
    }

    fun onSearchValueChange(text: String) {
        _uiState.update { value -> value.copy(search = text) }
    }

    fun clearSearchQuery() {
        _uiState.update { value -> value.copy(search = "") }
    }

    fun onRefresh() {
        viewModelScope.launch(dispatcher) {
            getLocations()
        }
    }

    fun onLocationSelected(location: Location) {
        _uiState.update { value -> value.copy(selectedLocation = location) }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(tag, "LocationViewModel::onCleared")
    }
}
