package mx.utng.smart_health_monitor.mqtt

import kotlinx.serialization.Serializable

/**
 * Mensaje de FC enviado desde el Wear OS
 *
 * Ejemplo: {"bpm": 72, "estado": "Normal", "timestamp": 1700000000}
 */
@Serializable
data class FcMessage(
    val bpm: Int,
    val estado: String,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Mensaje enriquecido para Android TV
 *
 * Ejemplo: {"bpm": 72, "estado": "Normal", "hora": "10:30:00"}
 */
@Serializable
data class TvMessage(
    val bpm: Int,
    val estado: String,
    val hora: String
)

/**
 * Mensaje de alerta
 *
 * Ejemplo: {"tipo": "FC_ALTA", "bpm": 135, "mensaje": "FC fuera de rango"}
 */
@Serializable
data class AlertMessage(
    val tipo: String,
    val bpm: Int,
    val mensaje: String
)