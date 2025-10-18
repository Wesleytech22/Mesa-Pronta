pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT) // ← CORRIGIDO
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MesaPronta"
include(":app")