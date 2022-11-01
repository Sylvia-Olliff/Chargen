package chargen.app.ui.window.components

import chargen.app.ui.utils.asValue
import chargen.app.ui.window.CharacterEdit
import chargen.app.ui.window.CharacterEdit.Output
import chargen.app.ui.window.CharacterEdit.Model
import chargen.app.ui.window.integrations.characterEditStateToModel
import chargen.lib.character.data.dnd.Characteristics
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.types.Alignment
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.stores.characters.CharacterEditDataStore.Intent
import chargen.lib.database.stores.characters.CharacterEditDataStoreDatabase
import chargen.lib.database.stores.characters.CharacterEditDataStoreProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke

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
    override fun onCloseClicked() {
        output(Output.Finished)
    }

    override fun onLoad() {
        store.accept(Intent.LoadClasses)
        store.accept(Intent.LoadFeatures)
        store.accept(Intent.LoadRaces)
    }

    override fun onPlayerNameChanged(name: String) {
        store.accept(Intent.UpdatePlayerName(name))
    }

    override fun onCharacterNameChanged(name: String) {
        store.accept(Intent.UpdateCharacterName(name))
    }

    override fun onCampaignNameChanged(name: String) {
        store.accept(Intent.UpdateCampaignName(name))
    }

    override fun onStatChanged(stat: Stats, value: Int) {
        store.accept(Intent.UpdateStats(stat, value))
    }

    override fun onRaceSelected(race: RaceData) {
        store.accept(Intent.UpdateRaceData(race.id))
    }

    override fun onClassSelected(clazz: ClassData) {
        store.accept(Intent.UpdateClassData(clazz.id))
    }

    override fun onSkillChanged(skill: SkillData, isProficient: Boolean) {
        store.accept(Intent.UpdateSkillIsTrained(skill.id, isProficient))
    }

    override fun onAlignmentChanged(alignment: Alignment) {
        store.accept(Intent.UpdateAlignment(alignment))
    }

    override fun onBackgroundChanged(background: String) {
        store.accept(Intent.UpdateBackground(background))
    }

    override fun onBackstoryChanged(backstory: String) {
        store.accept(Intent.UpdateBackstory(backstory))
    }

    override fun onAbilityRemoved(ability: String) {
        store.accept(Intent.RemoveAbility(ability))
    }

    override fun onAbilityAdded(ability: String) {
        store.accept(Intent.AddAbility(ability))
    }

    override fun onFeatureAdded(feature: FeatureData) {
        store.accept(Intent.AddFeature(feature.id))
    }

    override fun onFeatureRemoved(feature: FeatureData) {
        store.accept(Intent.RemoveFeature(feature.id))
    }

    override fun onExpChanged(exp: Int) {
        store.accept(Intent.UpdateExperience(exp))
    }

    override fun onLevelChanged(level: Int) {
        store.accept(Intent.UpdateLevel(level))
    }

    override fun onCharacteristicsChanged(characteristics: Characteristics) {
        store.accept(Intent.UpdateCharacteristics(characteristics))
    }

    override fun onNotesChanged(notes: String) {
        store.accept(Intent.UpdateNotes(notes))
    }
}
