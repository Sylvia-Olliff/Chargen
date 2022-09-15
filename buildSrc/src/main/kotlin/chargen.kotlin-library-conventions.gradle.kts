
plugins {
    id("chargen.kotlin-common-conventions")

    `java-library`
}

dependencies {
    api("com.sksamuel.hoplite:hoplite-core:2.6.2")
    api("com.sksamuel.hoplite:hoplite-yaml:2.6.2")
    api("com.sksamuel.hoplite:hoplite-watch:2.6.2")
}
