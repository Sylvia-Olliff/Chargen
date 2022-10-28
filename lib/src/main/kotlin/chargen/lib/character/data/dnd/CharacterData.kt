package chargen.lib.character.data.dnd

import chargen.database.CharacterDataEntity
import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.types.Alignment
import chargen.lib.character.data.dnd.types.Stats
import kotlinx.serialization.Serializable

@Serializable
data class CharacterData(
    val id: Long,
    var playerName: String?,
    var characterName: String?,
    var campaignName: String?,
    val stats: MutableMap<Stats, Int>,
    var raceData: RaceData?,
    var classData: ClassData?,
    var skills: MutableMap<SkillData, Boolean>,
    var alignment: Alignment?,
    var background: String?,
    val abilities: MutableList<String>,
    val currentFeatures: MutableList<FeatureData>,
    var EXP: Int,
    var level: Int,
    val characteristics: Characteristics,
    var backstory: String?,
    var notes: String?
) {
    companion object {
        val DEFAULT = CharacterData(
            0L,
            null,
            null,
            null,
            mutableMapOf(
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
            null,
            null,
            mutableMapOf(),
            null,
            null,
            mutableListOf(),
            mutableListOf(),
            0,
            0,
            Characteristics(null, null, null, null, null, null),
            null,
            null
        )
    }
}

fun CharacterData.toEntity(): CharacterDataEntity {
    return CharacterDataEntity(
        id = id,
        playerName = playerName,
        characterName = characterName,
        campaignName = campaignName,
        raceDataId = raceData?.id,
        classDataId = classData?.id,
        stats = stats,
        skillIds = skills.mapKeys { it.key.id }.toMutableMap(),
        alignment = alignment,
        background = background,
        abilities = abilities,
        currentFeatureIds = currentFeatures.map { it.id }.toMutableList(),
        experience = EXP,
        level = level,
        characteristics = characteristics,
        backstory = backstory,
        notes = notes
    )
}
