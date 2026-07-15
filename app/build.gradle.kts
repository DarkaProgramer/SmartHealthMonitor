import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

// ── Leer local.properties para credenciales de Neon ──────────────
val localProps = Properties()
val localFile = file("local.properties")
if (localFile.exists()) {
    localProps.load(localFile.inputStream())
}

android {
    namespace = "mx.utng.smart_health_monitor"
    compileSdk = 35

    defaultConfig {
        applicationId = "mx.utng.smart_health_monitor"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ── BuildConfig para Neon ──────────────────────────────────
        buildConfigField("String", "NEON_API_KEY",
            "\"${localProps["NEON_API_KEY"]}\"")
        buildConfigField("String", "NEON_HOST",
            "\"${localProps["NEON_HOST"]}\"")
        buildConfigField("String", "NEON_DB",
            "\"${localProps["NEON_DB"]}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("boolean", "DEBUG", "true")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // ── Compose BOM ──────────────────────────────────────────────────
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ── Material Icons Extended ─────────────────────────────────────
    implementation("androidx.compose.material:material-icons-extended")

    // ── Navigation Compose ──────────────────────────────────────────
    implementation("androidx.navigation:navigation-compose:2.8.9")

    // ── ViewModel + Compose ────────────────────────────────────────
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // ── WEARABLE DATA LAYER (S6) ────────────────────────────────────
    implementation("com.google.android.gms:play-services-wearable:18.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // ── ROOM (S7) ────────────────────────────────────────────────────
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // ── CAST SDK (S13) ──────────────────────────────────────────────
    implementation("androidx.mediarouter:mediarouter:1.7.0")
    implementation("com.google.android.gms:play-services-cast-framework:21.5.0")

    // ── RETROFIT + OKHTTP PARA NEON (PostgreSQL) ────────────────────
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // ── KOTLINX SERIALIZATION ───────────────────────────────────────
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // ── WORK MANAGER PARA SYNC PERIÓDICO ────────────────────────────
    implementation("androidx.work:work-runtime-ktx:2.9.1")

    // ── COROUTINES ──────────────────────────────────────────────────
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ── TESTS ───────────────────────────────────────────────────────
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}