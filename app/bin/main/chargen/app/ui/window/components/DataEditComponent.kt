package chargen.app.ui.window.components

import chargen.app.ui.window.DataEdit
import com.arkivanov.decompose.ComponentContext

class DataEditComponent constructor(
    componentContext: ComponentContext
): DataEdit, ComponentContext by componentContext {
}
