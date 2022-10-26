package chargen.lib.database.stores.features

import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.types.FeatureType
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.stores.features.FeatureEditDataStore.Intent
import chargen.lib.database.stores.features.FeatureEditDataStore.State
import chargen.lib.database.stores.features.FeatureEditDataStore.Label
import com.arkivanov.mvikotlin.core.store.Store

interface FeatureEditDataStore: Store<Intent, State, Label> {

    sealed class Intent {
        data class UpdateName(val name: String): Intent()
        data class UpdateLevelGained(val level: Int): Intent()
        data class UpdateDescription(val description: String): Intent()
        data class UpdateGroup(val group: String): Intent()
        data class AddRequiredFeature(val id: Long): Intent()
        data class RemoveRequiredFeature(val id: Long): Intent()
        data class UpdateFeatureType(val featureType: FeatureType): Intent()
        data class SetValue(val value: Int): Intent()
        data class SetStat(val stat: Stats?): Intent()
        data class SetSourceStat(val sourceStat: Stats?): Intent()
        data class SetSpellSlots(val spellSlots: MutableMap<Int, MutableMap<Int, Int>>): Intent()
    }

    data class State(
        var name: String = "Feature Name",
        var levelGained: Int = 1,
        var description: String = "Feature Description",
        var group: String = "UNIVERSAL",
        var requiredFeatures: MutableList<Long> = mutableListOf(),
        var featureType: FeatureType = FeatureType.NEW_ABILITY,
        var value: Int? = null,
        var stat: Stats? = null,
        var sourceStat: Stats? = null,
        var spellSlots: MutableMap<Int, MutableMap<Int, Int>>? = null
    )

    sealed class Label {
        data class Changed(val item: FeatureData): Label()
    }
}