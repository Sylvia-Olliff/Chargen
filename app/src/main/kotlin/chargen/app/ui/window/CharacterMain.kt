package chargen.app.ui.window

import chargen.lib.character.data.dnd.CharacterData
import com.arkivanov.decompose.value.Value

interface CharacterMain {
    val models: Value<Model>

    fun onCharacterSelected(id: Long)
    fun onCharacterAddClicked()
    fun onCharacterDeleteClicked(id: Long)

    data class Model(
        val items: List<CharacterData>,
        val selected: CharacterData
    )

    sealed class Output {
        data class Selected(val id: Long): Output()
    }
}