plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    namespace = "com.my.targetDemoApp"

    compileSdk = AndroidSdk.compile
    defaultConfig {
        applicationId = "com.my.targetDemoApp"
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
        val ver = findVersionName()
        versionName = ver
        versionCode = convertVerToCode(ver)
        testInstrumentationRunner = "com.my.targetDemoTests.helpers.ScreenshotTestRunner"
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
    }
    lint {
        abortOnError = true
        warningsAsErrors = true
        lintConfig = File("../lint.xml")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    val sdkversion: String? by project
    debugImplementation("com.my.target:mytarget-sdk:$SDK_VERSION")
    releaseImplementation("com.my.target:mytarget-sdk:$sdkversion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$KOTLIN_VERSION")
    implementation("androidx.appcompat:appcompat:${AndroidX.appcompat}")
    implementation("com.google.android.material:material:${AndroidX.material}")
    implementation("androidx.cardview:cardview:${AndroidX.cardview}")
    implementation("androidx.recyclerview:recyclerview:${AndroidX.recyclerview}")
    implementation("androidx.constraintlayout:constraintlayout:${AndroidX.constraint}")
    implementation("com.google.android.gms:play-services-ads-identifier:${Google.play}")
    implementation("com.google.android.exoplayer:exoplayer:${Google.exoplayer}")
    implementation("androidx.navigation:navigation-fragment-ktx:${AndroidX.navigation}")
    implementation("androidx.navigation:navigation-ui-ktx:${AndroidX.navigation}")
    implementation("androidx.preference:preference-ktx:${AndroidX.preference}")
    implementation("androidx.multidex:multidex:${AndroidX.multidex}")

    androidTestImplementation("io.qameta.allure:allure-kotlin-model:${Test.allure}")
    androidTestImplementation("io.qameta.allure:allure-kotlin-commons:${Test.allure}")
    androidTestImplementation("io.qameta.allure:allure-kotlin-junit4:${Test.allure}")
    androidTestImplementation("io.qameta.allure:allure-kotlin-android:${Test.allure}")

    androidTestImplementation("junit:junit:${Test.junit}")
    androidTestImplementation("androidx.test:core:${Test.core}")
    androidTestImplementation("androidx.test:runner:${Test.runner}")
    androidTestImplementation("androidx.test:rules:${Test.rules}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Test.espresso}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Test.espresso}") {
        exclude(group = "org.checkerframework", module = "checker")
    }
    androidTestImplementation("androidx.test.espresso:espresso-intents:${Test.espresso}")
    androidTestImplementation("androidx.test.espresso:espresso-web:${Test.espresso}")
    androidTestImplementation("com.adevinta.android:barista:${Test.barista}") {
        exclude(group = "com.android.support")
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "org.checkerframework", module = "checker")
    }
}

apply(plugin = "shot")

configure<com.karumi.shot.ShotExtension> {
    appId = "com.my.targetDemoApp"
}

fun convertVerToCode(versionName: String): Int {
    return versionName.split(".")
            .take(3)
            .mapIndexed { index, s ->
                s.toInt() * (when (index) {
                    0 -> 1_000_000
                    1 -> 1000
                    else -> 1
                })
            }
            .sum()
}

fun findVersionName(): String {
    val sdkversion: String? by project
    return sdkversion ?: SDK_VERSION
}
