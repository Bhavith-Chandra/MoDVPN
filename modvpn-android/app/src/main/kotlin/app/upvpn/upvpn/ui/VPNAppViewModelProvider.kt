package app.modvpn.modvpn.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.modvpn.modvpn.VPNApplication
import app.modvpn.modvpn.ui.viewmodels.HomeViewModel
import app.modvpn.modvpn.ui.viewmodels.LocationViewModel
import app.modvpn.modvpn.ui.viewmodels.SignInViewModel

object VPNAppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VPNApplication)
            SignInViewModel(
                application.container.serviceConnectionManager,
                application.container.vpnRepository
            )
        }

        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VPNApplication)
            LocationViewModel(application.container.vpnRepository)
        }

        initializer {
            val application =
                (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VPNApplication)
            HomeViewModel(
                application.container.serviceConnectionManager,
                application.container.vpnRepository
            )
        }
    }
}
