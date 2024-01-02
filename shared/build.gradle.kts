@file:Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.multiplatform)
    kotlin("native.cocoapods")
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.compose)
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.serialization)
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
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            isStatic = true
        }

        pod(libs.versions.pod.naverMap.get(), linkOnly = true)
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":data"))
                implementation(project(":domain"))
                implementation(project(":presentation"))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)

                implementation(libs.bundles.moko)
                implementation(libs.bundles.decompose)
                implementation(libs.napier)
                implementation(libs.bundles.ktor)
                implementation(libs.ktor.client.logging)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                dependsOn(commonMain)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.core)

                implementation(compose.uiTooling)
                implementation(compose.preview)

                implementation(libs.bundles.koin)
                implementation(libs.ktor.okhttp)
                implementation(libs.ktor.client.logging.jvm)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(libs.ktor.darwin)
            }
        }
    }
}

android {
    namespace = libs.versions.namespace.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvm.target.get().toInt())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvm.target.get().toInt())
    }
    //jvm target 버전을 늦게 설정해야 ios 빌드가 됨
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = libs.versions.jvm.target.get()
        }
    }
}