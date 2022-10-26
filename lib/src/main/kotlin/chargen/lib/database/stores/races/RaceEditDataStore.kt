package chargen.lib.database.stores.races

import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.stores.races.RaceEditDataStore.Intent
import chargen.lib.database.stores.races.RaceEditDataStore.State
import chargen.lib.database.stores.races.RaceEditDataStore.Label
import com.arkivanov.mvikotlin.core.store.Store

interface RaceEditDataStore: Store<Intent, State, Label> {

    sealed class Intent {
        data class UpdateName(val name: String): Intent()
        data class UpdateNamePlural(val namePlural: String): Intent()
        data class UpdateDescription(val description: String): Intent()
        data class UpdateStatMods(val statMods: MutableMap<Stats, Int>): Intent()
        data class AddProficiency(val proficiency: Proficiency): Intent()
        data class RemoveProficiency(val proficiency: Proficiency): Intent()
        data class AddFeature(val featureId: Long): Intent()
        data class RemoveFeature(val featureId: Long): Intent()
    }

    data class State(
        var name: String = "Race Name",
        var namePlural: String = "Race Name Plural",
        var description: String = "Race Description",
        var statMods: MutableMap<Stats, Int> = mutableMapOf(),
        var proficiencies: MutableList<Proficiency> = mutableListOf(),
        var features: MutableList<Long> = mutableListOf()
    )

    sealed class Label {
        data class Changed(val item: RaceData): Label()
    }
}