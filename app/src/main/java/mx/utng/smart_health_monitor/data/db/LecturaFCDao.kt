package mx.utng.smart_health_monitor.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LecturaFCDao {
    @Query("SELECT * FROM lecturas_fc ORDER BY timestamp DESC LIMIT 50")
    fun obtenerUltimas(): Flow<List<LecturaFC>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(lectura: LecturaFC): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(lectura: LecturaFC)

    @Query("SELECT * FROM lecturas_fc WHERE sincronizado = 0")
    suspend fun obtenerNoSincronizados(): List<LecturaFC>

    @Query("UPDATE lecturas_fc SET sincronizado = 1 WHERE id = :id")
    suspend fun marcarSincronizado(id: Long)

    @Query("SELECT COUNT(*) FROM lecturas_fc WHERE sincronizado = 0")
    fun contarPendientes(): Flow<Int>
}