
plugins {
    id("chargen.kotlin-common-conventions")

    `java-library`
}

dependencies {
    api("com.sksamuel.hoplite:hoplite-core:2.6.2")
    api("com.sksamuel.hoplite:hoplite-yaml:2.6.2")
    api("com.sksamuel.hoplite:hoplite-watch:2.6.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}
