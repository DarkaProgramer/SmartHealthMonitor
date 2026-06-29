package mx.utng.smart_health_monitor.wear

import android.content.Context
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class WearDataSender(private val context: Context) {

    suspend fun enviarFC(bpm: Int) {
        try {
            val nodes = Wearable.getNodeClient(context).connectedNodes.await()
            if (nodes.isEmpty()) return

            val data = bpm.toString().toByteArray()
            nodes.forEach { node ->
                Wearable.getMessageClient(context)
                    .sendMessage(node.id, "/smarthealthmonitor/fc", data)
                    .await()
            }
        } catch (e: Exception) {
            // Log error
        }
    }

    suspend fun enviarPasos(pasos: Int) {
        try {
            val nodes = Wearable.getNodeClient(context).connectedNodes.await()
            if (nodes.isEmpty()) return

            val data = pasos.toString().toByteArray()
            nodes.forEach { node ->
                Wearable.getMessageClient(context)
                    .sendMessage(node.id, "/smarthealthmonitor/pasos", data)
                    .await()
            }
        } catch (e: Exception) {
            // Log error
        }
    }
}