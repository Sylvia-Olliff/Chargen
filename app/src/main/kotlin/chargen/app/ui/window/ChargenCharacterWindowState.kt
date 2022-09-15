package chargen.app.ui.window

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import chargen.app.ui.common.Settings
import chargen.app.ui.common.WindowTypes
import java.nio.file.Path

class ChargenCharacterWindowState(
    application: ChargenApplicationState,
    path: Path?,
    exit: (T: ChargenWindowState) -> Unit
): ChargenWindowState(application, null, exit) {
    val settings: Settings get() = application.settings

    var path by mutableStateOf(path)
        private set

    fun newWindow() {
        application.newWindow(WindowTypes.CHARACTER)
    }

    //TODO: Implement character save functions

    override suspend fun exit(): Boolean {
        // TODO: Implement save changes check
        return true
    }
}
