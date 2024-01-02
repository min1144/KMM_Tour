@file:Suppress("DSL_SCOPE_VIOLATION")
plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.kotlin.jvm).apply(false)
    alias(libs.plugins.multiplatform).apply(false)
    alias(libs.plugins.kotlin.serialization).apply(false)
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.jetbrains.compose).apply(false)
    alias(libs.plugins.moko.resources).apply(false)
    alias(libs.plugins.kotlinx.atomicfu) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}