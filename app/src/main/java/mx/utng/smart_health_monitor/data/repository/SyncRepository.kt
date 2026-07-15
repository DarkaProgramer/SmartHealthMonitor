package mx.utng.smart_health_monitor.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import mx.utng.smart_health_monitor.data.db.LecturaFC
import mx.utng.smart_health_monitor.data.db.LecturaFCDao
import mx.utng.smart_health_monitor.data.remote.NeonClient
import mx.utng.smart_health_monitor.data.remote.NeonRequest

class SyncRepository(
    private val dao: LecturaFCDao
) {
    private val TAG = "SyncRepository"

    // ── LECTURA LOCAL (offline-first) ─────────────────────
    fun observarHistorial(): Flow<List<LecturaFC>> = dao.obtenerUltimas()

    // ── ESCRITURA LOCAL + SYNC ──────────────────────────────
    suspend fun insertarLectura(lectura: LecturaFC) {
        // 1. Guardar localmente PRIMERO (nunca falla)
        val id = dao.insertar(lectura)

        // 2. Intentar sync con Neon (puede fallar sin internet)
        try {
            sincronizarHaciaNeon(lectura)
            dao.marcarSincronizado(id.toLong())
            Log.d(TAG, "✅ Lectura sincronizada con Neon: id=$id")
        } catch (e: Exception) {
            Log.w(TAG, "⏳ Pendiente de sync: ${e.message}")
        }
    }

    // ── PUSH: Room → Neon ──────────────────────────────────
    private suspend fun sincronizarHaciaNeon(lectura: LecturaFC) =
        withContext(Dispatchers.IO) {
            NeonClient.api.executeQuery(
                auth = NeonClient.AUTH_HEADER,
                connStr = NeonClient.CONN_STRING,
                request = NeonRequest(
                    query = """
                        INSERT INTO lecturas_fc (bpm, estado, dispositivo, hora)
                        VALUES ($1, $2, $3, $4) RETURNING id
                    """.trimIndent(),
                    params = listOf(
                        lectura.valorBpm,
                        if (lectura.esNormal) "Normal" else "Alerta",
                        "app",
                        lectura.hora
                    )
                )
            )
        }

    // ── PULL: Neon → Room ──────────────────────────────────
    suspend fun sincronizarDesdeNeon(limite: Int = 50) =
        withContext(Dispatchers.IO) {
            try {
                val response = NeonClient.api.executeQuery(
                    auth = NeonClient.AUTH_HEADER,
                    connStr = NeonClient.CONN_STRING,
                    request = NeonRequest(
                        query = """
                            SELECT id, bpm, estado, dispositivo, hora 
                            FROM lecturas_fc 
                            ORDER BY created_at DESC 
                            LIMIT $1
                        """.trimIndent(),
                        params = listOf(limite)
                    )
                )

                response.rows.forEach { dto ->
                    dao.upsert(
                        LecturaFC(
                            id = dto.id,
                            valorBpm = dto.bpm,
                            hora = dto.hora,
                            esNormal = dto.estado == "Normal"
                        )
                    )
                }
                Log.d(TAG, "✅ ${response.rowCount} registros descargados de Neon")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error al descargar de Neon: ${e.message}")
            }
        }

    suspend fun enviarPendientes() = withContext(Dispatchers.IO) {
        val pendientes = dao.obtenerNoSincronizados()
        pendientes.forEach { lectura ->
            try {
                sincronizarHaciaNeon(lectura)
                dao.marcarSincronizado(lectura.id.toLong())
                Log.d(TAG, "✅ Sincronizado pendiente id=${lectura.id}")
            } catch (e: Exception) {
                Log.w(TAG, "Aún sin internet: ${e.message}")
            }
        }
    }
}