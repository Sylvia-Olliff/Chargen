package chargen.app.ui.window

import chargen.app.ui.window.components.ClassMainComponent
import chargen.app.ui.window.components.FeatureMainComponent
import chargen.app.ui.window.components.RaceMainComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface DataEdit {
    val childStack: Value<ChildStack<*, Child>>

    fun onClassClicked()
    fun onFeatureClicked()
    fun onRaceClicked()
    sealed class Child {
        class ClassChild(val component: ClassMainComponent): Child()
        class FeatureChild(val component: FeatureMainComponent): Child()
        class RaceChild(val component: RaceMainComponent): Child()
    }
}
