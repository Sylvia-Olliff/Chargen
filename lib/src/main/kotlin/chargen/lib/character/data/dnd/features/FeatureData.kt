package chargen.lib.character.data.dnd.features

import chargen.database.FeatureDataEntity
import chargen.lib.character.data.dnd.templates.DataEntity
import chargen.lib.character.data.dnd.types.FeatureType
import chargen.lib.character.data.dnd.types.Stats
import kotlinx.serialization.Serializable

@Serializable
data class FeatureData(
    override val id: Long,
    override var name: String,
    var levelGained: Int,
    var description: String,
    var group: String,
    val requiredFeatures: MutableList<Long>,
    val featureType: FeatureType,
    val value: Int?,
    val stat: Stats?,
    val sourceStat: Stats?,
    val spellSlots: MutableMap<Int, MutableMap<Int, Int>>?
): DataEntity {
    companion object {
        val DEFAULT = FeatureData(
            0L,
            "Feature Name",
            0,
            "Feature Description",
            "UNIVERSAL",
            mutableListOf(),
            FeatureType.NEW_ABILITY,
            null, null, null, null
        )
    }
}

fun FeatureData.toEntity(): FeatureDataEntity {
    return FeatureDataEntity(
        id,
        name,
        levelGained,
        description,
        group,
        requiredFeatures,
        featureType,
        value,
        stat,
        sourceStat,
        spellSlots
    )
}
