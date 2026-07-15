package mx.utng.smart_health_monitor

import android.app.Application
import android.util.Log
import mx.utng.smart_health_monitor.data.SmartHealthRepository
import mx.utng.smart_health_monitor.data.sync.NeonSyncWorker

class SmartHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // 1. Inicializar el repositorio con Room (S7)
        SmartHealthRepository.init(this)

        // 2. Iniciar sync periódico con Neon PostgreSQL (Ejercicio Neon)
        try {
            NeonSyncWorker.schedule(this)
            Log.d("SmartHealthApp", "✅ NeonSyncWorker programado correctamente")
        } catch (e: Exception) {
            Log.e("SmartHealthApp", "❌ Error al programar NeonSyncWorker: ${e.message}")
        }
    }
}