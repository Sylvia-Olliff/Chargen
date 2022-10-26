package chargen.lib.character.data.dnd.features

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.templates.Feature
import chargen.lib.character.data.dnd.types.FeatureType

internal class ExtraAttackFeature(override val featureData: FeatureData) : Feature {
    override val featureType: FeatureType = FeatureType.EXTRA_ATTACK

    override fun modifyCharacter(item: CharacterData): CharacterData {
        if (!verify(item)) return item

//        item.getClassData()?.numAttacks?.plus(featureData.value!!)
        return item
    }
}
