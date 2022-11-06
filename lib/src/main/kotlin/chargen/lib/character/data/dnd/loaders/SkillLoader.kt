package chargen.lib.character.data.dnd.loaders

import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.skills.toEntity
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.utils.toSkillData
import kotlin.reflect.full.memberProperties

object SkillLoader : Loader<SkillData>() {
    override fun validateDB(forceReload: Boolean): Boolean {
        loadQueries()
        if (forceReload) return false

        val currentSkills: List<SkillData> = queries.getAllSkills().executeAsList().map { it.toSkillData() }

        if (currentSkills.isEmpty()) return false
        return true
    }

    override fun loadEntries() {
        for (prop in BaseSkills::class.memberProperties) {
            prop.get(BaseSkills)?.let {
                val skill = it as SkillData
                dataSet.add(skill)
            }
        }
    }

    override fun load(forceReload: Boolean) {
        if (validateDB(forceReload)) return
        loadEntries()

        queries.transaction {
            dataSet.forEach { skill ->
                queries.registerSkill(skill.toEntity())
            }
        }
    }

    object BaseSkills {
        val Athletics: SkillData = SkillData(
            id = 0L,
            name = "Athletics",
            description = "",
            stat = Stats.STR,
            untrained = true
        )

        val Acrobatics: SkillData = SkillData(
            id = 0L,
            name = "Acrobatics",
            description = "",
            stat = Stats.DEX,
            untrained = true
        )

        val AnimalHandling: SkillData = SkillData(
            id = 0L,
            name = "Animal Handling",
            description = "",
            stat = Stats.WIS,
            untrained = true
        )

        val Arcana: SkillData = SkillData(
            id = 0L,
            name = "Arcana",
            description = "",
            stat = Stats.INT,
            untrained = false
        )

        val Deception: SkillData = SkillData(
            id = 0L,
            name = "Deception",
            description = "",
            stat = Stats.CHA,
            untrained = true
        )

        val History: SkillData = SkillData(
            id = 0L,
            name = "History",
            description = "",
            stat = Stats.INT,
            untrained = true
        )

        val Insight: SkillData = SkillData(
            id = 0L,
            name = "Insight",
            description = "",
            stat = Stats.WIS,
            untrained = true
        )

        val Intimidation: SkillData = SkillData(
            id = 0L,
            name = "Intimidation",
            description = "",
            stat = Stats.CHA,
            untrained = true
        )

        val Investigation: SkillData = SkillData(
            id = 0L,
            name = "Investigation",
            description = "",
            stat = Stats.INT,
            untrained = true
        )

        val Medicine: SkillData = SkillData(
            id = 0L,
            name = "Medicine",
            description = "",
            stat = Stats.WIS,
            untrained = true
        )

        val Nature: SkillData = SkillData(
            id = 0L,
            name = "Nature",
            description = "",
            stat = Stats.INT,
            untrained = true
        )

        val Perception: SkillData = SkillData(
            id = 0L,
            name = "Perception",
            description = "",
            stat = Stats.WIS,
            untrained = true
        )

        val Performance: SkillData = SkillData(
            id = 0L,
            name = "Performance",
            description = "",
            stat = Stats.CHA,
            untrained = true
        )

        val Persuasion: SkillData = SkillData(
            id = 0L,
            name = "Persuasion",
            description = "",
            stat = Stats.CHA,
            untrained = true
        )

        val Religion: SkillData = SkillData(
            id = 0L,
            name = "Religion",
            description = "",
            stat = Stats.INT,
            untrained = true
        )

        val SleightOfHand: SkillData = SkillData(
            id = 0L,
            name = "Slight of Hand",
            description = "",
            stat = Stats.DEX,
            untrained = true
        )

        val Stealth: SkillData = SkillData(
            id = 0L,
            name = "Stealth",
            description = "",
            stat = Stats.DEX,
            untrained = true
        )

        val Survival: SkillData = SkillData(
            id = 0L,
            name = "Survival",
            description = "",
            stat = Stats.WIS,
            untrained = true
        )
    }
}