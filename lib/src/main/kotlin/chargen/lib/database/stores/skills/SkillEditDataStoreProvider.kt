package chargen.lib.database.stores.skills

import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.stores.skills.SkillEditDataStore.Label
import chargen.lib.database.stores.skills.SkillEditDataStore.Intent
import chargen.lib.database.stores.skills.SkillEditDataStore.State
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

class SkillEditDataStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database,
    private val id: Long
) {

    fun provide(): SkillEditDataStore =
        object : SkillEditDataStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SkillEditStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class Loaded(val item: SkillData): Msg()
        data class NameChanged(val name: String): Msg()
        data class DescriptionChanged(val description: String): Msg()
        data class StatChanged(val stat: Stats): Msg()
        data class UntrainedChanged(val untrained: Boolean): Msg()
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
                is Intent.UpdateDescription -> updateDescription(intent.description, getState())
                is Intent.UpdateName -> updateName(intent.name, getState())
                is Intent.UpdateStat -> updateStat(intent.stat, getState())
                is Intent.UpdateUntrained -> updateUntrained(intent.untrained, getState())
            }

        private fun updateName(name: String, state: State) {
            dispatch(Msg.NameChanged(name))
            publish(Label.Changed(
                SkillData(
                    id,
                    name = name,
                    description = state.description,
                    stat = state.stat,
                    untrained = state.untrained
                )
            ))
            database.updateName(id, name).subscribeScoped()
        }

        private fun updateDescription(description: String, state: State) {
            dispatch(Msg.DescriptionChanged(description))
            publish(Label.Changed(
                SkillData(
                    id,
                    name = state.name,
                    description = description,
                    stat = state.stat,
                    untrained = state.untrained
                )
            ))
            database.updateDescription(id, description).subscribeScoped()
        }

        private fun updateStat(stat: Stats, state: State) {
            dispatch(Msg.StatChanged(stat))
            publish(Label.Changed(
                SkillData(
                    id,
                    name = state.name,
                    description = state.description,
                    stat = stat,
                    untrained = state.untrained
                )
            ))
            database.updateStat(id, stat).subscribeScoped()
        }

        private fun updateUntrained(untrained: Boolean, state: State) {
            dispatch(Msg.UntrainedChanged(untrained))
            publish(Label.Changed(
                SkillData(
                    id,
                    name = state.name,
                    description = state.description,
                    stat = state.stat,
                    untrained = untrained
                )
            ))
            database.updateUntrained(id, untrained).subscribeScoped()
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.Loaded -> copy(
                    name = msg.item.name,
                    description = msg.item.description,
                    stat = msg.item.stat,
                    untrained = msg.item.untrained
                )
                is Msg.DescriptionChanged -> copy(description = msg.description)
                is Msg.NameChanged -> copy(name = msg.name)
                is Msg.StatChanged -> copy(stat = msg.stat)
                is Msg.UntrainedChanged -> copy(untrained = msg.untrained)
            }
    }

    interface Database {
        fun load(id: Long): Maybe<SkillData>

        fun updateName(id: Long, name: String): Completable

        fun updateDescription(id: Long, description: String): Completable

        fun updateStat(id: Long, stat: Stats): Completable

        fun updateUntrained(id: Long, untrained: Boolean): Completable
    }
}