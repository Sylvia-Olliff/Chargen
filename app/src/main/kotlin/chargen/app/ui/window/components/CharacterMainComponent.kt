package chargen.app.ui.window.components

import chargen.app.ui.utils.asValue
import chargen.app.ui.utils.getStore
import chargen.app.ui.window.CharacterMain
import chargen.app.ui.window.CharacterMain.Output
import chargen.app.ui.window.ClassMain
import chargen.app.ui.window.integrations.characterMainStateToModel
import chargen.lib.database.stores.characters.CharacterMainDataStore.Intent
import chargen.lib.database.stores.characters.CharacterMainDataStoreDatabase
import chargen.lib.database.stores.characters.CharacterMainDataStoreProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke

class CharacterMainComponent constructor(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<Output>
): CharacterMain, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            CharacterMainDataStoreProvider(
                storeFactory = storeFactory,
                database = CharacterMainDataStoreDatabase()
            ).provide()
        }

    override val models: Value<CharacterMain.Model> = this.store.asValue().map(characterMainStateToModel)

    override fun onCharacterSelected(id: Long) {
        output(Output.Selected(id))
    }

    override fun onCharacterAddClicked() {
        store.accept(Intent.AddCharacter)
    }

    override fun onCharacterDeleteClicked(id: Long) {
        store.accept(Intent.DeleteCharacter(id))
    }
}