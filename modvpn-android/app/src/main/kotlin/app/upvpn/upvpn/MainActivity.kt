package app.modvpn.modvpn

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.modvpn.modvpn.service.client.VPNServiceConnectionManager
import app.modvpn.modvpn.ui.VPNApp
import app.modvpn.modvpn.ui.theme.modvpnTheme
import com.google.accompanist.adaptive.calculateDisplayFeatures
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val tag = "MainActivity"
    private lateinit var serviceConnectionManager: VPNServiceConnectionManager

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialize connection manager
        val app = application as VPNApplication
        serviceConnectionManager = app.container.serviceConnectionManager

        // only login screen needs to be resized
        val resizeWindow: (shouldResize: Boolean) -> Unit = { shouldResize ->
            if (shouldResize) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            } else {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            }
        }



        setContent {
            modvpnTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    // A surface container using the 'background' color from the theme
                    val snackbarHostState = remember { SnackbarHostState() }
                    val scope = rememberCoroutineScope()
                    val displayFeatures = calculateDisplayFeatures(activity = this)

                    val showSnackBar: (msg: String) -> Unit = { msg ->
                        scope.launch {
                            snackbarHostState.showSnackbar(msg)
                        }
                    }

                    val windowSize = calculateWindowSizeClass(this)

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = {
                            SnackbarHost(
                                hostState = snackbarHostState,
                                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 80.dp)
                            )
                        }) {
                        VPNApp(
                            windowSize = windowSize,
                            displayFeatures = displayFeatures,
                            resizeWindow = resizeWindow,
                            showSnackBar = showSnackBar,
                            modifier = Modifier.padding(it)
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        serviceConnectionManager.bind()
    }

    override fun onStop() {
        super.onStop()
        serviceConnectionManager.unbind()
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy")
        super.onDestroy()
    }
}
