package chargen.lib.database.stores.characters

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.Characteristics
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.types.Alignment
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.character.data.dnd.utils.Utils
import chargen.lib.database.stores.characters.CharacterEditDataStore.Intent
import chargen.lib.database.stores.characters.CharacterEditDataStore.State
import chargen.lib.database.stores.characters.CharacterEditDataStore.Label
import com.arkivanov.mvikotlin.core.store.Store

interface CharacterEditDataStore: Store<Intent, State, Label> {
    sealed class Intent {
        data class UpdatePlayerName(val name: String): Intent()
        data class UpdateCharacterName(val name: String): Intent()
        data class UpdateCampaignName(val name: String): Intent()
        data class UpdateStats(val stat: Stats, val value: Int): Intent()
        data class UpdateRaceData(val raceId: Long): Intent()
        data class UpdateClassData(val classId: Long): Intent()
        data class UpdateSkillIsTrained(val skillId: Long, val value: Boolean): Intent()
        data class RemoveSkill(val skillId: Long): Intent()
        data class UpdateAlignment(val alignment: Alignment): Intent()
        data class UpdateBackground(val background: String): Intent()
        data class RemoveAbility(val ability: String): Intent()
        data class AddAbility(val ability: String): Intent()
        data class AddFeature(val featureId: Long): Intent()
        data class RemoveFeature(val featureId: Long): Intent()
        data class UpdateExperience(val exp: Int): Intent()
        data class UpdateLevel(val level: Int): Intent()
        data class UpdateCharacteristics(val characteristics: Characteristics): Intent()
        data class UpdateBackstory(val backstory: String): Intent()
        data class UpdateNotes(val notes: String): Intent()
        object LoadFeatures: Intent()
        object LoadClasses: Intent()
        object LoadRaces: Intent()
    }

    data class State(
        var playerName: String = "Player name",
        var characterName: String = "Character Name",
        var campaignName: String = "Campaign Name",
        var stats: MutableMap<Stats, Int> = mutableMapOf(
            Stats.STR to 10,
            Stats.DEX to 10,
            Stats.CON to 10,
            Stats.INT to 10,
            Stats.WIS to 10,
            Stats.CHA to 10,
            Stats.AC to 10,
            Stats.PROF to 2,
            Stats.INIT to 0
        ),
        var raceData: RaceData? = RaceData.DEFAULT,
        var classData: ClassData? = ClassData.DEFAULT,
        var skills: MutableMap<SkillData, Boolean> = Utils.buildSkillMap(),
        var alignment: Alignment = Alignment.NEUTRAL_TRUE,
        var background: String = "Character Background",
        var abilities: MutableList<String> = mutableListOf(),
        var currentFeatures: MutableList<FeatureData> = mutableListOf(),
        var experience: Int = 0,
        var level: Int = 0,
        var characteristics: Characteristics = Characteristics(null, null, null, null, null, null),
        var backstory: String = "Character backstory",
        var notes: String = "",

        var features: List<FeatureData> = emptyList(),
        var races: List<RaceData> = emptyList(),
        var classes: List<ClassData> = emptyList()
    )
    sealed class Label {
        data class Changed(val item: CharacterData): Label()
    }
}