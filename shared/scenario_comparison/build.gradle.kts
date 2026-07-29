plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

// The Compose Multiplatform plugin pulls in kotlinx-datetime 0.7.1 as a
// strict constraint for native (iOS) targets, while the Android target
// resolves the version catalogue's 0.6.1 — an inconsistency that breaks
// `Clock.System` (a different, incompatible API shape between the two
// versions) if left unresolved. Force a single version across every
// configuration so `kotlinx.datetime.Instant`/`Clock` are the same type on
// every platform this module targets.
configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlinx:kotlinx-datetime:${libs.versions.datetime.get()}")
    }
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
            baseName = "ScenarioComparison"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":shared:core"))
            api(project(":shared:profile"))
            api(project(":shared:retirement_engine"))
            api(project(":shared:design_system"))
            implementation(project(":shared:scenario_engine"))
            // retirement_engine's own dependency on benefits_engine is
            // `implementation`, so CanadaBenefitRuleRepositoryV1 (project()'s
            // default parameter type) isn't transitively visible otherwise —
            // same issue ADR-0008 documented for scenario_engine itself.
            implementation(project(":shared:benefits_engine"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            // Same icon set as shared/design_system/shared/navigation (see
            // docs/ADR/0005-design-system-v2-tokens-typeface-icons.md) — the
            // screens in this module reference Icons.Filled.* directly.
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.materialIconsExtended)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

android {
    namespace = "com.retirewise.scenariocomparison"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
