package mx.utng.smart_health_monitor

import android.app.Application
import mx.utng.smart_health_monitor.data.SmartHealthRepository

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this)
    }
}