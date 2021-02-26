plugins {
    id("com.github.ben-manes.versions") version Plugins.VERSIONS
}
allprojects {
    repositories {
        maven(url = "file://${rootDir}/../repository")
        google()
        mavenCentral()
        jcenter()
        flatDir { dirs("libs") }
    }
}

buildscript {
    repositories {
        maven(url = "file://${rootDir}/../repository")
        google()
        mavenCentral()
        jcenter()
        flatDir { dirs("libs") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Plugins.AGP}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$KOTLIN_VERSION")
        classpath("com.karumi:shot:${Plugins.KARUMI}")
        classpath("com.karumi:core:${Plugins.KARUMI}")
        classpath("com.github.ben-manes:gradle-versions-plugin:${Plugins.VERSIONS}")
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any {
        version.toUpperCase()
                .contains(it)
    }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
