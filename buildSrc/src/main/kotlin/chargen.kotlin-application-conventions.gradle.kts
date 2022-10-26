import org.jetbrains.compose.compose

plugins {
    id("chargen.kotlin-common-conventions")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(Deps.ArkIvanov.Decompose.decompose)
    implementation(Deps.ArkIvanov.Decompose.extensionsCompose)
    implementation(compose.desktop.common)
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "chargen.app.AppKt"
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
