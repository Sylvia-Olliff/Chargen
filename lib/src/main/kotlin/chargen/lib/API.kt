package chargen.lib

import chargen.lib.character.data.dnd.utils.Utils
import chargen.lib.config.Config
import chargen.lib.database.ClassRepository
import chargen.lib.database.FeatureRepository
import chargen.lib.database.RaceRepository
import chargen.lib.database.SkillRepository

@kotlinx.serialization.ExperimentalSerializationApi
class API {
    init {
        Config.loadConfig()
    }

    companion object {
        /**
         * WARNING: This will wipe any unsaved data
         */
        fun loadData() {
            ClassRepository.loadRepo()
            FeatureRepository.loadRepo()
            RaceRepository.loadRepo()
            SkillRepository.loadRepo()
        }

        /* GETTERS */


        fun getAbilityModifier(statValue: Int) = Utils.convertAttributeToModifier(statValue)


        /* MANAGEMENT */

    }
}
