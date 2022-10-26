package chargen.app.ui.window

import chargen.lib.character.data.dnd.types.FeatureType
import chargen.lib.character.data.dnd.types.Stats
import com.arkivanov.decompose.value.Value

interface FeatureEdit {

    val models: Value<Model>

    fun onCloseClicked()

    fun onAddRequiredFeatureClicked()
    fun onRemoveRequiredFeatureClicked(featureId: Long)
    fun onRequiredFeatureClicked(featureId: Long)
    fun onNameChanged(name: String)
    fun onDescriptionChanged(description: String)
    fun onLevelGainedChanged(levelGained: Int)
    fun onGroupNameChanged(group: String)
    fun onFeatureTypeChanged(featureType: FeatureType)
    fun onValueChanged(value: Int)
    fun onStatChanged(stat: Stats)
    fun onSourceStatChanged(stat: Stats)
    fun onSpellSlotsChanged(spellSlots: MutableMap<Int, MutableMap<Int, Int>>)

    data class Model(
        var name: String,
        var description: String,
        var levelGained: Int,
        var group: String,
        var requiredFeatures: MutableList<Long>,
        var featureType: FeatureType,
        var value: Int?,
        var stat: Stats?,
        var sourceStat: Stats?,
        var spellSlots: MutableMap<Int, MutableMap<Int, Int>>?
    )

    sealed class Output {
        object Finished: Output()
    }
}