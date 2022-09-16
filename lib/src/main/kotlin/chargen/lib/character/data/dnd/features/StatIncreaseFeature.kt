package chargen.lib.character.data.dnd.features

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.templates.Feature
import chargen.lib.character.data.dnd.types.FeatureType
import chargen.lib.character.data.dnd.utils.Utils

internal class StatIncreaseFeature(override val featureData: FeatureData) : Feature {
    override val featureType: FeatureType = FeatureType.STAT_INCREASE

    override fun modifyCharacter(item: CharacterData): CharacterData {
        if (!verify(item)) return item

        if (this.featureData.stat != null) {
           if (this.featureData.sourceStat != null) {
               val base = item.getStat(this.featureData.stat)
               val modifier = Utils.convertAttributeToModifier(item.getStat(this.featureData.sourceStat))
               item.setStat(this.featureData.stat, base + modifier)
           } else if (this.featureData.value != null) {
               val base = item.getStat(this.featureData.stat)
               item.setStat(this.featureData.stat, base + this.featureData.value)
           }
        }
        return item
    }
}
