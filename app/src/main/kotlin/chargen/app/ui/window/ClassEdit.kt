package chargen.app.ui.window

import chargen.lib.character.data.dnd.classes.CasterClassData
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.DiceType
import com.arkivanov.decompose.value.Value

interface ClassEdit {

    val models: Value<Model>

    fun onCloseClicked()

    fun onAddFeatureClicked()
    fun onRemoveFeature(featureId: Long)
    fun onFeatureClicked(featureId: Long)
    fun onAddProficiencyClicked()
    fun onRemoveProficiency(proficiency: Proficiency)
    fun onResourcesChanged(resource: Int, resourceName: String)
    fun onClassNameChanged(name: String)
    fun onSetCasterData(casterData: CasterClassData)
    fun onHitDieChange(hitDie: DiceType)
    fun onNumAttacksChange(numAttacks: Int)
    fun onIsCasterChange(value: Boolean)

    data class Model(
        var name: String,
        var isCaster: Boolean,
        var numAttacks: Int,
        var hitDie: DiceType,
        var features: MutableList<FeatureData>,
        var proficiencies: MutableList<Proficiency>,
        var resource: Int?,
        var resourceName: String?,
        var casterData: CasterClassData?
    )

    sealed class Output {
        object Finished : Output()
    }
}