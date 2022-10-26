
object Deps {
    object Jetbrains {
        object Kotlin {
            private val VERSION get() = "1.6.10"
            val gradlePlugin get() = "org.jetbrains.kotlin:kotlin-gradle-plugin:$VERSION"
        }
        object Kotlinx {
            val coroutines get() = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1"
            val serializationJson get() = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0"
        }
        object Compose {
            val gradlePlugin get() = "org.jetbrains.compose:compose-gradle-plugin:1.1.0"
        }
    }

    object ArkIvanov {
        object MVIKotlin {
            private const val VERSION = "3.0.0-beta01"
            const val rx = "com.arkivanov.mvikotlin:rx:$VERSION"
            const val mvikotlin = "com.arkivanov.mvikotlin:mvikotlin:$VERSION"
            const val mvikotlinMain = "com.arkivanov.mvikotlin:mvikotlin-main:$VERSION"
            const val mvikotlinLogging = "com.arkivanov.mvikotlin:mvikotlin-logging:$VERSION"
            const val mvikotlinTimeTravel = "com.arkivanov.mvikotlin:mvikotlin-timetravel:$VERSION"
            const val mvikotlinExtensionsReaktive = "com.arkivanov.mvikotlin:mvikotlin-extensions-reaktive:$VERSION"
        }

        object Decompose {
            private const val VERSION = "1.0.0-alpha-04"
            const val decompose = "com.arkivanov.decompose:decompose:$VERSION"
            const val extensionsCompose = "com.arkivanov.decompose:extensions-compose-jetbrains:$VERSION"
        }

        object Essenty {

        }
    }

    object Sksamuel {
        object Hoplite {
            private const val VERSION = "2.6.2"
            const val core = "com.sksamuel.hoplite:hoplite-core:$VERSION"
            const val yaml = "com.sksamuel.hoplite:hoplite-yaml:$VERSION"
            const val watch = "com.sksamuel.hoplite:hoplite-watch:$VERSION"
        }
    }

    object Badoo {
        object Reaktive {
            private const val VERSION = "1.2.1"
            const val reaktive = "com.badoo.reaktive:reaktive:$VERSION"
            const val reaktiveTesting = "com.badoo.reaktive:reaktive-testing:$VERSION"
            const val utils = "com.badoo.reaktive:utils:$VERSION"
            const val coroutinesInterop = "com.badoo.reaktive:coroutines-interop:$VERSION"
        }
    }

    object Squareup {
        object SQLDelight {
            private const val VERSION = "1.5.3"
            const val gradlePlugin = "com.squareup.sqldelight:gradle-plugin:$VERSION"
            const val sqliteDriver = "com.squareup.sqldelight:sqlite-driver:$VERSION"
            const val nativeDriver = "com.squareup.sqldelight:native-driver:$VERSION"
        }
    }
}