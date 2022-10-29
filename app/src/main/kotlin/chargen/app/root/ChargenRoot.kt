package chargen.app.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value


interface ChargenRoot {

    val childStack: Value<ChildStack<*, Child>>

    fun onCharacterMainClicked()
    fun onClassMainClicked()
    fun onFeatureMainClicked()
    fun onRaceMainClicked()
    fun onSkillMainClicked()

    sealed class Child {
        data class CharacterMain(val component: chargen.app.ui.window.CharacterMain): Child()
        data class CharacterEdit(val component: chargen.app.ui.window.CharacterEdit): Child()
        data class ClassMain(val component: chargen.app.ui.window.ClassMain): Child()
        data class ClassEdit(val component: chargen.app.ui.window.ClassEdit): Child()
        data class FeatureMain(val component: chargen.app.ui.window.FeatureMain): Child()
        data class FeatureEdit(val component: chargen.app.ui.window.FeatureEdit): Child()
        data class RaceMain(val component: chargen.app.ui.window.RaceMain): Child()
        data class RaceEdit(val component: chargen.app.ui.window.RaceEdit): Child()
        data class SkillMain(val component: chargen.app.ui.window.SkillMain): Child()
        data class SkillEdit(val component: chargen.app.ui.window.SkillEdit): Child()
    }
}
