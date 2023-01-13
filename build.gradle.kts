buildscript {
    extra.apply{
        set("compose_ui_version" , "1.1.1")
    }
    repositories {
        google()
        mavenCentral()
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
val composeUiVersion = rootProject.extra["compose_ui_version"] as String

plugins {
    id (Plugins.androidApplication) version (Plugins.androidApplicationVersion) apply false
    id (Plugins.androidLibrary) version (Plugins.androidLibraryVersion) apply false
    id (Plugins.kotlinPlugin) version (Plugins.kotlinPluginVersion) apply false
}