import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("chargen.kotlin-common-conventions")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(Deps.ArkIvanov.Decompose.decompose)
    implementation(Deps.ArkIvanov.Decompose.extensionsCompose)
    implementation(compose.desktop.common)
    implementation(compose.desktop.currentOs)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material)
}

compose.desktop {
    application {
        mainClass = "chargen.app.AppKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi)
            packageName = "ChargenDND5e"
            packageVersion = "1.1.28"

            modules("java.sql")

            windows {
                menuGroup = "SylvanTitan Character Generators"
                // see https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                upgradeUuid = "DA634CE1-C0F0-4167-993F-6BB810614CD7"
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
