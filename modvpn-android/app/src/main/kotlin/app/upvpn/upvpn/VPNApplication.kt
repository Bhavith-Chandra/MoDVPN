package app.modvpn.modvpn

import android.app.Application
import android.util.Log
import app.modvpn.modvpn.data.AppContainer
import app.modvpn.modvpn.data.DefaultAppContainer

class VPNApplication : Application() {
    private val tag = "VPNApplication"
    
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        container.init()
    }

    override fun onTerminate() {
        Log.i(tag, "onTerminate")
        super.onTerminate()
    }
}
