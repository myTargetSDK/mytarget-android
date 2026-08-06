plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.com.my.target.android.buildTypesPlugin)
    alias(libs.plugins.com.my.target.android.compilePlugin)
    alias(libs.plugins.com.my.target.android.configPlugin)
    alias(libs.plugins.com.my.target.android.lintPlugin)
}

// Legacy плагин, на данный момент нет способа подключить через `plugins`
apply(plugin = "shot")

android {
    namespace = "com.my.targetDemoApp"
}

dependencies {
    debugImplementation("com.my.target:mytarget-sdk:${libs.versions.com.my.target.mytargetSdk.get()}")
    releaseImplementation("com.my.target:mytarget-sdk:${rootProject.extra["sdkversion"] as String}")

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.multidex)
    implementation(libs.bundles.androidx.navigation)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.recyclerview)

    implementation(libs.com.google.android.gms.playservices.adsIdentifier)
    implementation(libs.com.google.android.material)

    implementation(libs.org.jetbrains.kotlin.stdlib)

    androidTestImplementation(testLibs.androidx.test.core)
    androidTestImplementation(testLibs.androidx.test.espresso.contrib) {
        exclude(group = "org.checkerframework", module = "checker")
    }
    androidTestImplementation(testLibs.androidx.test.espresso.core)
    androidTestImplementation(testLibs.androidx.test.espresso.intents)
    androidTestImplementation(testLibs.androidx.test.espresso.web)
    androidTestImplementation(testLibs.androidx.test.rules)
    androidTestImplementation(testLibs.com.adevinta.android.barista) {
        exclude(group = "com.android.support")
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "org.checkerframework", module = "checker")
    }

    androidTestImplementation(testLibs.bundles.io.qameta.allure.kotlin)
    androidTestImplementation(testLibs.junit)
}
