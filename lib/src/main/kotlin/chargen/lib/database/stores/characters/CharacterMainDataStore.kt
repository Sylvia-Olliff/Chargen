package chargen.lib.database.stores.characters

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.database.stores.characters.CharacterMainDataStore.Intent
import chargen.lib.database.stores.characters.CharacterMainDataStore.State
import com.arkivanov.mvikotlin.core.store.Store

interface CharacterMainDataStore: Store<Intent, State, Nothing> {

    sealed class Intent {
        data class DeleteCharacter(val id: Long): Intent()
        object AddCharacter: Intent()
    }

    data class State(
        val items: List<CharacterData> = emptyList(),
        val selected: CharacterData = CharacterData.DEFAULT
    )
}