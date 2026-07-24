plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

// Compose Resources generates accessor code (e.g. Res.font.*) under
// build/generated, mixed into the same commonMain source set as hand-written
// code. A glob-based exclude ("**/generated/**") only reliably excludes it
// from ktlintCheck's underlying source FileTree, not ktlintFormat's — both
// task types share the same PatternFilterable wiring, but their FileTree
// unions resolve relative paths differently in practice. Matching on the
// element's absolute file path instead is unambiguous across both task
// types.
ktlint {
    filter {
        exclude { entry -> entry.file.path.contains("/generated/") }
    }
}

// Module identifier for the generated Compose Resources accessors (Res.font.*).
// See https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-resources.html
compose.resources {
    packageOfResClass = "com.retirewise.designsystem.generated.resources"
}

kotlin {
    androidTarget {
        @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DesignSystem"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":shared:core"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            // Only the Compose Multiplatform-maintained icon set covers every glyph the
            // design system needs (see docs/ADR/0005-design-system-v2-tokens-typeface-icons.md).
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.materialIconsExtended)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "com.retirewise.designsystem"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
