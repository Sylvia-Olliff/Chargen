package chargen.app.ui.window.components

import chargen.app.ui.utils.asValue
import chargen.app.ui.window.RaceMain
import chargen.app.ui.window.RaceMain.Output
import chargen.app.ui.window.integrations.raceMainStateToModel
import chargen.lib.database.stores.races.RaceMainDataStore.Intent
import chargen.lib.database.stores.races.RaceMainDataStoreDatabase
import chargen.lib.database.stores.races.RaceMainDataStoreProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke

class RaceMainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<Output>
): RaceMain, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            RaceMainDataStoreProvider(
                storeFactory = storeFactory,
                database = RaceMainDataStoreDatabase()
            ).provide()
        }

    override val models: Value<RaceMain.Model> = store.asValue().map(raceMainStateToModel)

    override fun onRaceSelected(id: Long) {
        output(Output.Selected(id))
    }

    override fun onRaceAddClicked() {
        store.accept(Intent.AddRace)
    }

    override fun onRaceDeleteClicked(id: Long) {
        store.accept(Intent.DeleteRace(id))
    }
}