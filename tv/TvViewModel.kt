package mx.utng.smart_health_monitor.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.utng.smart_health_monitor.data.SmartHealthRepository
import mx.utng.smart_health_monitor.data.db.LecturaFC

class TvViewModel : ViewModel() {

    // FC actual del wearable (o 0 si no hay dato)
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    // Historial de lecturas desde Room DAO
    val historial: StateFlow<List<LecturaFC>> =
        SmartHealthRepository.obtenerHistorial()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}