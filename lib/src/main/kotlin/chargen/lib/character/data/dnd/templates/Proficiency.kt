package chargen.lib.character.data.dnd.templates

import chargen.lib.character.data.dnd.types.ProficiencyType
import kotlinx.serialization.Serializable

@Serializable
data class Proficiency(
    val name: String,
    val description: String,
    val type: ProficiencyType
) {
    companion object {
        val DEFAULT = Proficiency(
            "Proficiency Name",
            "Proficiency Description",
            ProficiencyType.SKILL
        )
    }
}
