package mx.utng.smart_health_monitor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import mx.utng.smart_health_monitor.data.SmartHealthRepository
import mx.utng.smart_health_monitor.data.db.LecturaFC

class DashboardViewModel : ViewModel() {

    // Frecuencia cardíaca en tiempo real
    val fc: StateFlow<Int> = SmartHealthRepository.fcFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 72
        )

    // Pasos en tiempo real
    val pasos: StateFlow<Int> = SmartHealthRepository.pasosFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 4250
        )

    // ✅ NUEVO: Historial desde Room (Flow reactivo)
    val historial: StateFlow<List<LecturaFC>> =
        SmartHealthRepository.obtenerHistorial()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}