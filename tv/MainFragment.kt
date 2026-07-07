package mx.utng.smart_health_monitor.tv

import android.os.Bundle
import android.view.View
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import mx.utng.smart_health_monitor.data.db.LecturaFC

class MainFragment : BrowseSupportFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del BrowseFragment
        title = "SmartHealth TV"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // Color de la marca en el sidebar
        brandColor = resources.getColor(R.color.sh_primary, null)

        cargarFilas()
    }

    private fun cargarFilas() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // ── Fila 1: Estado actual (FC + Pasos) ───────────
        val estadoAdapter = ArrayObjectAdapter(FCCardPresenter())

        // Datos simulados - en Ej.03 vendrán de Room
        estadoAdapter.add(LecturaFC(id = 0, valorBpm = 88, hora = "Ahora"))
        estadoAdapter.add(LecturaFC(id = 1, valorBpm = 4250, hora = "Pasos"))

        rowsAdapter.add(ListRow(HeaderItem("Estado actual"), estadoAdapter))

        // ── Fila 2: Historial de FC ────────────────────
        val histAdapter = ArrayObjectAdapter(FCCardPresenter())

        // Datos simulados
        histAdapter.add(LecturaFC(id = 2, valorBpm = 72, hora = "10:30"))
        histAdapter.add(LecturaFC(id = 3, valorBpm = 95, hora = "10:00"))
        histAdapter.add(LecturaFC(id = 4, valorBpm = 68, hora = "09:30"))
        histAdapter.add(LecturaFC(id = 5, valorBpm = 110, hora = "09:00"))
        histAdapter.add(LecturaFC(id = 6, valorBpm = 75, hora = "08:30"))

        rowsAdapter.add(ListRow(HeaderItem("Historial FC"), histAdapter))

        this.adapter = rowsAdapter
    }
}