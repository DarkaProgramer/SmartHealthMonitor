package mx.utng.smart_health_monitor.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ── DAO: operaciones sobre lecturas_fc ────────────────────────────────────────
@Dao
interface LecturaFCDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(lectura: LecturaFC)

    /** Flow reactivo: la UI se actualiza automáticamente al insertar datos */
    @Query("""
        SELECT * FROM lecturas_fc
        ORDER BY timestamp DESC
        LIMIT 50
    """)
    fun obtenerUltimas(): Flow<List<LecturaFC>>

    @Query("SELECT COUNT(*) FROM lecturas_fc")
    suspend fun contarRegistros(): Int

    /** Elimina lecturas más antiguas de N milisegundos */
    @Query("""
        DELETE FROM lecturas_fc
        WHERE timestamp < :limite
    """)
    suspend fun limpiarViejos(limite: Long)
}
