/*
 * build.gradle.kts for the app module.
 * Configures plugins, Android settings, build options, and dependencies.
 */

plugins {
    // Apply Android application plugin and Kotlin plugins.
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Enable Kotlin annotation processing (used for Room).
    id("kotlin-kapt")
}

android {
    // Set the package namespace.
    namespace = "com.example.scriba"
    // Specify the compile SDK version.
    compileSdk = 35

    defaultConfig {
        // Unique application ID.
        applicationId = "com.example.scriba"
        // Minimum SDK version supported.
        minSdk = 24
        // Target SDK version.
        targetSdk = 35
        // App versioning.
        versionCode = 1
        versionName = "1.0"

        // Instrumentation test runner.
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // Release build configuration.
        release {
            // Disable code minification for the release build.
            isMinifyEnabled = false
            // Specify ProGuard rules for release builds.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    // Configure Java compatibility options.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    // Configure Kotlin compiler options.
    kotlinOptions {
        jvmTarget = "11"
        languageVersion = "1.8" // Force Kotlin 1.8 features.
    }
    // Enable Jetpack Compose features.
    buildFeatures {
        compose = true
    }
}

// Configuration for Kotlin Annotation Processing Tool (KAPT).
kapt {
    correctErrorTypes = true
    arguments {
        arg("room.incremental", "false")
        arg("kapt.incremental.apt", "false")
    }
}

dependencies {
    // Android instrumentation tests.
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Use Compose BOM (Bill of Materials) for version alignment in Android tests.
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Debug dependencies for UI tooling.
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Room annotation processor.
    kapt(libs.androidx.room.compiler)

    // Core implementation dependencies.
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    // Use Compose BOM for implementation dependencies.
    implementation(platform(libs.androidx.compose.bom))
}
