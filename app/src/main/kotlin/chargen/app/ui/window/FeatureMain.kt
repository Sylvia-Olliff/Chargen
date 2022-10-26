package chargen.app.ui.window

import chargen.lib.character.data.dnd.features.FeatureData
import com.arkivanov.decompose.value.Value

interface FeatureMain {
    val models: Value<Model>

    fun onFeatureSelected(id: Long)
    fun onFeatureAddClicked()
    fun onFeatureDeleteClicked(id: Long)

    data class Model(
        val items: List<FeatureData>,
        val selected: FeatureData
    )

    sealed class Output {
        data class Selected(val id: Long) : Output()
    }
}