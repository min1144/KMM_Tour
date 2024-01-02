@file:Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.multiplatform)
    kotlin("native.cocoapods")
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.compose)
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.moko.resources)
    alias(libs.plugins.kotlinx.atomicfu)
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
        ios.deploymentTarget = libs.versions.ios.deployment.target.get()
        version = "1.0"
        framework {
            isStatic = true
        }

        pod(libs.versions.pod.naverMap.get())

        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":domain"))
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.runtime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.atomicfu)
                implementation(libs.kotlin.stdlib)

                // Javax Inject
                implementation(libs.javax.inject)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.logging)

                implementation(libs.bundles.moko)
                implementation(libs.bundles.moko.resourse)
                implementation(libs.bundles.decompose)

                implementation(libs.koin.core)
                implementation(libs.koin.compose)

                implementation(libs.bundles.compose.imageloader)
                implementation(libs.napier)
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
                implementation(libs.naver.map)
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
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = libs.versions.namespace.get() + ".presentation"
    iosBaseLocalizationRegion = "ko"
    multiplatformResourcesClassName = "RR"
    disableStaticFrameworkWarning = true
}

android {
    namespace = libs.versions.namespace.get() + ".presentation"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvm.target.get().toInt())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvm.target.get().toInt())
    }
}