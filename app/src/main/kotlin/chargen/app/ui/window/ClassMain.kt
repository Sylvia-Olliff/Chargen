package chargen.app.ui.window

import chargen.lib.character.data.dnd.classes.ClassData
import com.arkivanov.decompose.value.Value

interface ClassMain {
    val models: Value<Model>

    fun onClassSelected(id: Long)
    fun onClassAddedClicked()
    fun onClassDeletedClicked(id: Long)

    data class Model(
        val items: List<ClassData>,
        val selected: ClassData
    )

    sealed class Output {
        data class Selected(val id: Long) : Output()
    }
}