package chargen.app.ui.window.components

import chargen.app.ui.utils.asValue
import chargen.app.ui.utils.getStore
import chargen.app.ui.window.SkillEdit
import chargen.app.ui.window.SkillEdit.Output
import chargen.app.ui.window.integrations.skillEditStateToModel
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.stores.skills.SkillEditDataStore.Intent
import chargen.lib.database.stores.skills.SkillEditDataStoreDatabase
import chargen.lib.database.stores.skills.SkillEditDataStoreProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke

class SkillEditComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val itemId: Long,
    private val output: Consumer<Output>
): SkillEdit, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            SkillEditDataStoreProvider(
                storeFactory = storeFactory,
                database = SkillEditDataStoreDatabase(),
                id = itemId
            ).provide()
        }

    override val models: Value<SkillEdit.Model> = store.asValue().map(skillEditStateToModel)

    override fun onClosedClicked() {
        output(Output.Finished)
    }

    override fun onNameChanged(name: String) {
        store.accept(Intent.UpdateName(name))
    }

    override fun onDescriptionChanged(description: String) {
        store.accept(Intent.UpdateDescription(description))
    }

    override fun onStatChanged(stat: Stats) {
        store.accept(Intent.UpdateStat(stat))
    }

    override fun onUntrainedChanged(untrained: Boolean) {
        store.accept(Intent.UpdateUntrained(untrained))
    }
}