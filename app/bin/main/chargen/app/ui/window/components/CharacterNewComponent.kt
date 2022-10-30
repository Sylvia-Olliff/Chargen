package chargen.app.ui.window.components

import chargen.app.ui.window.CharacterNew
import chargen.lib.character.data.dnd.CharacterData
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class CharacterNewComponent constructor(
    componentContext: ComponentContext
) : CharacterNew, ComponentContext by componentContext {

    private val _characterData = MutableValue()
}