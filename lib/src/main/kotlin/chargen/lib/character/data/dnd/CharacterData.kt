package chargen.lib.character.data.dnd

import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.types.Alignment
import chargen.lib.character.data.dnd.types.Stats
import kotlinx.serialization.Serializable

@Serializable
data class CharacterData(
    val id: Long,
    var playerName: String,
    var characterName: String?,
    var campaignName: String?,
    val stats: MutableMap<Stats, Int>,
    var raceData: RaceData?,
    var classData: ClassData?,
    var skills: MutableMap<Long, Boolean>,
    var alignment: Alignment?,
    var background: String?,
    val abilities: MutableList<String>,
    val currentFeatures: MutableList<Long>,
    var EXP: Int,
    var level: Int,
    val characteristics: Characteristics,
    var backstory: String?,
    var notes: String?
) {
    fun getDefaultSaveName(): String {
        return when(characterName) {
            null -> playerName
            else -> "$playerName-$characterName"
        }
    }
}
