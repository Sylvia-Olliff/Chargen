package chargen.lib.database.stores.features

import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.types.FeatureType
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.stores.classes.ClassEditDataStoreProvider
import chargen.lib.database.stores.features.FeatureEditDataStore.Label
import chargen.lib.database.stores.features.FeatureEditDataStore.Intent
import chargen.lib.database.stores.features.FeatureEditDataStore.State
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.map
import com.badoo.reaktive.maybe.observeOn
import com.badoo.reaktive.scheduler.mainScheduler

class FeatureEditDataStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database,
    private val id: Long
) {

    fun provide(): FeatureEditDataStore =
        object: FeatureEditDataStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FeatureEditStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class Loaded(val item: FeatureData): Msg()
        data class NameChanged(val name: String): Msg()
        data class DescriptionChanged(val description: String): Msg()
        data class LevelGainedChanged(val level: Int): Msg()
        data class GroupNameChanged(val group: String): Msg()
        data class AddRequiredFeature(val featureId: Long): Msg()
        data class RemoveRequiredFeature(val featureId: Long): Msg()
        data class FeatureTypeChanged(val featureType: FeatureType): Msg()
        data class ValueChanged(val value: Int): Msg()
        data class StatChanged(val stat: Stats?): Msg()
        data class SourceStatChanged(val stat: Stats?): Msg()
        data class SpellSlotsChanged(val spellSlots: MutableMap<Int, MutableMap<Int, Int>>): Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            database
                .load(id)
                .map(Msg::Loaded)
                .observeOn(mainScheduler)
                .subscribeScoped(onSuccess = ::dispatch)
        }

        override fun executeIntent(intent: Intent, getState: () -> State) =
            when(intent) {
                is Intent.AddRequiredFeature -> addRequiredFeature(intent.id, getState())
                is Intent.RemoveRequiredFeature -> removeRequiredFeature(intent.id, getState())
                is Intent.SetSourceStat -> setSourceStat(intent.sourceStat, getState())
                is Intent.SetSpellSlots -> setSpellSlots(intent.spellSlots, getState())
                is Intent.SetStat -> setStat(intent.stat, getState())
                is Intent.SetValue -> setValue(intent.value, getState())
                is Intent.UpdateDescription -> updateDescription(intent.description, getState())
                is Intent.UpdateFeatureType -> updateFeatureType(intent.featureType, getState())
                is Intent.UpdateGroup -> updateGroupName(intent.group, getState())
                is Intent.UpdateLevelGained -> updateLevelGained(intent.level, getState())
                is Intent.UpdateName -> updateName(intent.name, getState())
            }

        private fun addRequiredFeature(featureId: Long, state: State) {
            dispatch(Msg.AddRequiredFeature(featureId))
            val currentRequiredFeatures = state.requiredFeatures.toMutableList()
            currentRequiredFeatures.add(featureId)

            publish(Label.Changed(
                FeatureData(
                    id,
                    name = state.name,
                    levelGained = state.levelGained,
                    description = state.description,
                    group = state.group,
                    requiredFeatures = currentRequiredFeatures,
                    featureType = state.featureType,
                    value = state.value,
                    stat = state.stat,
                    sourceStat = state.sourceStat,
                    spellSlots = state.spellSlots
                )
            ))
            database.addRequiredFeature(id, featureId).subscribeScoped()
        }

        private fun removeRequiredFeature(featureId: Long, state: State) {
            dispatch(Msg.RemoveRequiredFeature(featureId))
            publish(Label.Changed(
                FeatureData(
                    id,
                    name = state.name,
                    levelGained = state.levelGained,
                    description = state.description,
                    group = state.group,
                    requiredFeatures = state.requiredFeatures.filterNot { it == featureId }.toMutableList(),
                    featureType = state.featureType,
                    value = state.value,
                    stat = state.stat,
                    sourceStat = state.sourceStat,
                    spellSlots = state.spellSlots
                )
            ))
            database.removeRequiredFeature(id, featureId).subscribeScoped()
        }

        private fun setSourceStat(stat: Stats?, state: State) {
            dispatch(Msg.SourceStatChanged(stat))
            publish(Label.Changed(
                FeatureData(
                    id,
                    name = state.name,
                    levelGained = state.levelGained,
                    description = state.description,
                    group = state.group,
                    requiredFeatures = state.requiredFeatures,
                    featureType = state.featureType,
                    value = state.value,
                    stat = state.stat,
                    sourceStat = stat,
                    spellSlots = state.spellSlots
                )
            ))
            database.setSourceStat(id, stat).subscribeScoped()
        }

        private fun setSpellSlots(spellSlots: MutableMap<Int, MutableMap<Int, Int>>, state: State) {
            dispatch(Msg.SpellSlotsChanged(spellSlots))
            publish(Label.Changed(
                FeatureData(
                    id,
                    name = state.name,
                    levelGained = state.levelGained,
                    description = state.description,
                    group = state.group,
                    requiredFeatures = state.requiredFeatures,
                    featureType = state.featureType,
                    value = state.value,
                    stat = state.stat,
                    sourceStat = state.sourceStat,
                    spellSlots = spellSlots
                )
            ))
            database.updateSpellSlots(id, spellSlots).subscribeScoped()
        }

        private fun setStat(stat: Stats?, state: State) {
            dispatch(Msg.StatChanged(stat))
            publish(Label.Changed(
                FeatureData(
                    id,
                    name = state.name,
                    levelGained = state.levelGained,
                    description = state.description,
                    group = state.group,
                    requiredFeatures = state.requiredFeatures,
                    featureType = state.featureType,
                    value = state.value,
                    stat = stat,
                    sourceStat = state.sourceStat,
                    spellSlots = state.spellSlots
                )
            ))
            database.setStat(id, stat).subscribeScoped()
        }

        private fun setValue(value: Int, state: State) {
            dispatch(Msg.ValueChanged(value))
            publish(Label.Changed(
                FeatureData(
                    id,
                    name = state.name,
                    levelGained = state.levelGained,
                    description = state.description,
                    group = state.group,
                    requiredFeatures = state.requiredFeatures,
                    featureType = state.featureType,
                    value = value,
                    stat = state.stat,
                    sourceStat = state.sourceStat,
                    spellSlots = state.spellSlots
                )
            ))
            database.setValue(id, value).subscribeScoped()
        }

        private fun updateDescription(description: String, state: State) {
            dispatch(Msg.DescriptionChanged(description))
            publish(Label.Changed(
                FeatureData(
                    id,
                    name = state.name,
                    levelGained = state.levelGained,
                    description = description,
                    group = state.group,
                    requiredFeatures = state.requiredFeatures,
                    featureType = state.featureType,
                    value = state.value,
                    stat = state.stat,
                    sourceStat = state.sourceStat,
                    spellSlots = state.spellSlots
                )
            ))
            database.updateDescription(id, description).subscribeScoped()
        }

        private fun updateFeatureType(featureType: FeatureType, state: State) {
            dispatch(Msg.FeatureTypeChanged(featureType))
            publish(Label.Changed(
                FeatureData(
                    id,
                    name = state.name,
                    levelGained = state.levelGained,
                    description = state.description,
                    group = state.group,
                    requiredFeatures = state.requiredFeatures,
                    featureType = featureType,
                    value = state.value,
                    stat = state.stat,
                    sourceStat = state.sourceStat,
                    spellSlots = state.spellSlots
                )
            ))
            database.updateFeatureType(id, featureType).subscribeScoped()
        }

        private fun updateGroupName(groupName: String, state: State) {
            dispatch(Msg.GroupNameChanged(groupName))
            publish(Label.Changed(
                FeatureData(
                    id,
                    name = state.name,
                    levelGained = state.levelGained,
                    description = state.description,
                    group = groupName,
                    requiredFeatures = state.requiredFeatures,
                    featureType = state.featureType,
                    value = state.value,
                    stat = state.stat,
                    sourceStat = state.sourceStat,
                    spellSlots = state.spellSlots
                )
            ))
            database.updateGroup(id, groupName).subscribeScoped()
        }

        private fun updateLevelGained(level: Int, state: State) {
            dispatch(Msg.LevelGainedChanged(level))
            publish(Label.Changed(
                FeatureData(
                    id,
                    name = state.name,
                    levelGained = level,
                    description = state.description,
                    group = state.group,
                    requiredFeatures = state.requiredFeatures,
                    featureType = state.featureType,
                    value = state.value,
                    stat = state.stat,
                    sourceStat = state.sourceStat,
                    spellSlots = state.spellSlots
                )
            ))
            database.updateLevelGained(id, level).subscribeScoped()
        }

        private fun updateName(name: String, state: State) {
            dispatch(Msg.NameChanged(name))
            publish(Label.Changed(
                FeatureData(
                    id,
                    name = name,
                    levelGained = state.levelGained,
                    description = state.description,
                    group = state.group,
                    requiredFeatures = state.requiredFeatures,
                    featureType = state.featureType,
                    value = state.value,
                    stat = state.stat,
                    sourceStat = state.sourceStat,
                    spellSlots = state.spellSlots
                )
            ))
            database.updateName(id, name).subscribeScoped()
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.Loaded -> copy(
                    name = msg.item.name,
                    levelGained = msg.item.levelGained,
                    description = msg.item.description,
                    group = msg.item.group,
                    requiredFeatures = msg.item.requiredFeatures,
                    featureType = msg.item.featureType,
                    value = msg.item.value,
                    stat = msg.item.stat,
                    sourceStat = msg.item.sourceStat,
                    spellSlots = msg.item.spellSlots
                )
                is Msg.AddRequiredFeature -> copy(requiredFeatures = updateList(requiredFeatures, msg.featureId))
                is Msg.DescriptionChanged -> copy(description = msg.description)
                is Msg.FeatureTypeChanged -> copy(featureType = msg.featureType)
                is Msg.GroupNameChanged -> copy(group = msg.group)
                is Msg.LevelGainedChanged -> copy(levelGained = msg.level)
                is Msg.NameChanged -> copy(name = msg.name)
                is Msg.RemoveRequiredFeature -> copy(requiredFeatures = updateList(requiredFeatures, msg.featureId, true))
                is Msg.SourceStatChanged -> copy(sourceStat = msg.stat)
                is Msg.SpellSlotsChanged -> copy(spellSlots = msg.spellSlots)
                is Msg.StatChanged -> copy(stat = msg.stat)
                is Msg.ValueChanged -> copy(value = msg.value)
            }

        private fun <T> updateList(list: MutableList<T>, item: T, remove: Boolean = false): MutableList<T> {
            if (remove) {
                list.remove(item)
            } else {
                list.add(item)
            }
            return list
        }
    }

    interface Database {
        fun load(id: Long): Maybe<FeatureData>

        fun updateName(id: Long, name: String): Completable

        fun updateDescription(id: Long, description: String): Completable

        fun updateLevelGained(id: Long, level: Int): Completable

        fun updateGroup(id: Long, group: String): Completable

        fun addRequiredFeature(id: Long, featureId: Long): Completable

        fun removeRequiredFeature(id: Long, featureId: Long): Completable

        fun updateFeatureType(id: Long, featureType: FeatureType): Completable

        fun setValue(id: Long, value: Int): Completable

        fun setStat(id: Long, stat: Stats?): Completable

        fun setSourceStat(id: Long, stat: Stats?): Completable

        fun updateSpellSlots(id: Long, spellSlots: MutableMap<Int, MutableMap<Int, Int>>): Completable
    }
}