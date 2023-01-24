plugins {
    id(Plugins.androidApplication)
    id(Plugins.kotlinPlugin)
    id(Plugins.kapt)
    id(Plugins.parcelize)
    id(Plugins.daggerHilt)
    id(Plugins.kspPlugin) version (Plugins.kspPluginVersion)
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.dev.james.booktracker"
    compileSdk =  AndroidSdk.compileSdk

    defaultConfig {
        applicationId =  AndroidSdk.applicationId
        minSdk = AndroidSdk.minSdk
        targetSdk = AndroidSdk.targetSdk
        versionCode = 1
        versionName = AndroidSdk.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    applicationVariants.all{
        kotlin.sourceSets {
            getByName(name){
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {
    implementation(project(Modules.core))
    implementation(project(Modules.composeUi))
    implementation(project(Modules.onBoarding))
    implementation(project(Modules.home))
    implementation(project(Modules.coreDataStore))

    implementation("io.github.raamcosta.compose-destinations:animations-core:${Versions.navDestinations}")
    ksp("io.github.raamcosta.compose-destinations:ksp:${Versions.navDestinations}")
}