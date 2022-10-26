package chargen.app.ui.window

import chargen.lib.character.data.dnd.types.Stats
import com.arkivanov.decompose.value.Value

interface SkillEdit {
    val models: Value<Model>

    fun onClosedClicked()

    fun onNameChanged(name: String)
    fun onDescriptionChanged(description: String)
    fun onStatChanged(stat: Stats)
    fun onUntrainedChanged(untrained: Boolean)

    data class Model(
        var name: String,
        var description: String,
        var stat: Stats,
        var untrained: Boolean
    )

    sealed class Output {
        object Finished: Output()
    }
}