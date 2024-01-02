@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
}


@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = libs.versions.jvm.target.get()
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            isStatic = false
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain"))
                implementation(libs.bundles.ktor)
                implementation(libs.ktor.client.logging)
                implementation(libs.kotlin.stdlib)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting{
            dependsOn(commonMain)
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosSet = arrayOf(iosArm64Main, iosArm64Main, iosSimulatorArm64Main)
        val iosMain by getting{
            dependsOn(commonMain)
            iosSet.forEach {
                it.dependsOn(this)
            }
            dependencies {
                implementation(libs.ktor.darwin)
            }
        }
    }
}

android {
    namespace = libs.versions.namespace.get() + "data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvm.target.get().toInt())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvm.target.get().toInt())
    }
}