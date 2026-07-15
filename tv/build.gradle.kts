plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}

android {
    namespace = "mx.utng.smart_health_monitor.tv"
    compileSdk = 35

    defaultConfig {
        applicationId = "mx.utng.smart_health_monitor.tv"
        minSdk = 21
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
    // Leanback Library
    implementation("androidx.leanback:leanback:1.2.0")

    // Glide para cargar imágenes
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Compartir Room + Repository con módulo app
    implementation(project(":app"))

    // ViewModel + Coroutines
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // Fragment
    implementation("androidx.fragment:fragment-ktx:1.8.5")

    // Core KTX
    implementation("androidx.core:core-ktx:1.13.1")

    // Navigation Compose para TV
    implementation("androidx.navigation:navigation-compose:2.8.9")

    // Compose para TV
    implementation("androidx.compose.ui:ui:1.7.5")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.5")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.activity:activity-compose:1.9.3")

    // ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")

    // Lifecycle Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // ── ECLIPSE PAHO MQTT ────────────────────────────────────────────
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")

    // ── KOTLINX SERIALIZATION ───────────────────────────────────────
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}