package chargen.lib.character.data.dnd.classes

import chargen.lib.character.data.dnd.types.CasterType
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class CasterClassData(
    val castingStat: Stats,
    val casterType: CasterType,
    val spellListIDs: List<@Serializable(with = UUIDSerializer::class) UUID>,
    var spellsPerLevel: MutableMap<Int, MutableMap<Int, Int>?>?
) {
    fun getSpellSet(): MutableMap<Int, MutableMap<Int, Int>?> {
        if (this.spellsPerLevel != null) return this.spellsPerLevel!!

        return this.casterType.spellSet
    }
}
