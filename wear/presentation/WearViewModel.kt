package mx.utng.smart_health_monitor.wear.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.utng.smart_health_monitor.wear.mqtt.MqttWearPublisher

class WearViewModel(
    private val context: Context
) : ViewModel() {

    private val TAG = "WearViewModel"

    // ── Estado de la UI ──────────────────────────────────────────────
    private val _state = MutableStateFlow(WearUiState())
    val state: StateFlow<WearUiState> = _state.asStateFlow()

    // ── MQTT Publisher ──────────────────────────────────────────────
    private val mqttPublisher = MqttWearPublisher(context)

    // ── Simulación de FC (para pruebas sin sensor) ──────────────────
    private var _fcActual = MutableStateFlow(72)
    val fcActual: StateFlow<Int> = _fcActual.asStateFlow()

    init {
        // Conectar MQTT al iniciar el ViewModel
        viewModelScope.launch(Dispatchers.IO) {
            mqttPublisher.connect()
            Log.d(TAG, "✅ MQTT Publisher conectado")
        }

        // Simular cambios de FC cada 3 segundos (para pruebas)
        // En producción, esto vendría del sensor Health Services
        viewModelScope.launch {
            var bpm = 72
            while (true) {
                kotlinx.coroutines.delay(3000)
                // Simular variación aleatoria entre 60-100
                bpm = (60..100).random()
                _fcActual.value = bpm

                // Determinar estado
                val estado = when {
                    bpm < 60 -> "FC Baja"
                    bpm > 100 -> "FC Alta"
                    else -> "Normal"
                }

                // Actualizar UI
                _state.update { it.copy(
                    fcActual = bpm,
                    fcEstado = estado,
                    ultimaActualizacion = java.text.SimpleDateFormat(
                        "HH:mm:ss",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date())
                ) }

                // Publicar FC vía MQTT
                mqttPublisher.publishFC(bpm, estado)
                Log.d(TAG, "📤 FC publicada: $bpm bpm ($estado)")
            }
        }
    }

    /**
     * Método para actualizar FC desde Health Services (Wear OS)
     * Este método será llamado cuando el sensor envíe datos reales
     */
    fun updateFC(bpm: Int) {
        val estado = when {
            bpm < 60 -> "FC Baja"
            bpm > 100 -> "FC Alta"
            else -> "Normal"
        }

        _fcActual.value = bpm
        _state.update { it.copy(
            fcActual = bpm,
            fcEstado = estado,
            ultimaActualizacion = java.text.SimpleDateFormat(
                "HH:mm:ss",
                java.util.Locale.getDefault()
            ).format(java.util.Date())
        ) }

        // Publicar FC vía MQTT
        mqttPublisher.publishFC(bpm, estado)
        Log.d(TAG, "📤 FC publicada desde sensor: $bpm bpm ($estado)")
    }

    override fun onCleared() {
        super.onCleared()
        mqttPublisher.disconnect()
        Log.d(TAG, "MQTT Publisher desconectado")
    }
}

/**
 * Estado de la UI para Wear OS
 */
data class WearUiState(
    val fcActual: Int = 72,
    val fcEstado: String = "Normal",
    val ultimaActualizacion: String = "--:--:--",
    val isLoading: Boolean = false
)