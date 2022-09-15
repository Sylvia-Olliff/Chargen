
plugins {
    id("chargen.kotlin-application-conventions")
    id("org.jetbrains.compose") version "1.1.0"
}

dependencies {
    implementation(project(":lib"))
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "chargen.app.AppKt"
    }
}
