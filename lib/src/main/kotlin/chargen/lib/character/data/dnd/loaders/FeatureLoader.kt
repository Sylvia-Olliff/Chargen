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

    private fun resolveFeatureIdFromName(name: String): Long {

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
    }
}