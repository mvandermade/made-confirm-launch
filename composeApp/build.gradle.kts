import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)

    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

kotlin {
    jvm("desktop")
    jvmToolchain(17)

    sourceSets {
        val desktopMain by getting
        val desktopTest by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
        }
        commonTest.dependencies {
            // Enable tests for all platforms
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        desktopTest.dependencies {
            // Compose
            implementation("org.jetbrains.compose.ui:ui-test-junit4:1.7.0")
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "made-cl"
            packageVersion = "1.0.6"
        }
    }
}
