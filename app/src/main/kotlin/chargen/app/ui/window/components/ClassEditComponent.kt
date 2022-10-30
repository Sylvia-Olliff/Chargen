package chargen.app.ui.window.components

import chargen.app.ui.utils.asValue
import chargen.app.ui.window.ClassEdit
import chargen.app.ui.window.ClassEdit.Output
import chargen.app.ui.window.ClassEdit.Model
import chargen.app.ui.window.integrations.classEditStateToModel
import chargen.lib.character.data.dnd.classes.CasterClassData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.DiceType
import chargen.lib.database.stores.classes.ClassEditDataStore.Intent
import chargen.lib.database.stores.classes.ClassEditDataStoreDatabase
import chargen.lib.database.stores.classes.ClassEditDataStoreProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke

class ClassEditComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val itemId: Long,
    private val output: Consumer<Output>
): ClassEdit, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            ClassEditDataStoreProvider(
                storeFactory = storeFactory,
                database = ClassEditDataStoreDatabase(),
                id = itemId
            ).provide()
        }

    override val models: Value<Model> = store.asValue().map(classEditStateToModel)

    override fun onCloseClicked() {
        output(Output.Finished)
    }

    override fun onIsCasterChange(value: Boolean) {
        store.accept(Intent.UpdateCasterFlag(value))
        if (value && models.value.casterData == null) {
            onSetCasterData(CasterClassData())
        }
    }

    override fun onAddFeatureClicked() {
        TODO("Not yet implemented")
    }

    override fun onRemoveFeature(featureId: Long) {
        store.accept(Intent.RemoveFeature(featureId))
    }

    override fun onFeatureClicked(id: Long) {
        TODO("Not yet implemented")
    }

    override fun onAddProficiencyClicked() {
        TODO("Not yet implemented")
    }

    override fun onRemoveProficiency(proficiency: Proficiency) {
        store.accept(Intent.RemoveProficiency(proficiency))
    }

    override fun onResourcesChanged(resource: Int, resourceName: String) {
        store.accept(Intent.SetResources(resourceName, resource))
    }

    override fun onClassNameChanged(name: String) {
        store.accept(Intent.UpdateName(name))
    }

    override fun onSetCasterData(casterData: CasterClassData) {
        store.accept(Intent.SetCasterData(casterData))
    }

    override fun onHitDieChange(hitDie: DiceType) {
        store.accept(Intent.SetHitDie(hitDie))
    }

    override fun onNumAttacksChange(numAttacks: Int) {
        store.accept(Intent.SetNumAttacks(numAttacks))
    }

}