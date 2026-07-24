plugins {
    alias(libs.plugins.kotlinJvm)
    application
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.retirewise.infra.MainKt")
}

dependencies {
    implementation(libs.aws.cdk.lib)
    implementation(libs.aws.constructs)
    testImplementation(kotlin("test"))
}
