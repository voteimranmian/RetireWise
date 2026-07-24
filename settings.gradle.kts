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
include(":backend:authentication")
include(":infra")
