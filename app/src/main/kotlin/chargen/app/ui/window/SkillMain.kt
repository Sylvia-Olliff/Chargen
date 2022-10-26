package chargen.app.ui.window

import chargen.lib.character.data.dnd.skills.SkillData
import com.arkivanov.decompose.value.Value

interface SkillMain {
    val models: Value<Model>

    fun onSkillSelected(id: Long)
    fun onSkillAddClicked()
    fun onSkillDeleteClicked(id: Long)

    data class Model(
        val items: List<SkillData>,
        val selected: SkillData
    )

    sealed class Output {
        data class Selected(val id: Long) : Output()
    }
}