package chargen.lib

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.classes.ClassRegistry
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.features.FeatureRegistry
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.races.RaceRegistry
import chargen.lib.character.data.dnd.skills.SkillRegistry
import chargen.lib.config.Config
import java.util.*

@kotlinx.serialization.ExperimentalSerializationApi
class API {
    init {
        Config.loadConfig()
        ClassRegistry.load()
        RaceRegistry.load()
        FeatureRegistry.load()
        SkillRegistry.load()
    }

    companion object {
        /* GETTERS */
        fun getClasses(): List<ClassData> = ClassRegistry.getList()
        fun getRaces(): List<RaceData> = RaceRegistry.getList()
        fun getFeatures(): List<FeatureData> = FeatureRegistry.getList()

        val ClassBuilder = ClassData.Builder
        val FeatureBuilder = FeatureData.Builder
        val RaceBuilder = RaceData.Builder
        val CharacterBuilder = CharacterData.Builder

        /* SETTERS */

    }
}
