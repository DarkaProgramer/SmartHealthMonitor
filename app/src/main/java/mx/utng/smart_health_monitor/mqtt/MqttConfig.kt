package mx.utng.smart_health_monitor.mqtt

object MqttConfig {
    // ⚠️ Reemplaza con los datos de TU cluster HiveMQ
    const val BROKER_URL = "ssl://74e99432d3f24abfaf8d63c893adcdd9.s1.eu.hivemq.cloud:8883"
    const val USERNAME = "smarthealth"
    const val PASSWORD = "tu-contraseña-aqui"

    // Topics del proyecto
    const val TOPIC_FC = "utng/smarthealthmonitor/fc"
    const val TOPIC_TV = "utng/smarthealthmonitor/tv"
    const val TOPIC_ALERT = "utng/smarthealthmonitor/alerta"

    // QoS: 0=best effort, 1=at least once, 2=exactly once
    const val QOS = 1

    // Client IDs únicos por dispositivo
    const val CLIENT_WEAR = "smarthealthmonitor-wear"
    const val CLIENT_APP = "smarthealthmonitor-app"
    const val CLIENT_TV = "smarthealthmonitor-tv"
}