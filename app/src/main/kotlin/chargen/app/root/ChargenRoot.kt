package chargen.app.root

import chargen.app.ui.window.CharacterEdit
import chargen.app.ui.window.CharacterNew
import chargen.app.ui.window.DataEdit
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface ChargenRoot {
    val childStack: Value<ChildStack<*, Child>>

    fun onNewCharacterClicked()
    fun onEditCharacterClicked()
    fun onEditDataClicked()

    sealed class Child {
        class CharacterEditChild(val component: CharacterEdit): Child()
        class DataEditChild(val component: DataEdit): Child()
        class CharacterNewChild(val component: CharacterNew): Child()
    }
}
