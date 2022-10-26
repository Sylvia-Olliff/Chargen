package chargen.app.ui.window

import chargen.lib.character.data.dnd.races.RaceData
import com.arkivanov.decompose.value.Value

interface RaceMain {
    val models: Value<Model>

    fun onRaceSelected(id: Long)
    fun onRaceAddClicked()
    fun onRaceDeleteClicked(id: Long)

    data class Model(
        val items: List<RaceData>,
        val selected: RaceData
    )

    sealed class Output {
        data class Selected(val id: Long) : Output()
    }
}