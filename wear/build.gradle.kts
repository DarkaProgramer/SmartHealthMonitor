plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}

android {
    namespace = "mx.utng.smart_health_monitor.wear"
    compileSdk = 35

    defaultConfig {
        applicationId = "mx.utng.smart_health_monitor.wear"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {
    // Compose para Wear OS
    implementation("androidx.wear.compose:compose-material:1.3.1")
    implementation("androidx.wear.compose:compose-foundation:1.3.1")
    implementation("androidx.wear.compose:compose-navigation:1.3.1")

    // Horologist
    implementation("com.google.android.horologist:horologist-compose-layout:0.6.17")
    implementation("com.google.android.horologist:horologist-compose-material:0.6.17")

    // Compartir Repository con el módulo app
    implementation(project(":app"))

    // Activity Compose
    implementation("androidx.activity:activity-compose:1.9.3")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Health Services API
    implementation("androidx.health:health-services-client:1.1.0-alpha03")

    // Wearable Data Layer
    implementation("com.google.android.gms:play-services-wearable:18.2.0")

    // ── ECLIPSE PAHO MQTT ────────────────────────────────────────────
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")

    // ── KOTLINX SERIALIZATION ───────────────────────────────────────
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}