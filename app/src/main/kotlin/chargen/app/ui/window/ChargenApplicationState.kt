package chargen.app.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.TrayState
import chargen.app.ui.common.Settings
import chargen.app.ui.common.WindowTypes
import chargen.app.ui.window.ChargenCharacterWindowState

@Composable
fun rememberApplicationState() = remember {
    ChargenApplicationState().apply {
        newWindow(WindowTypes.MAIN_MENU)
    }
}

class ChargenApplicationState {
    val settings = Settings()
    val tray = TrayState()

    private val _windows = mutableStateListOf<ChargenWindowState>()
    val windows: List<ChargenWindowState> get() = _windows

    fun newWindow(type: WindowTypes) {
        when (type) {
            WindowTypes.MAIN_MENU -> TODO()
            WindowTypes.CHARACTER -> _windows.add(
                ChargenCharacterWindowState(
                    application = this,
                    path = null,
                    exit = _windows::remove
                )
            )
            WindowTypes.SKILL -> TODO()
            WindowTypes.FEATURE -> TODO()
            WindowTypes.RACE -> TODO()
            WindowTypes.CONFIGURE -> TODO()
        }
    }

    fun sendNotification(notification: Notification) {
        tray.sendNotification(notification)
    }

    suspend fun exit() {
        val windowsCopy = windows.reversed()
        for (window in windowsCopy) {
            if (!window.exit()) {
                break
            }
        }
    }
}
