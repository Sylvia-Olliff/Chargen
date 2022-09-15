package chargen.app.ui.window

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import chargen.lib.character.data.dnd.templates.DataEntity

open class ChargenWindowState(
    protected val application: ChargenApplicationState,
    protected val data: DataEntity?,
    protected val exit: (T: ChargenWindowState) -> Unit
) {
    val window = WindowState()

    var isChanged by mutableStateOf(false)
        private set

    fun toggleFullscreen() {
        window.placement = if (window.placement == WindowPlacement.Fullscreen) {
            WindowPlacement.Floating
        } else {
            WindowPlacement.Fullscreen
        }
    }

    //TODO: Implement notification tooling

    open suspend fun exit(): Boolean {
        this.exit.invoke(this)
        return true
    }
}
