plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "mx.utng.smart_health_monitor.wear"
    compileSdk = 35

    defaultConfig {
        applicationId = "mx.utng.smart_health_monitor.wear"
        minSdk = 30  // Wear OS requiere API 30+
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
}

dependencies {
    // Health Services API
    implementation("androidx.health:health-services-client:1.1.0-alpha03")

    // Coroutines para Guava ListenableFuture
    implementation("com.google.guava:guava:33.0.0-android")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.7.3")

    // Compose para Wear OS
    implementation("androidx.compose.ui:ui:1.7.5")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.5")
    implementation("androidx.wear.compose:compose-material:1.4.0")

    // Activity Compose
    implementation("androidx.activity:activity-compose:1.9.3")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}