package chargen.lib.database.stores.skills

import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.stores.skills.SkillMainDataStore.Intent
import chargen.lib.database.stores.skills.SkillMainDataStore.State
import com.arkivanov.mvikotlin.core.store.Store

interface SkillMainDataStore: Store<Intent, State, Nothing> {

    sealed class Intent {
        data class DeleteSkill(val id: Long): Intent()
        object AddSkill: Intent()
    }

    data class State(
        val items: List<SkillData> = emptyList(),
        val selected: SkillData = SkillData(
            0L,
            "Skill Name",
            "Skill Description",
            Stats.NONE,
            true
        )
    )
}