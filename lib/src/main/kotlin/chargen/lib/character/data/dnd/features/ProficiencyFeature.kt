package chargen.lib.character.data.dnd.features

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.templates.Feature
import chargen.lib.character.data.dnd.types.FeatureType

internal class ProficiencyFeature(override val featureData: FeatureData) : Feature {
    override val featureType: FeatureType = FeatureType.PROFICIENCY

    override fun modifyCharacter(item: CharacterData): CharacterData {
        TODO("Not yet implemented")
    }
}