package mx.utng.smart_health_monitor.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

// ── Room Entity: tabla lecturas_fc ────────────────────────────────────────────
@Entity(tableName = "lecturas_fc")
data class LecturaFC(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val valorBpm: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val hora: String = java.text.SimpleDateFormat(
        "HH:mm", java.util.Locale.getDefault()
    ).format(java.util.Date()),
    val esNormal: Boolean = valorBpm in 60..100
)
