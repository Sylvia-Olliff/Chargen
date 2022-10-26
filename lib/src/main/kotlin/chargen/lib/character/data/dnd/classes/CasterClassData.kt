package chargen.lib.character.data.dnd.classes

import chargen.lib.character.data.dnd.types.CasterType
import chargen.lib.character.data.dnd.types.Stats
import kotlinx.serialization.Serializable

@Serializable
data class CasterClassData(
    val castingStat: Stats? = null,
    val casterType: CasterType? = null,
    val spellListIDs: List<Long>? = null,
    var spellsPerLevel: MutableMap<Int, MutableMap<Int, Int>?>? = null
) {
    fun getSpellSet(): MutableMap<Int, MutableMap<Int, Int>?> {
        if (this.spellsPerLevel != null) return this.spellsPerLevel!!

        return this.casterType!!.spellSet
    }
}
