import org.gradle.api.JavaVersion

const val kotlinVersion = "1.6.10"
object BuildPlugins {
    object Versions {
        const val buildToolsVersion = "3.3.1"
    }
}

//android configuration
object AndroidSdk {
    const val compileSdk = 33
    const val minSdk = 21
    const val targetSdk = 33
    const val versionCode = 1
    const val versionName = "1.0.0-alpha01"
    const val applicationId = "com.dev.james.booktracker"

    val javaVersion = JavaVersion.VERSION_1_8
}

//all your modules will be defined here
object Modules {
    const val app = ":app"

    const val core = ":core"
    const val coreDatabase = ":core-database"
    const val coreDataStore = ":core-datastore"
    const val coreNetwork = ":core-network"
    const val composeUi = ":compose-ui"
    const val onBoarding = ":feature:on-boarding"
}

//plugins
object Plugins {
    const val androidApplicationVersion = "7.3.1"
    const val androidLibraryVersion = "7.3.1"
    const val kotlinPluginVersion = "1.6.10"

    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val kotlinPlugin = "org.jetbrains.kotlin.android"
}

object Versions{
    const val composeCompiler = "1.1.1"
    const val composeUiVersion = "1.1.1"
    const val kotlinCompilerVersion = "1.1.1"
    const val jvmTargetVersion = "1.8"

}