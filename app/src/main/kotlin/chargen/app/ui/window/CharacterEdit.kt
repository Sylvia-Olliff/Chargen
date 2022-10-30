package chargen.app.ui.window

import chargen.lib.character.data.dnd.Characteristics
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.types.Alignment
import chargen.lib.character.data.dnd.types.Stats
import com.arkivanov.decompose.value.Value

interface CharacterEdit {

    val models: Value<Model>
    val features: List<FeatureData>
    val classes: List<ClassData>
    val races: List<RaceData>

    fun onCloseClicked()

    fun onPlayerNameChanged(name: String)
    fun onCharacterNameChanged(name: String)
    fun onCampaignNameChanged(name: String)
    fun onStatChanged(stat: Stats, value: Int)
    fun onRaceClicked()
    fun onRaceSelected(race: RaceData)
    fun onClassClicked()
    fun onClassSelected(clazz: ClassData)
    fun onSkillChanged(skill: SkillData, isProficient: Boolean)
    fun onAlignmentChanged(alignment: Alignment)
    fun onBackgroundChanged(background: String)
    fun onBackstoryChanged(backstory: String)
    fun onAbilityRemoved(ability: String)
    fun onAbilityAdded(ability: String)
    fun onFeatureAdded(feature: FeatureData)
    fun onFeatureRemoved(feature: FeatureData)
    fun onExpChanged(exp: Int)
    fun onLevelChanged(level: Int)
    fun onCharacteristicsChanged(characteristics: Characteristics)
    fun onNotesChanged(notes: String)

    data class Model(
        var playerName: String,
        var characterName: String,
        var campaignName: String,
        val stats: MutableMap<Stats, Int>,
        var race: RaceData?,
        var clazz: ClassData?,
        val skills: MutableMap<SkillData, Boolean>,
        var alignment: Alignment,
        var background: String,
        var backstory: String,
        val abilities: MutableList<String>,
        val currentFeatures: MutableList<FeatureData>,
        var exp: Int,
        var level: Int,
        val characteristics: Characteristics,
        var notes: String
    )

    sealed class Output {
        object Finished: Output()
    }
}
