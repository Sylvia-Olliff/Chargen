package chargen.app.ui.window.components

import chargen.app.ui.utils.asValue
import chargen.app.ui.window.ClassMain
import chargen.app.ui.window.ClassMain.Output
import chargen.app.ui.window.ClassMain.Model
import chargen.app.ui.window.integrations.classMainStateToModel
import chargen.lib.database.stores.classes.ClassMainDataStore.Intent
import chargen.lib.database.stores.classes.ClassMainDataStoreDatabase
import chargen.lib.database.stores.classes.ClassMainDataStoreProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke

class ClassMainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<Output>
): ClassMain, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            ClassMainDataStoreProvider(
                storeFactory = storeFactory,
                database = ClassMainDataStoreDatabase()
            ).provide()
        }

    override val models: Value<Model> = store.asValue().map(classMainStateToModel)

    override fun onClassSelected(id: Long) {
        output(Output.Selected(id))
    }

    override fun onClassAddedClicked() {
        store.accept(Intent.AddClass)
    }

    override fun onClassDeletedClicked(id: Long) {
        store.accept(Intent.DeleteClass(id))
    }
}