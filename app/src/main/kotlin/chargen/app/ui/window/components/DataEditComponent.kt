package chargen.app.ui.window.components

import chargen.app.ui.window.DataEdit
import chargen.lib.API
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.skills.SkillData
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DataEditComponent constructor(
    componentContext: ComponentContext
): DataEdit, ComponentContext by componentContext {
    override val childStack: Value<ChildStack<*, DataEdit.Child>>
        get() = TODO("Not yet implemented")

    override fun onClassClicked() {
        TODO("Not yet implemented")
    }

    override fun onFeatureClicked() {
        TODO("Not yet implemented")
    }

    override fun onRaceClicked() {
        TODO("Not yet implemented")
    }


}
