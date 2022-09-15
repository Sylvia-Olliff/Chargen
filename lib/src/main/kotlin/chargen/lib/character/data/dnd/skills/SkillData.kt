package chargen.lib.character.data.dnd.skills

import chargen.lib.character.data.dnd.templates.DataEntity
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class SkillData(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID,
    var name: String,
    var stat: Stats,
    var untrained: Boolean
): DataEntity
