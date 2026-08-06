import java.util.Locale

buildscript {
    extra.apply {
        set("sdkversion", (project.properties["sdkversion"] as String?) ?: libs.versions.com.my.target.mytargetSdk.get())
    }

    // Legacy плагины, для которых нет варианта подключения через `plugins`
    dependencies {
        classpath("com.karumi:shot:${libs.versions.karumi.plugin.get()}")
    }
}

plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.github.benManes.versions)
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any {
        version.uppercase(Locale.getDefault())
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