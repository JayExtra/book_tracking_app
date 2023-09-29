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
    //moshi dependency
    implementation("com.squareup.moshi:moshi-kotlin:${Versions.moshi_version}")

    //Gson converter factory
    implementation ("com.squareup.retrofit2:converter-gson:${Versions.retrofit_version}")

    //retrofit dependency
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit_version}")

    //retrofit-moshi
    implementation("com.squareup.retrofit2:converter-moshi:${Versions.retrofit_version}")

    //okhttp logging interceptor
    implementation ("com.squareup.okhttp3:logging-interceptor:${Versions.okhttp_version}")
    implementation ("com.squareup.okhttp3:okhttp:${Versions.okhttp_version}")

}