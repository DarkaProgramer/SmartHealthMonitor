package mx.utng.smart_health_monitor.data

import android.content.Context
import kotlinx.coroutines.flow.*
import mx.utng.smart_health_monitor.data.db.LecturaFC
import mx.utng.smart_health_monitor.data.db.SmartHealthDB
import mx.utng.smart_health_monitor.data.models.Lectura
import java.text.SimpleDateFormat
import java.util.*

object SmartHealthRepository {
    // Flows para datos en tiempo real
    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    // DAO de Room
    private var dao: LecturaFCDao? = null

    // Inicializar con contexto (llamar desde Application)
    fun init(context: Context) {
        dao = SmartHealthDB.getDatabase(context).lecturaDao()
    }

    // Actualizar FC (con persistencia en Room)
    suspend fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
        // Persistir en Room automáticamente
        dao?.insertar(LecturaFC(valorBpm = bpm))
    }

    // Actualizar pasos
    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    // Obtener historial desde Room (Flow reactivo)
    fun obtenerHistorial(): Flow<List<LecturaFC>> =
        dao?.obtenerUltimas() ?: emptyFlow()
}