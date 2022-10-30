package chargen.lib.character.data.dnd.features

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.templates.Feature
import chargen.lib.character.data.dnd.types.FeatureType

internal class NewAbilityFeature(override val featureData: FeatureData) : Feature {
    override val featureType: FeatureType = FeatureType.NEW_ABILITY

    override fun modifyCharacter(item: CharacterData): CharacterData {
        if (!verify(item)) return item

        item.addAbility(this.featureData.description)
        return item
    }
}
