plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
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

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
}