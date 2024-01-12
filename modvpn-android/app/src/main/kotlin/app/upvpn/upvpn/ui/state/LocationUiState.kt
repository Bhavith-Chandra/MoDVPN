package app.modvpn.modvpn.ui.state

import app.modvpn.modvpn.model.Location

data class LocationUiState(
    val locationState: LocationState = LocationState.Loading,
    val search: String = "",
    val selectedLocation: Location? = null
)

sealed class LocationState {
    data object Loading : LocationState()

    data class Locations(val locations: List<Location>) : LocationState()

    data class Error(val message: String) : LocationState()
}
