package mx.utng.smart_health_monitor.tv.mqtt

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import mx.utng.smart_health_monitor.mqtt.MqttConfig
import mx.utng.smart_health_monitor.mqtt.TvMessage
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import javax.net.ssl.SSLSocketFactory

class MqttTvSubscriber(
    private val context: Context,
    private val tvFlow: MutableStateFlow<TvMessage?>
) {

    private var client: MqttAsyncClient? = null
    private val TAG = "MQTT_TV"

    /**
     * Conectar al broker MQTT y suscribirse al topic de TV
     */
    fun connect() {
        try {
            client = MqttAsyncClient(
                MqttConfig.BROKER_URL,
                MqttConfig.CLIENT_TV,
                MemoryPersistence()
            )

            client?.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, msg: MqttMessage) {
                    if (topic == MqttConfig.TOPIC_TV) {
                        try {
                            val payload = String(msg.payload)
                            Log.d(TAG, "📩 Mensaje recibido: $payload")

                            val tvMsg = Json.decodeFromString<TvMessage>(payload)
                            tvFlow.value = tvMsg
                            Log.d(TAG, "📺 TV actualizada: ${tvMsg.bpm} bpm (${tvMsg.estado}) a las ${tvMsg.hora}")

                        } catch (e: Exception) {
                            Log.e(TAG, "❌ Error al parsear mensaje: ${e.message}")
                        }
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

            val options = MqttConnectOptions().apply {
                userName = MqttConfig.USERNAME
                password = MqttConfig.PASSWORD.toCharArray()
                isCleanSession = true
                connectionTimeout = 30
                keepAliveInterval = 60
                socketFactory = SSLSocketFactory.getDefault()
            }

            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken?) {
                    client?.subscribe(MqttConfig.TOPIC_TV, MqttConfig.QOS)
                    Log.d(TAG, "✅ TV suscrita a ${MqttConfig.TOPIC_TV}")
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
     * Desconectar del broker
     */
    fun disconnect() {
        try {
            client?.disconnect()
            Log.d(TAG, "TV desconectada del broker")
        } catch (e: Exception) {
            Log.e(TAG, "Error al desconectar: ${e.message}")
        }
    }

    /**
     * Verificar si está conectado
     */
    fun isConnected(): Boolean = client?.isConnected == true
}