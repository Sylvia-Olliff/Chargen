package chargen.lib.character.data.dnd.races

import chargen.database.RaceDataEntity
import chargen.lib.character.data.dnd.templates.DataEntity
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.Stats

import kotlinx.serialization.Serializable

@Serializable
data class RaceData(
    override val id: Long,
    override val name: String,
    val raceNamePlural: String,
    val description: String,
    val statMods: MutableMap<Stats, Int>,
    val proficiencies: MutableList<Proficiency>,
    val features: MutableList<Long>,
): DataEntity {
    companion object {
        val DEFAULT = RaceData(
            0L,
            "Race Name",
            "Race Name Plural",
            "Race Description",
            mutableMapOf(),
            mutableListOf(),
            mutableListOf()
        )
    }
}

fun RaceData.toEntity(): RaceDataEntity {
    return RaceDataEntity(
        id, name, raceNamePlural, description, statMods, proficiencies, features
    )
}
