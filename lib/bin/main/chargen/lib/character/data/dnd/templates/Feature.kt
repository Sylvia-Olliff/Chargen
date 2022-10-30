package chargen.lib.character.data.dnd.templates

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.types.FeatureType
import kotlinx.coroutines.runBlocking

interface Feature {
    val featureData: FeatureData
    val featureType: FeatureType

    fun modifyCharacter(item: CharacterData): CharacterData

    fun verify(item: CharacterData): Boolean = runBlocking {
        featureData.requiredFeatures.forEach { featureId ->
            if (item.getCurrentFeatures().find { it.id == featureId } == null) {
                return@runBlocking false
            }
        }
        if (item.level < featureData.levelGained) {
            return@runBlocking false
        }
        return@runBlocking true
    }
}
