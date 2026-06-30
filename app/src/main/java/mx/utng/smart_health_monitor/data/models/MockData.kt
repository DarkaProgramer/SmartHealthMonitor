package mx.utng.smart_health_monitor.data.models

import mx.utng.smart_health_monitor.data.db.LecturaFC

object MockData {
    val historialFC = listOf(
        LecturaFC(1, 78, hora = "11:00"),
        LecturaFC(2, 82, hora = "10:30"),
        LecturaFC(3, 76, hora = "10:00"),
        LecturaFC(4, 95, hora = "09:30", esNormal = false),
        LecturaFC(5, 71, hora = "09:00"),
        LecturaFC(6, 80, hora = "08:30"),
        LecturaFC(7, 74, hora = "08:00")
    )

    var fcActual = 78
    var pasosActual = 4250
}