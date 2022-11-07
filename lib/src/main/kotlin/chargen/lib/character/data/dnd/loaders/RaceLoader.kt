package chargen.lib.character.data.dnd.loaders

import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.races.toEntity
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.ProficiencyType
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.utils.toFeatureData
import chargen.lib.utils.toRaceData
import kotlin.reflect.full.memberProperties

object RaceLoader : Loader<RaceData>() {
    private lateinit var features: List<FeatureData>

    override fun validateDB(forceReload: Boolean): Boolean {
        loadQueries()
        if (forceReload) return false

        val currentRaces: List<RaceData> = queries.getAllRaces().executeAsList().map { it.toRaceData() }

        if (currentRaces.isEmpty()) return false
        return true
    }

    override fun loadEntries() {
        features = queries.getAllFeatures().executeAsList().map { it.toFeatureData() }

        for (prop in BaseRaces::class.memberProperties) {
            prop.get(BaseRaces)?.let {
                val race = it as () -> RaceData
                dataSet.add(race())
            }
        }
    }

    override fun load(forceReload: Boolean) {
        if (validateDB(forceReload)) return
        loadEntries()

        queries.transaction {
            dataSet.forEach { race ->
                val existingRace = queries.getRaceDataByName(race.name).executeAsOneOrNull()

                if (existingRace == null) {
                    queries.registerRaceData(race.toEntity())
                }
            }
        }
        dataSet.clear() //No need to keep them all in memory once they are inserted into the DB
    }

    private fun getFeatureByName(name: String): FeatureData? {
        return features.find { it.name.lowercase() == name.lowercase() }
    }

    object BaseRaces {
        val Elf: () -> RaceData = {
            val feyAncestry = getFeatureByName("Fey Ancestry")!!
            val trance = getFeatureByName("Trance")!!
            val darkvision = getFeatureByName("Darkvision")!!

            RaceData(
                id = 0L,
                name = "Elf",
                raceNamePlural = "Elves",
                description = "Elves are a magical people of otherworldly grace, living in the world but not entirely part of it. They live in places of ethereal beauty, in the midst of ancient forests or in silvery spires glittering with faerie light, where soft music drifts through the air and gentle fragrances waft on the breeze. Elves love nature and magic, art and artistry, music and poetry, and the good things of the world.",
                statMods = mutableMapOf(
                    Stats.DEX to 2
                ),
                proficiencies = mutableListOf(
                    Proficiency("Common", "Understanding of the Common language", ProficiencyType.LANGUAGE),
                    Proficiency("Elvish", "Understanding of the Elvish language", ProficiencyType.LANGUAGE),
                    Proficiency("Perception", "Proficiency with the Perception skill", ProficiencyType.SKILL)
                ),
                features = mutableListOf(
                    feyAncestry.id,
                    trance.id,
                    darkvision.id
                )
            )
        }

        val Human: () -> RaceData = {
            RaceData(
                id = 0L,
                name = "Human",
                raceNamePlural = "Humans",
                description = "In the reckonings of most worlds, humans are the youngest of the common races, late to arrive on the world scene and short-lived in comparison to dwarves, elves, and dragons. Perhaps it is because of their shorter lives that they strive to achieve as much as they can in the years they are given. Or maybe they feel they have something to prove to the elder races, and that’s why they build their mighty empires on the foundation of conquest and trade. Whatever drives them, humans are the innovators, the achievers, and the pioneers of the worlds.",
                statMods = mutableMapOf(
                    Stats.STR to 1,
                    Stats.DEX to 1,
                    Stats.CON to 1,
                    Stats.INT to 1,
                    Stats.WIS to 1,
                    Stats.CHA to 1
                ),
                proficiencies = mutableListOf(
                    Proficiency("Common", "Understanding of the Common language", ProficiencyType.LANGUAGE)
                ),
                features = mutableListOf() //TODO: Update to reference features once they are loaded
            )
        }

        val HalfOrc: () -> RaceData = {
            RaceData(
                id = 0L,
                name = "Half-Orc",
                raceNamePlural = "Half-Orcs",
                description = "When alliances between humans and orcs are sealed by marriages, half-orcs are born. Some half-orcs rise to become proud chiefs of orc tribes, their human blood giving them an edge over their full-blooded orc rivals. Some venture into the world to prove their worth among humans and other more civilized races. Many of these become adventurers, achieving greatness for their mighty deeds and notoriety for their barbaric customs and savage fury.",
                statMods = mutableMapOf(
                    Stats.STR to 2,
                    Stats.CON to 1,
                ),
                proficiencies = mutableListOf(
                    Proficiency("Common", "Understanding of the Common language", ProficiencyType.LANGUAGE),
                    Proficiency("Orcish", "Understanding of the Orcish language", ProficiencyType.LANGUAGE),
                    Proficiency("Intimidation", "The ability to intimidate someone", ProficiencyType.SKILL)
                ),
                features = mutableListOf() //TODO: Update to reference features once they are loaded
            )
        }

        val Dwarf: () -> RaceData = {
            RaceData(
                id = 0L,
                name = "Dwarf",
                raceNamePlural = "Dwarves",
                description = "Kingdoms rich in ancient grandeur, halls carved into the roots of mountains, the echoing of picks and hammers in deep mines and blazing forges, a commitment to clan and tradition, and a burning hatred of goblins and orcs – these common threads unite all dwarves.",
                statMods = mutableMapOf(
                    Stats.CON to 2,
                ),
                proficiencies = mutableListOf(
                    Proficiency("Common", "Understanding of the Common language", ProficiencyType.LANGUAGE),
                    Proficiency("Dwarvish", "Understanding of the Dwarven language", ProficiencyType.LANGUAGE),
                    Proficiency("Battleaxe", "Proficiency with the use of Battleaxes", ProficiencyType.WEAPON),
                    Proficiency("Handaxe", "Proficiency with the use of Handaxes", ProficiencyType.WEAPON),
                    Proficiency("Light Hammer", "Proficiency with the use of Light Hammers", ProficiencyType.WEAPON),
                    Proficiency("Warhammer", "Proficiency with the use of Warhammers", ProficiencyType.WEAPON)
                ),
                features = mutableListOf() //TODO: Update to reference features once they are loaded
            )
        }

        val Gnome: () -> RaceData = {
            RaceData(
                id = 0L,
                name = "Gnome",
                raceNamePlural = "Gnomes",
                description = "A constant hum of busy activity pervades the warrens and neighborhoods where gnomes form their close-knit communities. Louder sounds punctuate the hum: a crunch of grinding gears here, a minor explosion there, a yelp of surprise or triumph, and especially bursts of laughter. Gnomes take delight in life, enjoying every moment of invention, exploration, investigation, creation, and play.",
                statMods = mutableMapOf(
                    Stats.INT to 2,
                ),
                proficiencies = mutableListOf(
                    Proficiency("Common", "Understanding of the Common language", ProficiencyType.LANGUAGE),
                    Proficiency("Gnomish", "Understanding of the Gnomish language", ProficiencyType.LANGUAGE)
                ),
                features = mutableListOf() //TODO: Update to reference features once they are loaded
            )
        }
    }
}