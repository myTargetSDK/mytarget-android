import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.my.target.buildlogic"

// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
    }
}

dependencies {
    compileOnly(libs.buildLogic.android.gradlePlugin)
    compileOnly(libs.buildLogic.kotlin.gradlePlugin)
}

// Тут регистрируем собственные плагины из папки build-logic
gradlePlugin {
    plugins {
        register("androidBuildTypesPlugin") {
            id = "com.my.target.android.buildTypesPlugin"
            implementationClass = "AndroidBuildTypesConventionPlugin"
        }
        register("androidCompilePlugin") {
            id = "com.my.target.android.compilePlugin"
            implementationClass = "AndroidCompileConventionPlugin"
        }
        register("androidConfigPlugin") {
            id = "com.my.target.android.configPlugin"
            implementationClass = "AndroidConfigConventionPlugin"
        }
        register("androidLintPlugin") {
            id = "com.my.target.android.lintPlugin"
            implementationClass = "AndroidLintConventionPlugin"
        }
    }
}

