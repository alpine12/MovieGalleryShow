pluginManagement {
    val agpVersion = "8.6.0"
    val kspVersion = "2.2.0-2.0.2"
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version agpVersion
        id("org.jetbrains.kotlin.android") version "2.2.0"
        id("org.jetbrains.kotlin.kapt") version "2.2.0"
        id("org.jetbrains.kotlin.plugin.parcelize") version "2.2.0"
        id ("com.google.dagger.hilt.android") version "2.56.2" apply false
        id("androidx.navigation.safeargs.kotlin") version "2.7.7"
        id("com.google.devtools.ksp") version kspVersion apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Movie Gallery"
include(":app")