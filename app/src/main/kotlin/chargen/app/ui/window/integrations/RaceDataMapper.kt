package chargen.app.ui.window.integrations

import chargen.app.ui.window.RaceEdit
import chargen.app.ui.window.RaceMain
import chargen.lib.database.FeatureRepository
import chargen.lib.database.stores.races.RaceEditDataStore
import chargen.lib.database.stores.races.RaceMainDataStore
import chargen.lib.utils.toFeatureData
import com.badoo.reaktive.maybe.blockingGet

val raceMainStateToModel: (RaceMainDataStore.State) -> RaceMain.Model = {
    RaceMain.Model(
        items = it.items,
        selected = it.selected
    )
}

val raceEditStateToModel: (RaceEditDataStore.State) -> RaceEdit.Model = { state ->
    val features = FeatureRepository.selectList(state.features).blockingGet()
    val featureDataList = features?.map { it.toFeatureData() }
    RaceEdit.Model(
        name = state.name,
        namePlural = state.namePlural,
        description = state.description,
        proficiencies = state.proficiencies,
        features = featureDataList?.toMutableList() ?: mutableListOf(),
        statMods = state.statMods
    )
}