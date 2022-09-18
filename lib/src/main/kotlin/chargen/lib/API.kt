package chargen.lib

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.classes.ClassRegistry
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.features.FeatureRegistry
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.races.RaceRegistry
import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.skills.SkillRegistry
import chargen.lib.character.data.dnd.templates.DataEntity
import chargen.lib.character.data.dnd.utils.Utils
import chargen.lib.config.Config
import java.util.*

@kotlinx.serialization.ExperimentalSerializationApi
class API {
    init {
        Config.loadConfig()
    }

    companion object {
        /**
         * WARNING: This will wipe any unsaved data
         */
        suspend fun loadData() {
            ClassRegistry.load()
            RaceRegistry.load()
            FeatureRegistry.load()
            SkillRegistry.load()
        }

        /* GETTERS */
        fun getClasses(): List<ClassData> = ClassRegistry.getList()
        fun getRaces(): List<RaceData> = RaceRegistry.getList()
        fun getFeatures(): List<FeatureData> = FeatureRegistry.getList()
        fun getSkills(): List<SkillData> = SkillRegistry.getList()

        val ClassBuilder = ClassData.Builder
        val FeatureBuilder = FeatureData.Builder
        val RaceBuilder = RaceData.Builder
        val CharacterBuilder = CharacterData.Builder

        fun getAbilityModifier(statValue: Int) = Utils.convertAttributeToModifier(statValue)

        /* SETTERS */
        suspend fun <T : DataEntity> register(item: T) {
            when (item) {
                is ClassData -> ClassRegistry.register(item)
                is RaceData -> RaceRegistry.register(item)
                is FeatureData -> FeatureRegistry.register(item)
                is SkillData -> SkillRegistry.register(item)
            }
        }

        suspend fun <T : DataEntity> delete(item: T) {
            when (item) {
                is ClassData -> ClassRegistry.delete(item.id)
                is RaceData -> RaceRegistry.delete(item.id)
                is FeatureData -> FeatureRegistry.delete(item.id)
                is SkillData -> SkillRegistry.delete(item.id)
            }
        }

        suspend fun delete (id: UUID) {
            if (ClassRegistry.validate(id)) ClassRegistry.delete(id)
            if (RaceRegistry.validate(id)) RaceRegistry.delete(id)
            if (FeatureRegistry.validate(id)) FeatureRegistry.delete(id)
            if (SkillRegistry.validate(id)) SkillRegistry.delete(id)
        }

        /* MANAGEMENT */

    }
}
