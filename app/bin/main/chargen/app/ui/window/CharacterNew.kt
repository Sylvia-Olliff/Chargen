package chargen.app.ui.window

import chargen.lib.character.data.dnd.CharacterData
import com.arkivanov.decompose.value.Value

interface CharacterNew {
    val characterData: Value<CharacterData>

    fun onClassSelect()
    fun onRaceSelect()
    fun onSkillsSelect()
}