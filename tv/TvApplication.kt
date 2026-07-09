package mx.utng.smart_health_monitor.tv

import android.app.Application
import mx.utng.smart_health_monitor.data.SmartHealthRepository

class TvApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializar Room para el módulo TV
        SmartHealthRepository.init(this)
    }
}