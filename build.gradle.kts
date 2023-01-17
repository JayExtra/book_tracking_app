import org.gradle.internal.impldep.org.eclipse.jgit.util.RawCharUtil.trimTrailingWhitespace

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
    id (Plugins.spotlessPlugin) version (Versions.spotlessVersion)
    id (Plugins.ktlintPlugin) version (Versions.ktlintVersion)
}

subprojects{
    apply(plugin = Plugins.spotlessPlugin)
    spotless{
        kotlin {
            target("**/*.kt")
            indentWithSpaces()
            trimTrailingWhitespace()
            endWithNewline()
        }

        apply(plugin = Plugins.ktlintPlugin)
        ktlint {
            android.set(true)
            verbose.set(true)
            debug.set(true)
            outputToConsole.set(true)
            outputColorName.set("RED")
            ignoreFailures.set(false)
            enableExperimentalRules.set(true)
            disabledRules.set(listOf("final-newline" , "no-wildcard-imports" , "max-line-length"))
            filter {
                exclude { element -> element.file.path.contains("generated/") }
            }
        }
    }
}




