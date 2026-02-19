@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs-common.versions.toml"))
        }
        create("testLibs") {
            from(files("../gradle/libs-test.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")