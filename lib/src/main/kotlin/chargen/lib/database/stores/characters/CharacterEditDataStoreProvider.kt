package chargen.lib.database.stores.characters

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.Characteristics
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.types.Alignment
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.stores.characters.CharacterEditDataStore.Intent
import chargen.lib.database.stores.characters.CharacterEditDataStore.State
import chargen.lib.database.stores.characters.CharacterEditDataStore.Label
import chargen.lib.database.stores.classes.ClassEditDataStoreProvider
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.blockingGet
import com.badoo.reaktive.maybe.map
import com.badoo.reaktive.maybe.observeOn
import com.badoo.reaktive.scheduler.mainScheduler

class CharacterEditDataStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database,
    private val id: Long
) {

    fun provide(): CharacterEditDataStore =
        object : CharacterEditDataStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CharacterEditStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class Loaded(val item: CharacterData): Msg()
        data class PlayerNameChanged(val name: String): Msg()
        data class CharacterNameChanged(val name: String): Msg()
        data class CampaignNameChanged(val name: String): Msg()
        data class StatsChanged(val stat: Stats, val value: Int): Msg()
        data class RaceSelected(val raceId: Long): Msg()
        data class ClassSelected(val classId: Long): Msg()
        data class SkillChanged(val skillId: Long, val isTrained: Boolean): Msg()
        data class SkillRemoved(val skillId: Long): Msg()
        data class AlignmentChanged(val alignment: Alignment): Msg()
        data class BackgroundChanged(val background: String): Msg()
        data class AbilityAdded(val ability: String): Msg()
        data class AbilityRemoved(val ability: String): Msg()
        data class FeaturesChanged(val featureIds: List<FeatureData>): Msg()
        data class EXPChanged(val experience: Int): Msg()
        data class LevelChanged(val level: Int): Msg()
        data class CharacteristicsChanged(val characteristics: Characteristics): Msg()
        data class BackstoryChanged(val backstory: String): Msg()
        data class NotesChanged(val notes: String): Msg()
    }

    private inner class ExecutorImpl: ReaktiveExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            database
                .load(id)
                .map(Msg::Loaded)
                .observeOn(mainScheduler)
                .subscribeScoped(onSuccess = ::dispatch)
        }

        override fun executeIntent(intent: Intent, getState: () -> State) =
            when (intent) {
                is Intent.AddAbility -> addAbility(intent.ability, getState())
                is Intent.AddFeature -> addFeature(intent.feature, getState())
                is Intent.RemoveAbility -> removeAbility(intent.ability, getState())
                is Intent.RemoveFeature -> removeFeature(intent.feature.id, getState())
                is Intent.UpdateAlignment -> TODO()
                is Intent.UpdateBackground -> TODO()
                is Intent.UpdateBackstory -> TODO()
                is Intent.UpdateCampaignName -> TODO()
                is Intent.UpdateCharacterName -> TODO()
                is Intent.UpdateCharacteristics -> TODO()
                is Intent.UpdateClassData -> TODO()
                is Intent.UpdateExperience -> TODO()
                is Intent.UpdateLevel -> TODO()
                is Intent.UpdateNotes -> TODO()
                is Intent.UpdatePlayerName -> TODO()
                is Intent.UpdateRaceData -> TODO()
                is Intent.UpdateSkills -> TODO()
                is Intent.UpdateStats -> TODO()
            }

        private fun addAbility(ability: String, state: State) {
            dispatch(Msg.AbilityAdded(ability))
            state.abilities.add(ability)
            publish(Label.Changed(
                CharacterData(
                    id = id,
                    name = state.characterName,
                    playerName = state.playerName,
                    campaignName = state.campaignName,
                    stats = state.stats,
                    raceData = state.raceData,
                    classData = state.classData,
                    skills = state.skills,
                    alignment = state.alignment,
                    background = state.background,
                    abilities = state.abilities,
                    currentFeatures = state.currentFeatures,
                    EXP = state.experience,
                    level = state.level,
                    characteristics = state.characteristics,
                    backstory = state.backstory,
                    notes = state.notes
                )
            ))
            database.addAbility(id, ability).subscribeScoped()
        }

        private fun addFeature(feature: FeatureData, state: State) {
            state.currentFeatures.add(feature)
            dispatch(Msg.FeaturesChanged(state.currentFeatures))
            publish(Label.Changed(
                CharacterData(
                    id = id,
                    name = state.characterName,
                    playerName = state.playerName,
                    campaignName = state.campaignName,
                    stats = state.stats,
                    raceData = state.raceData,
                    classData = state.classData,
                    skills = state.skills,
                    alignment = state.alignment,
                    background = state.background,
                    abilities = state.abilities,
                    currentFeatures = state.currentFeatures,
                    EXP = state.experience,
                    level = state.level,
                    characteristics = state.characteristics,
                    backstory = state.backstory,
                    notes = state.notes
                )
            ))
            database.addFeature(id, feature.id).subscribeScoped()
        }

        private fun removeAbility(ability: String, state: State) {
            dispatch(Msg.AbilityRemoved(ability))
            state.abilities.remove(ability)
            publish(Label.Changed(
                CharacterData(
                    id = id,
                    name = state.characterName,
                    playerName = state.playerName,
                    campaignName = state.campaignName,
                    stats = state.stats,
                    raceData = state.raceData,
                    classData = state.classData,
                    skills = state.skills,
                    alignment = state.alignment,
                    background = state.background,
                    abilities = state.abilities,
                    currentFeatures = state.currentFeatures,
                    EXP = state.experience,
                    level = state.level,
                    characteristics = state.characteristics,
                    backstory = state.backstory,
                    notes = state.notes
                )
            ))
        }

        private fun removeFeature(feature: FeatureData, state: State) {
            state.currentFeatures.remove(feature)
            dispatch(Msg.FeaturesChanged(state.currentFeatures))
            publish(Label.Changed(
                CharacterData(
                    id = id,
                    name = state.characterName,
                    playerName = state.playerName,
                    campaignName = state.campaignName,
                    stats = state.stats,
                    raceData = state.raceData,
                    classData = state.classData,
                    skills = state.skills,
                    alignment = state.alignment,
                    background = state.background,
                    abilities = state.abilities,
                    currentFeatures = state.currentFeatures,
                    EXP = state.experience,
                    level = state.level,
                    characteristics = state.characteristics,
                    backstory = state.backstory,
                    notes = state.notes
                )
            ))
            database.removeFeature(id, feature.id)
        }
    }

    interface Database {
        fun load(id: Long): Maybe<CharacterData>

        fun loadFeature(featureId: Long): Maybe<FeatureData>

        fun loadRace(raceId: Long): Maybe<RaceData>

        fun loadClass(classId: Long): Maybe<ClassData>

        fun loadSkill(skillId: Long): Maybe<SkillData>

        fun setPlayerName(id: Long, name: String): Completable
        fun setCharacterName(id: Long, name: String): Completable
        fun setCampaignName(id: Long, name: String): Completable
        fun setStat(id: Long, stat: Stats, value: Int): Completable
        fun setRace(id: Long, raceData: RaceData): Completable
        fun setClass(id: Long, classData: ClassData): Completable
        fun updateSkillIsTrained(id: Long, skillID: Long, isTrained: Boolean): Completable
        fun removeSkill(id: Long, skillId: Long): Completable
        fun setAlignment(id: Long, alignment: Alignment): Completable
        fun setBackground(id: Long, background: String): Completable
        fun addAbility(id: Long, ability: String): Completable
        fun removeAbility(id: Long, ability: String): Completable
        fun addFeature(id: Long, featureId: Long): Completable
        fun removeFeature(id: Long, featureId: Long): Completable
        fun setExp(id: Long, exp: Int): Completable
        fun setLevel(id: Long, level: Int): Completable
        fun setCharacteristics(id: Long, characteristics: Characteristics): Completable
        fun setBackstory(id: Long, backstory: String): Completable
        fun setNotes(id: Long, notes: String): Completable
    }
}