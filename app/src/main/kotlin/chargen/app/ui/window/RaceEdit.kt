package chargen.app.ui.window

import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.Stats
import com.arkivanov.decompose.value.Value

interface RaceEdit {
    val models: Value<Model>

    fun onClosedClicked()

    fun onNameChanged(name: String)
    fun onNamePluralChanged(namePlural: String)
    fun onDescriptionChanged(description: String)
    fun onAddProficiencyClicked(proficiency: Proficiency)
    fun onRemoveProficiencyClicked(proficiency: Proficiency)
    fun onAddFeatureClicked(feature: FeatureData)
    fun onRemoveFeatureClicked(feature: FeatureData)
    fun onStatModsChanged(statMods: MutableMap<Stats, Int>)

    data class Model(
        var name: String,
        var namePlural: String,
        var description: String,
        var proficiencies: MutableList<Proficiency>,
        var features: MutableList<FeatureData>,
        var statMods: MutableMap<Stats, Int>
    )

    sealed class Output {
        object Finished: Output()
    }
}