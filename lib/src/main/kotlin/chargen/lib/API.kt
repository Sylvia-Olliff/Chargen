package chargen.lib

import chargen.lib.character.data.dnd.utils.Utils
import chargen.lib.config.Config
import chargen.lib.database.*
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.createDirectories

@kotlinx.serialization.ExperimentalSerializationApi
class API {
    companion object {
        private fun checkForDataDirectory() {
            val config = Config.getConfig()
            val directory = config.registry.dataLocation
            val path = Paths.get(directory)

            if (!Files.isDirectory(path)) {
                path.createDirectories()
            }
        }

        private fun checkForConfigFile() {

        }
        /**
         * WARNING: This will wipe any unsaved data
         */
        fun loadData() {
            Config.loadConfig()
            checkForDataDirectory()

            ClassRepository.loadRepo()
            FeatureRepository.loadRepo()
            RaceRepository.loadRepo()
            SkillRepository.loadRepo()
            CharacterRepository.loadRepo()
        }

        /* GETTERS */


        fun getAbilityModifier(statValue: Int) = Utils.convertAttributeToModifier(statValue)


        /* MANAGEMENT */

    }
}
