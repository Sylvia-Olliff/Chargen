package chargen.app

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import chargen.app.root.ChargenRoot
import chargen.app.root.ChargenRootComponent
import chargen.app.root.ChargenRootContent
import chargen.lib.API
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.coroutinesinterop.asScheduler
import com.badoo.reaktive.scheduler.overrideSchedulers
import kotlinx.coroutines.Dispatchers

fun main() {
//    overrideSchedulers(main = Dispatchers.Main::asScheduler)
    API.loadData()

    val lifecycle = LifecycleRegistry()
    val root = chargenRoot(DefaultComponentContext(lifecycle = lifecycle))

    application {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Chargen"
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                MaterialTheme {
                    CompositionLocalProvider(
                        LocalScrollbarStyle provides ScrollbarStyle(
                            minimalHeight = 16.dp,
                            thickness = 8.dp,
                            shape = MaterialTheme.shapes.small,
                            hoverDurationMillis = 300,
                            unhoverColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                            hoverColor = MaterialTheme.colors.onSurface.copy(alpha = 0.50f)
                        )
                    ) {
                        ChargenRootContent(root)
                    }
                }
            }
        }
    }
}

private fun chargenRoot(componentContext: ComponentContext): ChargenRoot =
    ChargenRootComponent(
        componentContext = componentContext,
        storeFactory = DefaultStoreFactory()
    )