package chargen.lib.character.data.dnd.utils

import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.config.Config
import chargen.lib.database.SkillRepository
import chargen.lib.utils.toSkillData
import com.badoo.reaktive.maybe.blockingGet

internal class Utils {
    companion object {
        private val levelXpMap: () -> Map<Int, Int> = {
            val config = Config.getConfig()
            mapOf(
                1 to config.dndConfig.levelChart.first,
                2 to config.dndConfig.levelChart.second,
                3 to config.dndConfig.levelChart.third,
                4 to config.dndConfig.levelChart.fourth,
                5 to config.dndConfig.levelChart.fifth,
                6 to config.dndConfig.levelChart.sixth,
                7 to config.dndConfig.levelChart.seventh,
                8 to config.dndConfig.levelChart.eighth,
                9 to config.dndConfig.levelChart.ninth,
                10 to config.dndConfig.levelChart.tenth,
                11 to config.dndConfig.levelChart.eleventh,
                12 to config.dndConfig.levelChart.twelfth,
                13 to config.dndConfig.levelChart.thirteenth,
                14 to config.dndConfig.levelChart.fourteenth,
                15 to config.dndConfig.levelChart.fifteenth,
                16 to config.dndConfig.levelChart.sixteenth,
                17 to config.dndConfig.levelChart.seventeenth,
                18 to config.dndConfig.levelChart.eighteenth,
                19 to config.dndConfig.levelChart.nineteenth,
                20 to config.dndConfig.levelChart.twentieth
            )
        }
        fun convertAttributeToModifier(statValue: Int): Int {
            return (statValue - 10) / 2
        }

        fun getEXPForLevel(level: Int): Int {
            return if (level in 1..20) {
                levelXpMap()[level]!!
            } else 0
        }

        fun getLevelForEXP(exp: Int): Int {
            var level = 0
            levelXpMap().entries.forEach {
                if (it.value < exp) level = it.key
            }
            return level
        }

        fun buildSkillMap(): MutableMap<SkillData, Boolean> {
            val skills = SkillRepository.selectAll().blockingGet()!!
            val map: MutableMap<SkillData, Boolean> = mutableMapOf()
            skills.forEach {
                map[it.toSkillData()] = false
            }
            return map
        }
    }
}
