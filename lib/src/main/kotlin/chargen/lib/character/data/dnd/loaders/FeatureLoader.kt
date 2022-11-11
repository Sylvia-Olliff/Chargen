package chargen.lib.character.data.dnd.loaders

import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.types.FeatureType

object FeatureLoader : Loader<FeatureData>() {
    override fun validateDB(forceReload: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun loadEntries() {
        TODO("Not yet implemented")
    }

    override fun load(forceReload: Boolean) {
        TODO("Not yet implemented")
    }

    object BaseFeatures {
        val Trance = {
            FeatureData(
                id = 0L,
                name = "Trance",
                levelGained = 1,
                description = "You don’t need to sleep. Instead, you meditate deeply, remaining semiconscious, for 4 hours a day. (The Common word for such meditation is “trance.”) While meditating, you can dream after a fashion; such dreams are actually mental exercises that have become reflexive through years of practice. After resting in this way, you gain the same benefit that a human does from 8 hours of sleep.",
                group = "RACIAL",
                requiredFeatures = mutableListOf(),
                featureType = FeatureType.NEW_ABILITY,
                value = null,
                stat = null,
                sourceStat = null,
                spellSlots = null
            )
        }

        val Darkvision = {
            FeatureData(
                id = 0L,
                name = "Darkvision",
                levelGained = 1,
                description = "",
                group = "RACIAL",
                requiredFeatures = mutableListOf(),
                featureType = FeatureType.NEW_ABILITY,
                value = null,
                stat = null,
                sourceStat = null,
                spellSlots = null
            )
        }

        val FeyAncestry = {
            FeatureData(
                id = 0L,
                name = "Fey Ancestry",
                levelGained = 1,
                description = "You have advantage on saving throws against being charmed, and magic can’t put you to sleep.",
                group = "RACIAL",
                requiredFeatures = mutableListOf(),
                featureType = FeatureType.NEW_ABILITY,
                value = null,
                stat = null,
                sourceStat = null,
                spellSlots = null
            )
        }

        val Stonecunning = {
            FeatureData(
                id = 0L,
                name = "Stonecunning",
                levelGained = 1,
                description = "Whenever you make an Intelligence (History) check related to the origin of stonework, you are considered proficient in the History skill and add double your proficiency bonus to the check, instead of your normal proficiency bonus.",
                group = "RACIAL",
                requiredFeatures = mutableListOf(),
                featureType = FeatureType.NEW_ABILITY,
                value = null,
                stat = null,
                sourceStat = null,
                spellSlots = null
            )
        }
    }
}