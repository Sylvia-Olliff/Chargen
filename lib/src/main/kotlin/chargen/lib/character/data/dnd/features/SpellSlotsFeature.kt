package chargen.lib.character.data.dnd.features

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.templates.Feature
import chargen.lib.character.data.dnd.types.FeatureType

internal class SpellSlotsFeature(override val featureData: FeatureData) : Feature {
    override val featureType: FeatureType = FeatureType.SPELL_SLOTS

    override fun modifyCharacter(item: CharacterData): CharacterData {
        if (!verify(item)) return item

//        item.getClassData()?.casterData?.spellsPerLevel = item.getClassData()?.casterData?.spellsPerLevel?.let {
//            this.featureData.spellSlots?.let { it1 ->
//                mergeSpellMap(
//                    it,
//                    it1
//                )
//            }
//        }
        return item
    }

    private fun mergeSpellMap(original: MutableMap<Int, MutableMap<Int, Int>?>, additional: MutableMap<Int, MutableMap<Int, Int>>): MutableMap<Int, MutableMap<Int, Int>?> {
        additional.forEach { addSpellSet ->
            val currentLevel = original[addSpellSet.key]
            addSpellSet.value.forEach {
                if (currentLevel?.get(it.key) == null) {
                    currentLevel?.set(it.key, it.value)
                } else {
                    currentLevel[it.key]?.plus(it.value)
                }
            }
            original[addSpellSet.key] = currentLevel
        }
        return original
    }
}
