package chargen.app.ui.window.components

import chargen.app.ui.utils.asValue
import chargen.app.ui.window.CharacterEdit
import chargen.app.ui.window.CharacterEdit.Output
import chargen.app.ui.window.CharacterEdit.Model
import chargen.app.ui.window.integrations.characterEditStateToModel
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.database.stores.characters.CharacterEditDataStoreDatabase
import chargen.lib.database.stores.characters.CharacterEditDataStoreProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer

class CharacterEditComponent constructor(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val itemId: Long,
    private val output: Consumer<Output>
): CharacterEdit, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            CharacterEditDataStoreProvider(
                storeFactory = storeFactory,
                database = CharacterEditDataStoreDatabase(),
                id = itemId
            ).provide()
        }

    override val models: Value<Model> = store.asValue().map(characterEditStateToModel)
    override val classes: List<ClassData> =
}
