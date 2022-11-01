
plugins {
    id("chargen.kotlin-common-conventions")
    id("com.squareup.sqldelight")
    `java-library`
}


dependencies {
    api(Deps.Sksamuel.Hoplite.core)
    api(Deps.Sksamuel.Hoplite.yaml)
    api(Deps.Sksamuel.Hoplite.watch)

    implementation(Deps.Squareup.SQLDelight.sqliteDriver)
}

sqldelight {
    database("ChargenDatabase") {
        packageName = "chargen.lib.database"
        sourceFolders = listOf("sqldelight", "resources")
    }
}
