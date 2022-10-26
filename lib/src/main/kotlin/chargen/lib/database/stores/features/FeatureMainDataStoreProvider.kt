package chargen.lib.database.stores.features

import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.database.stores.features.FeatureMainDataStore.Intent
import chargen.lib.database.stores.features.FeatureMainDataStore.State
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

class FeatureMainDataStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database
) {

    fun provide(): FeatureMainDataStore =
        object : FeatureMainDataStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "FeatureDataStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class ItemsLoaded(val items: List<FeatureData>): Msg()
        data class ItemAdded(val item: FeatureData): Msg()
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
            when(intent) {
                is Intent.AddFeature -> addFeature(getState())
                is Intent.DeleteFeature -> deleteFeature(intent.id)
            }

        private fun addFeature(state: State) {
            if (state.selected.name.isNotEmpty() && state.selected.description.isNotEmpty()) {
                dispatch(Msg.ItemAdded(state.selected))
                database.addFeature(state.selected).subscribeScoped()
            }
        }

        private fun deleteFeature(id: Long) {
            dispatch(Msg.ItemDeleted(id))
            database.deleteFeature(id).subscribeScoped()
        }
    }

    private object ReducerImpl: Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when(msg) {
                is Msg.ItemAdded -> copy(selected = msg.item)
                is Msg.ItemDeleted -> copy(items = items.filterNot { it.id == msg.id })
                is Msg.ItemsLoaded -> copy(items = msg.items.sorted())
            }

        private inline fun State.update(id: Long, func: FeatureData.() -> FeatureData): State {
            val item = items.find { it.id == id } ?: return this

            return put(item.func())
        }

        private inline fun State.put(item: FeatureData): State {
            val oldItems = items.associateByTo(mutableMapOf(), FeatureData::id)
            val oldItem: FeatureData? = oldItems.put(item.id, item)

            return copy(items = if (oldItem?.name == item.name) oldItems.values.toList() else oldItems.values.sorted())
        }

        private fun Iterable<FeatureData>.sorted(): List<FeatureData> = sortedBy(FeatureData::name)
    }

    interface Database {
        val updates: Observable<List<FeatureData>>

        fun addFeature(item: FeatureData): Completable

        fun deleteFeature(id: Long): Completable
    }
}