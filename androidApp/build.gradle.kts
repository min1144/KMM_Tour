@file:Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = libs.versions.namespace.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = libs.versions.namespace.get()
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()
    }

    @Suppress("UnstableApiUsage")
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/versions/9/previous-compilation-data.bin"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            addManifestPlaceholders(mapOf(
                "naverClientId" to auth.versions.naver.client.id.get(),
                "naverClientSecret" to auth.versions.naver.client.secret.get(),
            ))
        }

        getByName("debug") {
            isMinifyEnabled = false
            addManifestPlaceholders(mapOf(
                "naverClientId" to auth.versions.naver.client.id.get(),
                "naverClientSecret" to auth.versions.naver.client.secret.get(),
            ))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvm.target.get().toInt())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvm.target.get().toInt())
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.android.compose.ui)
    implementation(libs.android.compose.ui.tooling)
    implementation(libs.android.compose.ui.tooling.preview)
    implementation(libs.android.compose.foundation)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.koin)
}