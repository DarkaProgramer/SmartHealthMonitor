package mx.utng.smart_health_monitor.mqtt

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.net.ssl.SSLSocketFactory

class MqttAppService(
    private val context: Context,
    private val fcFlow: MutableStateFlow<Int>
) {

    private var client: MqttAsyncClient? = null
    private val TAG = "MQTT_APP"

    /**
     * Conectar al broker MQTT y suscribirse al topic de FC
     */
    fun connect() {
        try {
            client = MqttAsyncClient(
                MqttConfig.BROKER_URL,
                MqttConfig.CLIENT_APP,
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

            // Callback para mensajes entrantes
            client?.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, msg: MqttMessage) {
                    when (topic) {
                        MqttConfig.TOPIC_FC -> handleFcMessage(msg)
                        else -> Log.d(TAG, "Mensaje en topic no manejado: $topic")
                    }
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.w(TAG, "⚠️ Conexión perdida: ${cause?.message}")
                    // Intentar reconectar después de 5 segundos
                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                        connect()
                    }, 5000)
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    // No se usa en este contexto
                }
            })

            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken?) {
                    // Suscribirse al topic de FC
                    client?.subscribe(MqttConfig.TOPIC_FC, MqttConfig.QOS)
                    Log.d(TAG, "✅ Conectado y suscrito a ${MqttConfig.TOPIC_FC}")
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
     * Manejar mensaje de FC del wearable
     */
    private fun handleFcMessage(msg: MqttMessage) {
        try {
            val payload = String(msg.payload)
            Log.d(TAG, "📩 Mensaje recibido: $payload")

            val fcMsg = Json.decodeFromString<FcMessage>(payload)

            // 1. Actualizar el StateFlow del Repository
            fcFlow.value = fcMsg.bpm
            Log.d(TAG, "✅ FC actualizada en Repository: ${fcMsg.bpm} bpm")

            // 2. Re-publicar al topic TV con formato enriquecido
            val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val tvMsg = TvMessage(
                bpm = fcMsg.bpm,
                estado = fcMsg.estado,
                hora = hora
            )

            val tvPayload = Json.encodeToString(tvMsg).toByteArray()
            val tvMqtt = MqttMessage(tvPayload).apply {
                qos = MqttConfig.QOS
                isRetained = true
            }

            client?.publish(MqttConfig.TOPIC_TV, tvMqtt)
            Log.d(TAG, "🔁 Re-publicado al TV: ${fcMsg.bpm} bpm → ${MqttConfig.TOPIC_TV}")

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al procesar mensaje: ${e.message}")
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