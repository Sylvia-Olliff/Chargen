package chargen.lib.database.stores.races

import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.stores.features.FeatureEditDataStoreProvider
import chargen.lib.database.stores.races.RaceEditDataStore.Intent
import chargen.lib.database.stores.races.RaceEditDataStore.State
import chargen.lib.database.stores.races.RaceEditDataStore.Label
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

class RaceEditDataStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database,
    private val id: Long
) {

    fun provide(): RaceEditDataStore =
        object: RaceEditDataStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RaceEditStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class Loaded(val item: RaceData): Msg()
        data class NameChanged(val name: String): Msg()
        data class NamePluralChanged(val namePlural: String): Msg()
        data class DescriptionChanged(val description: String): Msg()
        data class StatModsChanged(val statMods: MutableMap<Stats, Int>): Msg()
        data class AddProficiency(val proficiency: Proficiency): Msg()
        data class RemoveProficiency(val proficiency: Proficiency): Msg()
        data class AddFeature(val featureId: Long): Msg()
        data class RemoveFeature(val featureId: Long): Msg()
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
            when(intent) {
                is Intent.AddFeature -> addFeature(intent.featureId, getState())
                is Intent.AddProficiency -> addProficiency(intent.proficiency, getState())
                is Intent.RemoveFeature -> removeFeature(intent.featureId, getState())
                is Intent.RemoveProficiency -> removeProficiency(intent.proficiency, getState())
                is Intent.UpdateDescription -> updateDescription(intent.description, getState())
                is Intent.UpdateName -> updateName(intent.name, getState())
                is Intent.UpdateNamePlural -> updateNamePlural(intent.namePlural, getState())
                is Intent.UpdateStatMods -> updateStatMods(intent.statMods, getState())
            }

        private fun addFeature(featureId: Long, state: State) {
            dispatch(Msg.AddFeature(featureId))
            state.features.add(featureId)
            publish(Label.Changed(
                RaceData(
                    id = id,
                    name = state.name,
                    raceNamePlural = state.namePlural,
                    description = state.description,
                    statMods = state.statMods,
                    proficiencies = state.proficiencies,
                    features = state.features
                )
            ))
            database.addFeature(id, featureId).subscribeScoped()
        }

        private fun addProficiency(proficiency: Proficiency, state: State) {
            dispatch(Msg.AddProficiency(proficiency))
            state.proficiencies.add(proficiency)
            publish(Label.Changed(
                RaceData(
                    id = id,
                    name = state.name,
                    raceNamePlural = state.namePlural,
                    description = state.description,
                    statMods = state.statMods,
                    proficiencies = state.proficiencies,
                    features = state.features
                )
            ))
            database.addProficiency(id, proficiency).subscribeScoped()
        }

        private fun removeFeature(featureId: Long, state: State) {
            dispatch(Msg.RemoveFeature(featureId))
            state.features.remove(featureId)
            publish(Label.Changed(
                RaceData(
                    id = id,
                    name = state.name,
                    raceNamePlural = state.namePlural,
                    description = state.description,
                    statMods = state.statMods,
                    proficiencies = state.proficiencies,
                    features = state.features
                )
            ))
            database.removeFeature(id, featureId).subscribeScoped()
        }

        private fun removeProficiency(proficiency: Proficiency, state: State) {
            dispatch(Msg.RemoveProficiency(proficiency))
            state.proficiencies.remove(proficiency)
            publish(Label.Changed(
                RaceData(
                    id = id,
                    name = state.name,
                    raceNamePlural = state.namePlural,
                    description = state.description,
                    statMods = state.statMods,
                    proficiencies = state.proficiencies,
                    features = state.features
                )
            ))
            database.removeProficiency(id, proficiency).subscribeScoped()
        }

        private fun updateDescription(description: String, state: State) {
            dispatch(Msg.DescriptionChanged(description))
            publish(Label.Changed(
                RaceData(
                    id = id,
                    name = state.name,
                    raceNamePlural = state.namePlural,
                    description = description,
                    statMods = state.statMods,
                    proficiencies = state.proficiencies,
                    features = state.features
                )
            ))
            database.updateDescription(id, description).subscribeScoped()
        }

        private fun updateName(name: String, state: State) {
            dispatch(Msg.NameChanged(name))
            publish(Label.Changed(
                RaceData(
                    id = id,
                    name = name,
                    raceNamePlural = state.namePlural,
                    description = state.description,
                    statMods = state.statMods,
                    proficiencies = state.proficiencies,
                    features = state.features
                )
            ))
            database.updateName(id, name).subscribeScoped()
        }

        private fun updateNamePlural(namePlural: String, state: State) {
            dispatch(Msg.NamePluralChanged(namePlural))
            publish(Label.Changed(
                RaceData(
                    id = id,
                    name = state.name,
                    raceNamePlural = namePlural,
                    description = state.description,
                    statMods = state.statMods,
                    proficiencies = state.proficiencies,
                    features = state.features
                )
            ))
            database.updateNamePlural(id, namePlural).subscribeScoped()
        }

        private fun updateStatMods(statMods: MutableMap<Stats, Int>, state: State) {
            dispatch(Msg.StatModsChanged(statMods))
            publish(Label.Changed(
                RaceData(
                    id = id,
                    name = state.name,
                    raceNamePlural = state.namePlural,
                    description = state.description,
                    statMods = statMods,
                    proficiencies = state.proficiencies,
                    features = state.features
                )
            ))
            database.updateStatMods(id, statMods).subscribeScoped()
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when(msg) {
                is Msg.Loaded -> copy(
                    name = msg.item.name,
                    namePlural = msg.item.raceNamePlural,
                    description = msg.item.description,
                    statMods = msg.item.statMods,
                    proficiencies = msg.item.proficiencies,
                    features = msg.item.features
                )
                is Msg.AddFeature -> copy(features = updateList(features, msg.featureId))
                is Msg.AddProficiency -> copy(proficiencies = updateList(proficiencies, msg.proficiency))
                is Msg.DescriptionChanged -> copy(description = msg.description)
                is Msg.NameChanged -> copy(name = msg.name)
                is Msg.NamePluralChanged -> copy(namePlural = msg.namePlural)
                is Msg.RemoveFeature -> copy(features = updateList(features, msg.featureId, true))
                is Msg.RemoveProficiency -> copy(proficiencies = updateList(proficiencies, msg.proficiency, true))
                is Msg.StatModsChanged -> copy(statMods = msg.statMods)
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
        fun load(id: Long): Maybe<RaceData>

        fun updateName(id: Long, name: String): Completable

        fun updateNamePlural(id: Long, namePlural: String): Completable

        fun updateDescription(id: Long, description: String): Completable

        fun addProficiency(id: Long, proficiency: Proficiency): Completable

        fun removeProficiency(id: Long, proficiency: Proficiency): Completable

        fun updateStatMods(id: Long, statMods: MutableMap<Stats, Int>): Completable

        fun addFeature(id: Long, featureId: Long): Completable

        fun removeFeature(id: Long, featureId: Long): Completable
    }
}