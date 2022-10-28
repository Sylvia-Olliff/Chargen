package chargen.lib.database.stores.classes

import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.database.stores.classes.ClassMainDataStore.Intent
import chargen.lib.database.stores.classes.ClassMainDataStore.State
import com.arkivanov.mvikotlin.core.store.Store

interface ClassMainDataStore : Store<Intent, State, Nothing> {
    sealed class Intent {
        data class DeleteClass(val id: Long): Intent()
        object AddClass: Intent()
    }

    data class State (
        val items: List<ClassData> = emptyList(),
        val selected: ClassData = ClassData.DEFAULT
    )
}