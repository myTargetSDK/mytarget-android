plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.my.targetDemoApp"
        minSdkVersion(16)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.my.targetDemoTests.helpers.ScreenshotTestRunner"
        vectorDrawables.useSupportLibrary = true
    }
    lintOptions {
        isAbortOnError = true
        isWarningsAsErrors = true
        lintConfig = File("../lint.xml")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.my.target:mytarget-sdk:$SDK_VERSION")
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

    androidTestImplementation("junit:junit:${Test.junit}")
    androidTestImplementation("androidx.test:runner:${Test.runner}")
    androidTestImplementation("androidx.test:rules:${Test.rules}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Test.espresso}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Test.espresso}")
    androidTestImplementation("androidx.test.espresso:espresso-intents:${Test.espresso}")
    androidTestImplementation("androidx.test.espresso:espresso-web:${Test.espresso}")
    androidTestImplementation("com.schibsted.spain:barista:${Test.barista}") {
        exclude(group = "com.android.support")
        exclude(group = "org.jetbrains.kotlin")
    }
}

apply(plugin = "shot")

configure<com.karumi.shot.ShotExtension>
{
    appId = "com.my.targetDemoApp"
}