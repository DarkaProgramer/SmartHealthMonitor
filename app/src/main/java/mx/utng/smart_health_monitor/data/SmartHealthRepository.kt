package mx.utng.smart_health_monitor.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import mx.utng.smart_health_monitor.data.models.Lectura
import java.text.SimpleDateFormat
import java.util.*

object SmartHealthRepository {
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    private val _historialFC = MutableStateFlow<List<Lectura>>(emptyList())
    val historialFC: StateFlow<List<Lectura>> = _historialFC.asStateFlow()

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        val horaActual = SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Date())
        _historialFC.value = _historialFC.value + Lectura(bpm, horaActual)
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }
}