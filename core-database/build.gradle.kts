plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinPlugin)
    id(Plugins.daggerHilt)
    id(Plugins.kapt)
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    compileSdk = AndroidSdk.compileSdk

    defaultConfig {
        minSdk = AndroidSdk.minSdk
        targetSdk = AndroidSdk.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = AndroidSdk.javaVersion
        targetCompatibility = AndroidSdk.javaVersion
    }
    kotlinOptions {
        jvmTarget = Versions.jvmTargetVersion
    }
}

dependencies {

    implementation(project(Modules.core))
    // Room
    implementation("androidx.room:room-runtime:2.5.0")
    kapt("androidx.room:room-compiler:2.5.0")
    testImplementation("androidx.room:room-testing:2.5.0")
    androidTestImplementation("androidx.room:room-testing:2.5.0")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.5.0")

    implementation("com.google.code.gson:gson:2.9.0")

}