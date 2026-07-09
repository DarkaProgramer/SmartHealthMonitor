package mx.utng.smart_health_monitor.tv

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import kotlinx.coroutines.launch
import mx.utng.smart_health_monitor.data.db.LecturaFC

class MainFragment : BrowseSupportFragment() {

    private val viewModel: TvViewModel by viewModels()
    private lateinit var histAdapter: ArrayObjectAdapter
    private lateinit var estadoAdapter: ArrayObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del BrowseFragment
        title = "SmartHealth TV"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // Color de la marca en el sidebar
        brandColor = resources.getColor(R.color.sh_primary, null)

        cargarFilas()
        observarDatos()
    }

    private fun cargarFilas() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        // ── Fila 1: Estado actual (FC) ───────────────
        estadoAdapter = ArrayObjectAdapter(FCCardPresenter())
        // Agregar un item por defecto mientras llegan datos reales
        estadoAdapter.add(LecturaFC(id = 0, valorBpm = 0, hora = "Esperando..."))
        rowsAdapter.add(ListRow(HeaderItem("Estado actual"), estadoAdapter))

        // ── Fila 2: Historial de FC ────────────────────
        histAdapter = ArrayObjectAdapter(FCCardPresenter())
        rowsAdapter.add(ListRow(HeaderItem("Historial FC"), histAdapter))

        this.adapter = rowsAdapter
    }

    private fun observarDatos() {
        // Observar FC actual
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fc.collect { fc ->
                    estadoAdapter.clear()
                    val estado = if (fc == 0) {
                        LecturaFC(id = 0, valorBpm = 0, hora = "Esperando...")
                    } else {
                        LecturaFC(id = 0, valorBpm = fc, hora = "Ahora")
                    }
                    estadoAdapter.add(estado)
                }
            }
        }

        // Observar historial desde Room
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.historial.collect { lecturas ->
                    histAdapter.clear()
                    if (lecturas.isEmpty()) {
                        histAdapter.add(LecturaFC(id = -1, valorBpm = 0, hora = "Sin datos"))
                    } else {
                        lecturas.forEach { histAdapter.add(it) }
                    }
                }
            }
        }
    }
}