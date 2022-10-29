package chargen.app.ui.window.integrations

import chargen.app.ui.window.CharacterMain
import chargen.lib.database.stores.characters.CharacterMainDataStore

val characterMainStateToModel: (CharacterMainDataStore.State) -> CharacterMain.Model = {
    CharacterMain.Model(
        items = it.items,
        selected = it.selected
    )
}