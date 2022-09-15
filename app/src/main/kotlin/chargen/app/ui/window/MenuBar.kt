package chargen.app.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar

@Composable
fun FrameWindowScope.WindowMenuBar() = MenuBar {
    val scope = rememberCoroutineScope()

}
