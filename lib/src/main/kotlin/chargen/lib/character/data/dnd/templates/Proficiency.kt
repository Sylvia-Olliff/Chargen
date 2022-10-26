package chargen.lib.character.data.dnd.templates

import kotlinx.serialization.Serializable

@Serializable
data class Proficiency(
    val name: String,
    val description: String
) {
    companion object {
        val DEFAULT = Proficiency(
            "Proficiency Name",
            "Proficiency Description"
        )
    }
}
