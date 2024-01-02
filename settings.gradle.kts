enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://naver.jfrog.io/artifactory/maven/")
    }

    versionCatalogs {
        create("auth") {
            from(files("./gradle/auth.versions.toml"))
        }
    }
}

rootProject.name = "exampleKMP"
include(":androidApp")
include(":shared")
include(":data")
include(":presentation")
include(":domain")