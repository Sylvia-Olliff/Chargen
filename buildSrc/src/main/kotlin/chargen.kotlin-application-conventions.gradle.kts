
plugins {
    id("chargen.kotlin-common-conventions")
}

dependencies {
    implementation("com.arkivanov.decompose:decompose:1.0.0-alpha-04")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:1.0.0-alpha-04")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
