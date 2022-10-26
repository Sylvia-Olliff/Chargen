package chargen.app.ui.window.components

import chargen.app.ui.utils.asValue
import chargen.app.ui.window.SkillMain
import chargen.app.ui.window.SkillMain.Output
import chargen.app.ui.window.integrations.skillStateToModel
import chargen.lib.database.stores.skills.SkillMainDataStore.Intent
import chargen.lib.database.stores.skills.SkillMainDataStoreDatabase
import chargen.lib.database.stores.skills.SkillMainDataStoreProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke

class SkillMainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<Output>
): SkillMain, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            SkillMainDataStoreProvider(
                storeFactory = storeFactory,
                database = SkillMainDataStoreDatabase()
            ).provide()
        }

    override val models: Value<SkillMain.Model> = store.asValue().map(skillStateToModel)

    override fun onSkillSelected(id: Long) {
        output(Output.Selected(id))
    }

    override fun onSkillAddClicked() {
        store.accept(Intent.AddSkill)
    }

    override fun onSkillDeleteClicked(id: Long) {
        store.accept(Intent.DeleteSkill(id))
    }
}