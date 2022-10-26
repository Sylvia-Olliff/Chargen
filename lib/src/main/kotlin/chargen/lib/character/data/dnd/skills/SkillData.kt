package chargen.lib.character.data.dnd.skills

import chargen.database.SkillDataEntity
import chargen.lib.character.data.dnd.templates.DataEntity
import chargen.lib.character.data.dnd.types.Stats
import kotlinx.serialization.Serializable

@Serializable
data class SkillData(
    override val id: Long,
    override var name: String,
    var description: String,
    var stat: Stats,
    var untrained: Boolean
): DataEntity

fun SkillData.toEntity(): SkillDataEntity {
    return SkillDataEntity(
        id, name, description, stat, untrained
    )
}