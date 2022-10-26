package chargen.app.ui.window.components

import chargen.app.ui.utils.asValue
import chargen.app.ui.utils.getStore
import chargen.app.ui.window.FeatureMain
import chargen.app.ui.window.FeatureMain.Output
import chargen.app.ui.window.FeatureMain.Model
import chargen.app.ui.window.integrations.featureMainStateToModel
import chargen.lib.database.stores.features.FeatureMainDataStore
import chargen.lib.database.stores.features.FeatureMainDataStore.Intent
import chargen.lib.database.stores.features.FeatureMainDataStoreDatabase
import chargen.lib.database.stores.features.FeatureMainDataStoreProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke

class FeatureMainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<Output>
): FeatureMain, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            FeatureMainDataStoreProvider(
                storeFactory = storeFactory,
                database = FeatureMainDataStoreDatabase()
            ).provide()
        }

    override val models: Value<Model> = store.asValue().map(featureMainStateToModel)

    override fun onFeatureSelected(id: Long) {
        output(Output.Selected(id))
    }

    override fun onFeatureAddClicked() {
        store.accept(Intent.AddFeature)
    }

    override fun onFeatureDeleteClicked(id: Long) {
        store.accept(Intent.DeleteFeature(id))
    }
}