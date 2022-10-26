package chargen.lib.database.stores.classes

import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.types.DiceType
import chargen.lib.database.stores.classes.ClassMainDataStore.State
import chargen.lib.database.stores.classes.ClassMainDataStore.Intent
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

class ClassMainDataStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database
) {

    fun provide(): ClassMainDataStore =
        object : ClassMainDataStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "ClassDataStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class ItemsLoaded(val items: List<ClassData>): Msg()
        data class ItemDeleted(val id: Long): Msg()
        data class ItemAdded(val classData: ClassData): Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Msg, Nothing>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            database
                .updates
                .observeOn(mainScheduler)
                .map(Msg::ItemsLoaded)
                .subscribeScoped(onNext = ::dispatch)
        }

        override fun executeIntent(intent: Intent, getState: () -> State): Unit =
            when (intent) {
                is Intent.AddClass -> addClass(state = getState())
                is Intent.DeleteClass -> deleteClass(intent.id)
            }

        private fun deleteClass(id: Long) {
            dispatch(Msg.ItemDeleted(id))
            database.deleteClass(id).subscribeScoped()
        }

        private fun addClass(state: State) {
            if (state.selected.name.isNotEmpty() && state.selected.numAttacks > 0) {
                if (state.selected.resource != null || state.selected.resourceName != null) {
                    if (state.selected.resource != null && state.selected.resourceName != null) {
                        dispatch(
                            Msg.ItemAdded(
                                ClassData(
                                    0L,
                                    "Class Name",
                                    false,
                                    DiceType.D6,
                                    1,
                                    mutableListOf(),
                                    mutableListOf(),
                                    null, null, null
                                )
                            )
                        )
                        database.addClass(state.selected).subscribeScoped()
                    }
                } else {
                    dispatch(
                        Msg.ItemAdded(
                            ClassData(
                                0L,
                                "Class Name",
                                false,
                                DiceType.D6,
                                1,
                                mutableListOf(),
                                mutableListOf(),
                                null, null, null
                            )
                        )
                    )
                    database.addClass(state.selected).subscribeScoped()
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ItemsLoaded -> copy(items = msg.items.sorted())
                is Msg.ItemDeleted -> copy(items = items.filterNot { it.id == msg.id })
                is Msg.ItemAdded   -> copy(selected = msg.classData)
            }

        private inline fun State.update(id: Long, func: ClassData.() -> ClassData): State {
            val item = items.find { it.id == id } ?: return this

            return put(item.func())
        }

        private inline fun State.put(item: ClassData): State {
            val oldItems = items.associateByTo(mutableMapOf(), ClassData::id)
            val oldItem: ClassData? = oldItems.put(item.id, item)

            return copy(items = if (oldItem?.name == item.name) oldItems.values.toList() else oldItems.values.sorted())
        }

        private fun Iterable<ClassData>.sorted(): List<ClassData> = sortedBy(ClassData::name)
    }

    interface Database {
        val updates: Observable<List<ClassData>>

        fun addClass(item: ClassData): Completable

        fun deleteClass(id: Long): Completable
    }
}