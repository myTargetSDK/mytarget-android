@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("gradle/libs-common.versions.toml"))
        }
        create("testLibs") {
            from(files("gradle/libs-test.versions.toml"))
        }
    }

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        maven(url = "file://${rootDir}/../repository")
        google()
        mavenCentral()
        flatDir { dirs("libs") }
    }
}

rootProject.name = "myTargetDemo"

include(":app")
