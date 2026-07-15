package mx.utng.smart_health_monitor

import android.app.Application
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import mx.utng.smart_health_monitor.data.SmartHealthRepository
import mx.utng.smart_health_monitor.data.sync.NeonSyncWorker
import mx.utng.smart_health_monitor.mqtt.MqttAppService

class SmartHealthApp : Application() {

    lateinit var mqttService: MqttAppService

    override fun onCreate() {
        super.onCreate()

        // 1. Inicializar Room
        SmartHealthRepository.init(this)
        Log.d("SmartHealthApp", "✅ Room inicializado")

        // 2. Iniciar sync periódico con Neon
        try {
            NeonSyncWorker.schedule(this)
            Log.d("SmartHealthApp", "✅ NeonSyncWorker programado")
        } catch (e: Exception) {
            Log.e("SmartHealthApp", "❌ Error en NeonSyncWorker: ${e.message}")
        }

        // 3. Inicializar MQTT App Service
        mqttService = MqttAppService(
            context = this,
            fcFlow = SmartHealthRepository.fcFlow as MutableStateFlow<Int>
        )
        mqttService.connect()
        Log.d("SmartHealthApp", "✅ MQTT App Service inicializado")
    }

    override fun onTerminate() {
        super.onTerminate()
        mqttService.disconnect()
        Log.d("SmartHealthApp", "MQTT App Service desconectado")
    }
}