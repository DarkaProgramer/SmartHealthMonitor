package mx.utng.smart_health_monitor.wear.mqtt

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mx.utng.smart_health_monitor.mqtt.FcMessage
import mx.utng.smart_health_monitor.mqtt.MqttConfig
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import javax.net.ssl.SSLSocketFactory

class MqttWearPublisher(private val context: Context) {

    private var client: MqttAsyncClient? = null
    private val TAG = "MQTT_WEAR"

    /**
     * Conectar al broker MQTT
     */
    suspend fun connect() = withContext(Dispatchers.IO) {
        try {
            client = MqttAsyncClient(
                MqttConfig.BROKER_URL,
                MqttConfig.CLIENT_WEAR,
                MemoryPersistence()
            )

            val options = MqttConnectOptions().apply {
                userName = MqttConfig.USERNAME
                password = MqttConfig.PASSWORD.toCharArray()
                isCleanSession = true
                connectionTimeout = 30
                keepAliveInterval = 60
                socketFactory = SSLSocketFactory.getDefault()
            }

            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "✅ Conectado a HiveMQ Cloud")
                }

                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    Log.e(TAG, "❌ Error de conexión: ${ex?.message}")
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al conectar: ${e.message}")
        }
    }

    /**
     * Publicar FC al topic MQTT
     */
    fun publishFC(bpm: Int, estado: String) {
        if (client?.isConnected != true) {
            Log.w(TAG, "⚠️ No conectado, intentando reconectar...")
            return
        }

        try {
            val message = FcMessage(bpm = bpm, estado = estado)
            val payload = Json.encodeToString(message).toByteArray()

            val mqttMessage = MqttMessage(payload).apply {
                qos = MqttConfig.QOS
                isRetained = true
            }

            client?.publish(MqttConfig.TOPIC_FC, mqttMessage)
            Log.d(TAG, "📤 Publicado: $bpm bpm → ${MqttConfig.TOPIC_FC}")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al publicar: ${e.message}")
        }
    }

    /**
     * Desconectar del broker
     */
    fun disconnect() {
        try {
            client?.disconnect()
            Log.d(TAG, "Desconectado del broker")
        } catch (e: Exception) {
            Log.e(TAG, "Error al desconectar: ${e.message}")
        }
    }

    /**
     * Verificar si está conectado
     */
    fun isConnected(): Boolean = client?.isConnected == true
}