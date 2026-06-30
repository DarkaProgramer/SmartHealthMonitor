# SmartHealth Monitor

![Android CI](https://img.shields.io/badge/Android-API26+-green)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-MD3-blue)
![Version](https://img.shields.io/badge/Version-1.0.0-blue)

Aplicación Android multiplataforma para monitoreo de salud personal en tiempo real.

Desarrollada como proyecto integrador en UTNG — 9° Cuatrimestre 2025.

---

## Stack tecnológico

| Tecnología | Uso |
|------------|-----|
| Kotlin + Jetpack Compose | UI declarativa con Material Design 3 |
| Wearable Data Layer API | Comunicación reloj ↔ teléfono (BLE) |
| Health Services API | Sensor FC real en background (Wear OS) |
| Room Database | Historial persistente de lecturas FC |
| Jetpack Navigation | NavHost entre 4 pantallas |
| Android TV / Leanback + Media3 | Pantalla para Android TV |
| GitHub + Conventional Commits | Control de versiones profesional |

---

## Pantallas implementadas

| Pantalla | Sesión | Estado |
|----------|--------|--------|
| LoginScreen | S4 | ✅ Implementada |
| DashboardScreen | S5 | ✅ Implementada |
| Historial + wearable real | S6-S7 | ✅ Implementada |
| AlertaScreen | S8 | ✅ Implementada |
| Android TV | S10-S12 | ⬜ Pendiente |

---

## Cómo ejecutar el proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/DarkaProgramer/SmartHealthMonitor.git
## Autor
Claudio Angel Huerta Ducoing — UTNG
