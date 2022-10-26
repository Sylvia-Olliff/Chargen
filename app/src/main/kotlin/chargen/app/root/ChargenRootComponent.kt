package chargen.app.root

import chargen.app.ui.window.*
import chargen.app.ui.window.components.*
import chargen.app.root.ChargenRoot.Child
import chargen.app.ui.utils.Consumer
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer

class ChargenRootComponent internal constructor(
    componentContext: ComponentContext,
    private val featureMain: (ComponentContext, Consumer<FeatureMain.Output>) -> FeatureMain,
    private val featureEdit: (ComponentContext, itemId: Long, Consumer<FeatureEdit.Output>) -> FeatureEdit,
    private val classMain: (ComponentContext, Consumer<ClassMain.Output>) -> ClassMain,
    private val classEdit: (ComponentContext, itemId: Long, Consumer<ClassEdit.Output>) -> ClassEdit,
    private val skillMain: (ComponentContext, Consumer<SkillMain.Output>) -> SkillMain,
    private val skillEdit: (ComponentContext, itemId: Long, Consumer<SkillEdit.Output>) -> SkillEdit,
    private val raceMain: (ComponentContext, Consumer<RaceMain.Output>) -> RaceMain,
    private val raceEdit: (ComponentContext, itemId: Long, Consumer<RaceEdit.Output>) -> RaceEdit
): ChargenRoot, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory
    ) : this(
        componentContext = componentContext,
        featureMain = { childContext, output ->
            FeatureMainComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = output
            )
        },
        featureEdit = { childContext, itemId, output ->
            FeatureEditComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                itemId = itemId,
                output = output
            )
        },
        classMain = { childContext, output ->
            ClassMainComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = output
            )
        },
        classEdit = { childContext, itemId, output ->
            ClassEditComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                itemId = itemId,
                output = output
            )
        },
        skillMain = { childContext, output ->
            SkillMainComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = output
            )
        },
        skillEdit = { childContext, itemId, output ->
            SkillEditComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                itemId = itemId,
                output = output
            )
        },
        raceMain = { childContext, output ->
            RaceMainComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = output
            )
        },
        raceEdit = { childContext, itemId, output ->
            RaceEditComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                itemId = itemId,
                output = output
            )
        }
    )

    private val navigation = StackNavigation<Config>()

    private val stack = childStack(
        source = navigation,
        initialStack = { listOf(Config.CharacterNew) },
        childFactory = ::child
    )

    override val childStack: Value<ChildStack<*, Child>> = stack

    private fun child(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            Config.CharacterEdit -> TODO()
            Config.CharacterNew -> TODO()
            Config.ClassMain -> Child.ClassMain(classMain(componentContext, Consumer(::onClassMainOutput)))
            Config.FeatureMain -> Child.FeatureMain(featureMain(componentContext, Consumer(::onFeatureMainOutput)))
            Config.RaceMain -> Child.RaceMain(raceMain(componentContext, Consumer(::onRaceMainOutput)))
            Config.SkillMain -> Child.SkillMain(skillMain(componentContext, Consumer(::onSkillMainOutput)))
            is Config.ClassEdit -> Child.ClassEdit(classEdit(componentContext, config.itemId, Consumer(::onClassEditOutput)))
            is Config.FeatureEdit -> Child.FeatureEdit(featureEdit(componentContext, config.itemId, Consumer(::onFeatureEditOutput)))
            is Config.RaceEdit -> Child.RaceEdit(raceEdit(componentContext, config.itemId, Consumer(::onRaceEditOutput)))
            is Config.SkillEdit -> Child.SkillEdit(skillEdit(componentContext, config.itemId, Consumer(::onSkillEditOutput)))
        }

    private fun onFeatureMainOutput(output: FeatureMain.Output): Unit =
        when(output) {
            is FeatureMain.Output.Selected -> navigation.bringToFront(Config.FeatureEdit(output.id))
        }

    private fun onFeatureEditOutput(output: FeatureEdit.Output): Unit =
        when(output) {
            is FeatureEdit.Output.Finished -> navigation.pop()
        }

    private fun onClassMainOutput(output: ClassMain.Output): Unit =
        when (output) {
            is ClassMain.Output.Selected -> navigation.bringToFront(Config.ClassEdit(output.id))
        }

    private fun onClassEditOutput(output: ClassEdit.Output): Unit =
        when(output) {
            is ClassEdit.Output.Finished -> navigation.pop()
        }

    private fun onRaceMainOutput(output: RaceMain.Output): Unit =
        when (output) {
            is RaceMain.Output.Selected -> navigation.bringToFront(Config.RaceEdit(output.id))
        }

    private fun onRaceEditOutput(output: RaceEdit.Output): Unit =
        when (output) {
            is RaceEdit.Output.Finished -> navigation.pop()
        }

    private fun onSkillMainOutput(output: SkillMain.Output): Unit =
        when (output) {
            is SkillMain.Output.Selected -> navigation.bringToFront(Config.SkillEdit(output.id))
        }

    private fun onSkillEditOutput(output: SkillEdit.Output): Unit =
        when (output) {
            is SkillEdit.Output.Finished -> navigation.pop()
        }

    override fun onNewCharacterClicked() {
        navigation.bringToFront(Config.CharacterNew)
    }

    override fun onEditCharacterClicked() {
        navigation.bringToFront(Config.CharacterEdit)
    }

    override fun onFeatureMainClicked() {
        navigation.bringToFront(Config.FeatureMain)
    }

    override fun onClassMainClicked() {
        navigation.bringToFront(Config.ClassMain)
    }

    override fun onRaceMainClicked() {
        navigation.bringToFront(Config.RaceMain)
    }

    override fun onSkillMainClicked() {
        navigation.bringToFront(Config.SkillMain)
    }

    private sealed interface Config : Parcelable {
        @Parcelize
        object CharacterNew: Config
        @Parcelize
        object CharacterEdit : Config
        @Parcelize
        object FeatureMain: Config
        @Parcelize
        data class FeatureEdit(val itemId: Long): Config
        @Parcelize
        object ClassMain: Config
        @Parcelize
        data class ClassEdit(val itemId: Long): Config
        @Parcelize
        object SkillMain: Config
        @Parcelize
        data class SkillEdit(val itemId: Long): Config
        @Parcelize
        object RaceMain: Config
        @Parcelize
        data class RaceEdit(val itemId: Long): Config
    }
}
