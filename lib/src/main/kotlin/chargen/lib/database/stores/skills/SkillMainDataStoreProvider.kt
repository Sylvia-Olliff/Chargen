package chargen.lib.database.stores.skills

import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.database.stores.races.RaceMainDataStore
import chargen.lib.database.stores.races.RaceMainDataStoreProvider
import chargen.lib.database.stores.skills.SkillMainDataStore.Intent
import chargen.lib.database.stores.skills.SkillMainDataStore.State
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.mainScheduler

class SkillMainDataStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database
) {

    fun provide(): SkillMainDataStore =
        object: SkillMainDataStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "SkillMainStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class ItemsLoaded(val items: List<SkillData>): Msg()
        data class ItemAdded(val item: SkillData): Msg()
        data class ItemDeleted(val id: Long): Msg()
    }

    private inner class ExecutorImpl: ReaktiveExecutor<Intent, Unit, State, Msg, Nothing>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            database
                .updates
                .observeOn(mainScheduler)
                .map(Msg::ItemsLoaded)
                .subscribeScoped(onNext = ::dispatch)
        }

        override fun executeIntent(intent: Intent, getState: () -> State) =
            when(intent) {
                Intent.AddSkill -> addSkill(getState())
                is Intent.DeleteSkill -> deleteSkill(intent.id)
            }

        private fun addSkill(state: State) {
            dispatch(Msg.ItemAdded(state.selected))
            database.addSkill(state.selected).subscribeScoped()
        }

        private fun deleteSkill(id: Long) {
            dispatch(Msg.ItemDeleted(id))
            database.deleteSkill(id).subscribeScoped()
        }
    }

    private object ReducerImpl: Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when(msg) {
                is Msg.ItemAdded -> copy(selected = msg.item)
                is Msg.ItemDeleted -> copy(items = items.filterNot { it.id == msg.id })
                is Msg.ItemsLoaded -> copy(items = msg.items)
            }
    }

    interface Database {
        val updates: Observable<List<SkillData>>

        fun addSkill(item: SkillData): Completable

        fun deleteSkill(id: Long): Completable
    }
}