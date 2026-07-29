rootProject.name = "RetireWise"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":apps:androidApp")
include(":shared:core")
include(":shared:design_system")
include(":shared:navigation")
include(":shared:authentication")
include(":shared:profile")
include(":shared:retirement_engine")
include(":shared:benefits_engine")
include(":shared:scenario_engine")
include(":shared:scenario_comparison")
include(":shared:onboarding")
include(":backend:authentication")
include(":infra")
