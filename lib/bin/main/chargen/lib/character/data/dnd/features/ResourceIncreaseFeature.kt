package chargen.lib.character.data.dnd.features

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.templates.Feature
import chargen.lib.character.data.dnd.types.FeatureType

internal class ResourceIncreaseFeature(override val featureData: FeatureData) : Feature {
    override val featureType: FeatureType = FeatureType.RESOURCE_INCREASE

    override fun modifyCharacter(item: CharacterData): CharacterData {
        if (!verify(item)) return item

        item.getClassData()?.resource?.plus(this.featureData.value!!)
        return item
    }
}
