package chargen.lib.database.stores.classes

import chargen.lib.character.data.dnd.classes.CasterClassData
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.DiceType
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
        val selected: ClassData = ClassData(
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
}