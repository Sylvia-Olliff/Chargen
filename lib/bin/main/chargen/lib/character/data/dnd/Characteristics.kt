package chargen.lib.character.data.dnd

import kotlinx.serialization.Serializable

@Serializable
data class Characteristics(
    var age: Int?,
    var height: String?,
    var weight: String?,
    var eyes: String?,
    var skin: String?,
    var hair: String?,
)
