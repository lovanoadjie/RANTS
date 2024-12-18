pluginManagement {
    repositories {
        google {
            content {
                // Termasuk semua grup Android dan Google
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral() // Repositori Maven Central untuk dependensi umum
        gradlePluginPortal() // Plugin Gradle
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // Repositori Google
        mavenCentral() // Repositori Maven Central
//        maven { url "https://jitpack.io" } // Repositori JitPack untuk library pihak ketiga
    }
}

rootProject.name = "RANTS"
include(":app")
