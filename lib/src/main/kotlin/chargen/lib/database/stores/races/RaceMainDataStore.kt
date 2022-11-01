package chargen.lib.database.stores.races

import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.database.stores.races.RaceMainDataStore.Intent
import chargen.lib.database.stores.races.RaceMainDataStore.State
import com.arkivanov.mvikotlin.core.store.Store

interface RaceMainDataStore: Store<Intent, State, Nothing> {

    sealed class Intent {
        data class DeleteRace(val id: Long): Intent()
        object AddRace: Intent()
    }

    data class State(
        val items: List<RaceData> = emptyList(),
        val selected: RaceData = RaceData.DEFAULT
    )
}