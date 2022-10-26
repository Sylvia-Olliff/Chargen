package chargen.lib.database.stores.races

import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.database.stores.races.RaceMainDataStore.Intent
import chargen.lib.database.stores.races.RaceMainDataStore.State
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

class RaceMainDataStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database
) {

    fun provide(): RaceMainDataStore =
        object: RaceMainDataStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "RaceDataStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class ItemsLoaded(val items: List<RaceData>): Msg()
        data class ItemAdded(val item: RaceData): Msg()
        data class ItemDeleted(val id: Long): Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Msg, Nothing>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            database
                .updates
                .observeOn(mainScheduler)
                .map(Msg::ItemsLoaded)
                .subscribeScoped(onNext = ::dispatch)
        }

        override fun executeIntent(intent: Intent, getState: () -> State) =
            when (intent) {
                Intent.AddRace -> addRace(getState())
                is Intent.DeleteRace -> deleteRace(intent.id)
            }

        private fun addRace(state: State) {
            if (state.selected.name.isNotEmpty() && state.selected.description.isNotEmpty()) {
                dispatch(Msg.ItemAdded(state.selected))
                database.addRace(state.selected).subscribeScoped()
            }
        }

        private fun deleteRace(id: Long) {
            dispatch(Msg.ItemDeleted(id))
            database.deleteRace(id).subscribeScoped()
        }
    }

    private object ReducerImpl: Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when(msg) {
                is Msg.ItemAdded -> copy(selected = msg.item)
                is Msg.ItemDeleted -> copy(items = items.filterNot { it.id == msg.id })
                is Msg.ItemsLoaded -> copy(items = msg.items.sorted())
            }

        private fun Iterable<RaceData>.sorted(): List<RaceData> = sortedBy(RaceData::name)
    }

    interface Database {
        val updates: Observable<List<RaceData>>

        fun addRace(item: RaceData): Completable

        fun deleteRace(id: Long): Completable
    }
}