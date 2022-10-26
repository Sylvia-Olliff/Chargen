package chargen.lib.database.stores.skills

import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.stores.skills.SkillEditDataStore.Label
import chargen.lib.database.stores.skills.SkillEditDataStore.Intent
import chargen.lib.database.stores.skills.SkillEditDataStore.State
import com.arkivanov.mvikotlin.core.store.Store

interface SkillEditDataStore: Store<Intent, State, Label> {

    sealed class Intent {
        data class UpdateName(val name: String): Intent()
        data class UpdateStat(val stat: Stats): Intent()
        data class UpdateDescription(val description: String): Intent()
        data class UpdateUntrained(val untrained: Boolean): Intent()
    }

    data class State(
        var name: String = "Skill Name",
        var description: String = "Skill Description",
        var stat: Stats = Stats.NONE,
        var untrained: Boolean = true
    )

    sealed class Label {
        data class Changed(val item: SkillData): Label()
    }
}