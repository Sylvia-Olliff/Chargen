package chargen.lib.database.stores.characters

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.Characteristics
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.types.Alignment
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.character.data.dnd.utils.Utils
import chargen.lib.database.stores.characters.CharacterEditDataStore.Intent
import chargen.lib.database.stores.characters.CharacterEditDataStore.State
import chargen.lib.database.stores.characters.CharacterEditDataStore.Label
import com.arkivanov.mvikotlin.core.store.Reducer
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
        data class RaceSelected(val race: RaceData): Msg()
        data class ClassSelected(val clazz: ClassData): Msg()
        data class SkillChanged(val skill: SkillData, val isTrained: Boolean): Msg()
        data class SkillRemoved(val skill: SkillData): Msg()
        data class AlignmentChanged(val alignment: Alignment): Msg()
        data class BackgroundChanged(val background: String): Msg()
        data class AbilityAdded(val ability: String): Msg()
        data class AbilityRemoved(val ability: String): Msg()
        data class FeatureAdded(val feature: FeatureData): Msg()
        data class FeatureRemoved(val feature: FeatureData): Msg()
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
                is Intent.AddFeature -> addFeature(intent.featureId, getState())
                is Intent.RemoveAbility -> removeAbility(intent.ability, getState())
                is Intent.RemoveFeature -> removeFeature(intent.featureId, getState())
                is Intent.UpdateAlignment -> updateAlignment(intent.alignment, getState())
                is Intent.UpdateBackground -> updateBackground(intent.background, getState())
                is Intent.UpdateBackstory -> updateBackstory(intent.backstory, getState())
                is Intent.UpdateCampaignName -> updateCampaignName(intent.name, getState())
                is Intent.UpdateCharacterName -> updateCharacterName(intent.name, getState())
                is Intent.UpdateCharacteristics -> updateCharacteristics(intent.characteristics, getState())
                is Intent.UpdateClassData -> updateClassData(intent.classId, getState())
                is Intent.UpdateExperience -> updateExperience(intent.exp, getState())
                is Intent.UpdateLevel -> updateLevel(intent.level, getState())
                is Intent.UpdateNotes -> updateNotes(intent.notes, getState())
                is Intent.UpdatePlayerName -> updatePlayerName(intent.name, getState())
                is Intent.UpdateRaceData -> updateRaceData(intent.raceId, getState())
                is Intent.UpdateSkillIsTrained -> updateSkillIsTrained(intent.skillId, intent.value, getState())
                is Intent.RemoveSkill -> removeSkill(intent.skillId, getState())
                is Intent.UpdateStats -> updateStats(intent.stat, intent.value, getState())
            }

        private fun addAbility(ability: String, state: State) {
            dispatch(Msg.AbilityAdded(ability))
            state.abilities.add(ability)
            publishCharacter(state)
            database.addAbility(id, ability).subscribeScoped()
        }

        private fun addFeature(featureId: Long, state: State) {
            val feature = database.loadFeature(featureId).blockingGet()!!
            dispatch(Msg.FeatureAdded(feature))
            state.currentFeatures.add(feature)
            publishCharacter(state)
            database.addFeature(id, featureId).subscribeScoped()
        }

        private fun removeAbility(ability: String, state: State) {
            dispatch(Msg.AbilityRemoved(ability))
            state.abilities.remove(ability)
            publishCharacter(state)
            database.removeAbility(id, ability).subscribeScoped()
        }

        private fun removeFeature(featureId: Long, state: State) {
            state.currentFeatures = state.currentFeatures.filterNot { it.id == featureId }.toMutableList()
            dispatch(Msg.FeatureRemoved(database.loadFeature(featureId).blockingGet()!!))
            publishCharacter(state)
            database.removeFeature(id, featureId).subscribeScoped()
        }

        private fun updateAlignment(alignment: Alignment, state: State) {
            dispatch(Msg.AlignmentChanged(alignment))
            state.alignment = alignment
            publishCharacter(state)
            database.setAlignment(id, alignment).subscribeScoped()
        }

        private fun updateBackground(background: String, state: State) {
            dispatch(Msg.BackgroundChanged(background))
            state.background = background
            publishCharacter(state)
            database.setBackground(id, background).subscribeScoped()
        }

        private fun updateBackstory(backstory: String, state: State) {
            dispatch(Msg.BackstoryChanged(backstory))
            state.backstory = backstory
            publishCharacter(state)
            database.setBackstory(id, backstory).subscribeScoped()
        }

        private fun updateCampaignName(name: String, state: State) {
            dispatch(Msg.CampaignNameChanged(name))
            state.campaignName = name
            publishCharacter(state)
            database.setCampaignName(id, name).subscribeScoped()
        }

        private fun updateCharacterName(name: String, state: State) {
            dispatch(Msg.CharacterNameChanged(name))
            state.characterName = name
            publishCharacter(state)
            database.setCharacterName(id, name).subscribeScoped()
        }

        private fun updateCharacteristics(characteristics: Characteristics, state: State) {
            dispatch(Msg.CharacteristicsChanged(characteristics))
            state.characteristics = characteristics
            publishCharacter(state)
            database.setCharacteristics(id, characteristics).subscribeScoped()
        }

        private fun updateClassData(classId: Long, state: State) {
            val clazz = database.loadClass(classId).blockingGet()!!
            dispatch(Msg.ClassSelected(clazz))
            state.classData = clazz
            publishCharacter(state)
            database.setClass(id, classId).subscribeScoped()
        }

        private fun updateExperience(exp: Int, state: State) {
            dispatch(Msg.EXPChanged(exp))
            state.experience = exp
            val level = Utils.getLevelForEXP(exp)
            dispatch(Msg.LevelChanged(level))
            state.level = level
            publishCharacter(state)
            database.setExp(id, exp).subscribeScoped()
            database.setLevel(id, level).subscribeScoped()
        }

        private fun updateLevel(level: Int, state: State) {
            dispatch(Msg.LevelChanged(level))
            state.level = level
            val exp = Utils.getEXPForLevel(level)
            dispatch(Msg.EXPChanged(exp))
            state.experience = exp
            publishCharacter(state)
            database.setLevel(id, level).subscribeScoped()
            database.setExp(id, exp).subscribeScoped()
        }

        private fun updateNotes(notes: String, state: State) {
            dispatch(Msg.NotesChanged(notes))
            state.notes = notes
            publishCharacter(state)
            database.setNotes(id, notes).subscribeScoped()
        }

        private fun updatePlayerName(name: String, state: State) {
            dispatch(Msg.PlayerNameChanged(name))
            state.playerName = name
            publishCharacter(state)
            database.setPlayerName(id, name).subscribeScoped()
        }

        private fun updateRaceData(raceId: Long, state: State) {
            val race = database.loadRace(raceId).blockingGet()!!
            dispatch(Msg.RaceSelected(race))
            state.raceData = race
            publishCharacter(state)
            database.setRace(id, raceId).subscribeScoped()
        }

        private fun updateSkillIsTrained(skillId: Long, value: Boolean, state: State) {
            val skillData = state.skills.keys.find { it.id == skillId }!!
            dispatch(Msg.SkillChanged(skillData, value))
            state.skills[skillData] = value
            publishCharacter(state)
            database.updateSkillIsTrained(id, skillId, value).subscribeScoped()
        }

        private fun removeSkill(skillId: Long, state: State) {
            val skillData = state.skills.keys.find { it.id == skillId }!!
            dispatch(Msg.SkillRemoved(skillData))
            state.skills.remove(skillData)
            publishCharacter(state)
            database.removeSkill(id, skillId).subscribeScoped()
        }

        private fun updateStats(stat: Stats, value: Int, state: State) {
            dispatch(Msg.StatsChanged(stat, value))
            state.stats[stat] = value
            publishCharacter(state)
            database.setStat(id, stat, value).subscribeScoped()
        }

        private fun publishCharacter(state: State) {
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
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.Loaded -> copy(
                    characterName = msg.item.name,
                    playerName = msg.item.playerName ?: playerName,
                    campaignName = msg.item.campaignName ?: campaignName,
                    stats = msg.item.stats,
                    raceData = msg.item.raceData,
                    classData = msg.item.classData,
                    skills = msg.item.skills,
                    alignment = msg.item.alignment ?: alignment,
                    background = msg.item.background ?: background,
                    backstory = msg.item.backstory ?: backstory,
                    abilities = msg.item.abilities,
                    currentFeatures = msg.item.currentFeatures,
                    experience = msg.item.EXP,
                    level = msg.item.level,
                    characteristics = msg.item.characteristics,
                    notes = msg.item.notes ?: notes
                )
                is Msg.AbilityAdded -> copy(abilities = updateList(abilities, msg.ability))
                is Msg.AbilityRemoved -> copy(abilities = updateList(abilities, msg.ability, true))
                is Msg.AlignmentChanged -> copy(alignment = msg.alignment)
                is Msg.BackgroundChanged -> copy(background = msg.background)
                is Msg.BackstoryChanged -> copy(backstory = msg.backstory)
                is Msg.CampaignNameChanged -> copy(campaignName = msg.name)
                is Msg.CharacterNameChanged -> copy(characterName = msg.name)
                is Msg.CharacteristicsChanged -> copy(characteristics = msg.characteristics)
                is Msg.ClassSelected -> copy(classData = msg.clazz)
                is Msg.EXPChanged -> copy(experience = msg.experience)
                is Msg.FeatureAdded -> copy(currentFeatures = updateList(currentFeatures, msg.feature))
                is Msg.FeatureRemoved -> copy(currentFeatures = updateList(currentFeatures, msg.feature, true))
                is Msg.LevelChanged -> copy(level = msg.level)
                is Msg.NotesChanged -> copy(notes = msg.notes)
                is Msg.PlayerNameChanged -> copy(playerName = msg.name)
                is Msg.RaceSelected -> copy(raceData = msg.race)
                is Msg.SkillChanged -> copy(skills = updateMap(skills, msg.skill, msg.isTrained))
                is Msg.SkillRemoved -> copy(skills = updateMap(skills, msg.skill, false, true))
                is Msg.StatsChanged -> copy(stats = updateMap(stats, msg.stat, msg.value))
            }

        private fun <T> updateList(list: MutableList<T>, item: T, remove: Boolean = false): MutableList<T> {
            if (remove) {
                list.remove(item)
            } else {
                list.add(item)
            }
            return list
        }

        private fun <K, V> updateMap(map: MutableMap<K, V>, item: K, value: V, remove: Boolean = false): MutableMap<K, V> {
            if (remove) {
                map.remove(item)
            } else {
                map[item] = value
            }
            return map
        }
    }

    interface Database {
        fun load(id: Long): Maybe<CharacterData>

        fun loadFeature(featureId: Long): Maybe<FeatureData>

        fun loadRace(raceId: Long): Maybe<RaceData>

        fun loadClass(classId: Long): Maybe<ClassData>

        fun setPlayerName(id: Long, name: String): Completable
        fun setCharacterName(id: Long, name: String): Completable
        fun setCampaignName(id: Long, name: String): Completable
        fun setStat(id: Long, stat: Stats, value: Int): Completable
        fun setRace(id: Long, raceId: Long): Completable
        fun setClass(id: Long, classId: Long): Completable
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