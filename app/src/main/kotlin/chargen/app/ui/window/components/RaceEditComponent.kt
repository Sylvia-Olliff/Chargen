package chargen.app.ui.window.components

import chargen.app.ui.utils.asValue
import chargen.app.ui.window.RaceEdit
import chargen.app.ui.window.RaceEdit.Output
import chargen.app.ui.window.integrations.raceEditStateToModel
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.stores.races.RaceEditDataStore.Intent
import chargen.lib.database.stores.races.RaceEditDataStoreDatabase
import chargen.lib.database.stores.races.RaceEditDataStoreProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke

class RaceEditComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val itemId: Long,
    private val output: Consumer<Output>
): RaceEdit, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            RaceEditDataStoreProvider(
                storeFactory = storeFactory,
                database = RaceEditDataStoreDatabase(),
                id = itemId
            ).provide()
        }

    override val models: Value<RaceEdit.Model> = store.asValue().map(raceEditStateToModel)

    override fun onClosedClicked() {
        output(Output.Finished)
    }

    override fun onNameChanged(name: String) {
        store.accept(Intent.UpdateName(name))
    }

    override fun onNamePluralChanged(namePlural: String) {
        store.accept(Intent.UpdateNamePlural(namePlural))
    }

    override fun onDescriptionChanged(description: String) {
        store.accept(Intent.UpdateDescription(description))
    }

    override fun onAddProficiencyClicked(proficiency: Proficiency) {
        store.accept(Intent.AddProficiency(proficiency))
    }

    override fun onRemoveProficiencyClicked(proficiency: Proficiency) {
        store.accept(Intent.RemoveProficiency(proficiency))
    }

    override fun onAddFeatureClicked(feature: FeatureData) {
        store.accept(Intent.AddFeature(feature.id))
    }

    override fun onRemoveFeatureClicked(feature: FeatureData) {
        store.accept(Intent.RemoveFeature(feature.id))
    }

    override fun onStatModsChanged(statMods: MutableMap<Stats, Int>) {
        store.accept(Intent.UpdateStatMods(statMods))
    }
}