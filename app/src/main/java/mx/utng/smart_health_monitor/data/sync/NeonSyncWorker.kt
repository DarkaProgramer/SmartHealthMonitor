package mx.utng.smart_health_monitor.data.sync

import android.content.Context
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.utng.smart_health_monitor.data.db.SmartHealthDB
import mx.utng.smart_health_monitor.data.repository.SyncRepository
import java.util.concurrent.TimeUnit

class NeonSyncWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val db = SmartHealthDB.getDatabase(applicationContext)
                val repo = SyncRepository(db.lecturaDao())

                // 1. Enviar pendientes locales a Neon
                repo.enviarPendientes()

                // 2. Descargar los más recientes de Neon
                repo.sincronizarDesdeNeon(limite = 100)

                Log.d("SYNC_WORKER", "✅ Sync completado")
                Result.success()
            } catch (e: Exception) {
                Log.e("SYNC_WORKER", "❌ Sync fallido: ${e.message}")
                Result.retry()
            }
        }
    }

    companion object {
        const val WORK_NAME = "NeonSyncWork"

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<NeonSyncWorker>(
                30, TimeUnit.MINUTES
            ).setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}