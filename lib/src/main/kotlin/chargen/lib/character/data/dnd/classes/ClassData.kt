package chargen.lib.character.data.dnd.classes

import chargen.database.ClassDataEntity
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.templates.DataEntity
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.DiceType
import kotlinx.serialization.Serializable

@Serializable
data class ClassData(
    override val id: Long,
    override val name: String,
    var isCaster: Boolean,
    var hitDie: DiceType,
    var numAttacks: Int,
    val proficiencies: MutableList<Proficiency>,
    val features: MutableList<FeatureData>,
    var resourceName: String?,
    var resource: Int?,
    val casterData: CasterClassData?
): DataEntity

fun ClassData.toEntity(): ClassDataEntity = ClassDataEntity(
    id,
    name,
    isCaster,
    hitDie,
    numAttacks,
    resourceName,
    resource,
    proficiencies.toList(),
    features.map { it.id },
    casterData
)