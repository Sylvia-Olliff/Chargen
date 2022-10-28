package chargen.lib.database.stores.characters

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.database.stores.characters.CharacterMainDataStore.State
import chargen.lib.database.stores.characters.CharacterMainDataStore.Intent
import chargen.lib.database.stores.classes.ClassMainDataStore
import chargen.lib.database.stores.classes.ClassMainDataStoreProvider
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

class CharacterMainDataStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database
) {

    fun provide(): CharacterMainDataStore =
        object : CharacterMainDataStore, Store<Intent, State, Nothing> by storeFactory.create(
            name = "CharacterMainStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class ItemsLoaded(val items: List<CharacterData>): Msg()
        data class ItemDeleted(val id: Long): Msg()
        data class ItemAdded(val characterData: CharacterData): Msg()
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
                Intent.AddCharacter -> addCharacter(getState())
                is Intent.DeleteCharacter -> deleteCharacter(intent.id)
            }

        private fun addCharacter(state: State) {
            dispatch(Msg.ItemAdded(state.selected))
            database.addCharacter(state.selected).subscribeScoped()
        }

        private fun deleteCharacter(id: Long) {
            dispatch(Msg.ItemDeleted(id))
            database.deleteCharacter(id).subscribeScoped()
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ItemsLoaded -> copy(items = msg.items.sorted())
                is Msg.ItemAdded -> copy(selected = msg.characterData)
                is Msg.ItemDeleted -> copy(items = items.filterNot { it.id == msg.id })
            }

        private fun Iterable<CharacterData>.sorted(): List<CharacterData> = sortedBy(CharacterData::campaignName)
    }

    interface Database {
        val updates: Observable<List<CharacterData>>

        fun addCharacter(item: CharacterData): Completable

        fun deleteCharacter(id: Long): Completable
    }
}