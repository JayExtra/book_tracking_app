plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinPlugin)
    id(Plugins.daggerHilt)
    id(Plugins.kapt)
    id(Plugins.kspPlugin) version (Plugins.kspPluginVersion)
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

    testOptions {
        unitTests {
          isIncludeAndroidResources = true
        }
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
    buildFeatures{
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
}

kotlin {
    sourceSets {
        debug {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        release {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

dependencies {

    implementation(project(Modules.core))
    implementation(project(Modules.composeUi))
    implementation(project(Modules.coreDataStore))
    implementation(project(Modules.coreNetwork))
    implementation(project(Modules.coreDatabase))

    implementation("io.github.raamcosta.compose-destinations:animations-core:${Versions.navDestinations}")
    ksp("io.github.raamcosta.compose-destinations:ksp:${Versions.navDestinations}")

    implementation("androidx.camera:camera-camera2:${Versions.camerx_version}")
    implementation("androidx.camera:camera-lifecycle:${Versions.camerx_version}")
    implementation("androidx.camera:camera-view:${Versions.camerx_version}")

    
/*
    implementation ("com.google.accompanist-permissions:0.21.1-beta")
*/


}

ksp{
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName" , "home")
}

