package chargen.app.ui.window.components

import chargen.app.ui.utils.asValue
import chargen.app.ui.utils.getStore
import chargen.app.ui.window.FeatureEdit
import chargen.app.ui.window.FeatureEdit.Model
import chargen.app.ui.window.FeatureEdit.Output
import chargen.app.ui.window.integrations.featureEditStateToModel
import chargen.lib.character.data.dnd.types.FeatureType
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.stores.features.FeatureEditDataStoreDatabase
import chargen.lib.database.stores.features.FeatureEditDataStoreProvider
import chargen.lib.database.stores.features.FeatureEditDataStore.Intent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke

class FeatureEditComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val itemId: Long,
    private val output: Consumer<Output>
): FeatureEdit, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            FeatureEditDataStoreProvider(
                storeFactory = storeFactory,
                database = FeatureEditDataStoreDatabase(),
                id = itemId
            ).provide()
        }

    override val models: Value<Model> = store.asValue().map(featureEditStateToModel)

    override fun onCloseClicked() {
        output(Output.Finished)
    }

    override fun onAddRequiredFeatureClicked() {
        TODO("Not yet implemented")
    }

    override fun onRemoveRequiredFeatureClicked(featureId: Long) {
        store.accept(Intent.RemoveRequiredFeature(featureId))
    }

    override fun onRequiredFeatureClicked(featureId: Long) {
        TODO("Not yet implemented")
    }

    override fun onNameChanged(name: String) {
        store.accept(Intent.UpdateName(name))
    }

    override fun onDescriptionChanged(description: String) {
        store.accept(Intent.UpdateDescription(description))
    }

    override fun onLevelGainedChanged(levelGained: Int) {
        store.accept(Intent.UpdateLevelGained(levelGained))
    }

    override fun onGroupNameChanged(group: String) {
        store.accept(Intent.UpdateGroup(group))
    }

    override fun onFeatureTypeChanged(featureType: FeatureType) {
        store.accept(Intent.UpdateFeatureType(featureType))
    }

    override fun onValueChanged(value: Int) {
        store.accept(Intent.SetValue(value))
    }

    override fun onStatChanged(stat: Stats) {
        if (stat != Stats.NONE) {
            store.accept(Intent.SetStat(stat))
        } else {
            store.accept(Intent.SetStat(null))
        }
    }

    override fun onSourceStatChanged(stat: Stats) {
        if (stat != Stats.NONE) {
            store.accept(Intent.SetSourceStat(stat))
        } else {
            store.accept(Intent.SetSourceStat(null))
        }
    }

    override fun onSpellSlotsChanged(spellSlots: MutableMap<Int, MutableMap<Int, Int>>) {
        store.accept(Intent.SetSpellSlots(spellSlots))
    }
}