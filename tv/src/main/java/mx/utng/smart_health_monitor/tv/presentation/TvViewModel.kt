package mx.utng.smart_health_monitor.tv.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smart_health_monitor.data.db.LecturaFC
import mx.utng.smart_health_monitor.mqtt.TvMessage
import mx.utng.smart_health_monitor.tv.mqtt.MqttTvSubscriber
import mx.utng.smart_health_monitor.data.SmartHealthRepository

class TvViewModel(
    private val context: Context
) : ViewModel() {

    private val TAG = "TvViewModel"

    // ── Estado de la UI ──────────────────────────────────────────────
    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    // ── Flow de mensajes MQTT ────────────────────────────────────────
    private val mqttFlow = MutableStateFlow<TvMessage?>(null)
    private val mqttSubscriber = MqttTvSubscriber(context, mqttFlow)

    init {
        // Conectar MQTT
        mqttSubscriber.connect()
        Log.d(TAG, "✅ MQTT Subscriber conectado")

        // Observar mensajes MQTT y actualizar la UI
        viewModelScope.launch {
            mqttFlow.collect { tvMsg ->
                tvMsg?.let {
                    Log.d(TAG, "📺 Actualizando TV con: ${it.bpm} bpm")
                    _state.update { currentState ->
                        currentState.copy(
                            fcActual = it.bpm,
                            fcEstado = it.estado,
                            ultimaHora = it.hora,
                            isLoading = false
                        )
                    }
                }
            }
        }

        // Obtener datos locales de Room (como respaldo y para historial)
        viewModelScope.launch {
            SmartHealthRepository.obtenerHistorial().collect { lecturas ->
                _state.update { it.copy(
                    lecturas = lecturas.take(10) // Solo las últimas 10
                ) }

                if (lecturas.isNotEmpty() && _state.value.fcActual == 0) {
                    val ultima = lecturas.first()
                    _state.update { it.copy(
                        fcActual = ultima.valorBpm,
                        fcEstado = if (ultima.esNormal) "Normal" else "Alerta",
                        ultimaHora = ultima.hora
                    ) }
                }
            }
        }
    }

    fun refresh() {
        _state.update { it.copy(isLoading = true) }
        _state.update { it.copy(isLoading = false) }
    }

    override fun onCleared() {
        super.onCleared()
        mqttSubscriber.disconnect()
        Log.d(TAG, "MQTT Subscriber desconectado")
    }
}

/**
 * Estado de la UI para Android TV
 */
data class TvUiState(
    val fcActual: Int = 0,
    val fcEstado: String = "Esperando...",
    val ultimaHora: String = "--:--:--",
    val isLoading: Boolean = false,
    val error: String? = null,
    val lecturas: List<LecturaFC> = emptyList() // ✅ Agregado para DetailScreen
)