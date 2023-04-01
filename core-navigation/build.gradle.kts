plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinPlugin)
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
    implementation(project(Modules.home))
    implementation(project(Modules.onBoarding))

    implementation("io.github.raamcosta.compose-destinations:animations-core:${Versions.navDestinations}")
    ksp("io.github.raamcosta.compose-destinations:ksp:${Versions.navDestinations}")

}

