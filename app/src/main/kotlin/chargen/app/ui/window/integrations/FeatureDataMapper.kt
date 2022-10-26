package chargen.app.ui.window.integrations

import chargen.app.ui.window.FeatureEdit
import chargen.app.ui.window.FeatureMain
import chargen.lib.database.stores.features.FeatureEditDataStore
import chargen.lib.database.stores.features.FeatureMainDataStore

val featureMainStateToModel: (FeatureMainDataStore.State) -> FeatureMain.Model = {
    FeatureMain.Model(
        items = it.items,
        selected = it.selected
    )
}

val featureEditStateToModel: (FeatureEditDataStore.State) -> FeatureEdit.Model = {
    FeatureEdit.Model(
        name = it.name,
        description = it.description,
        levelGained = it.levelGained,
        group = it.group,
        requiredFeatures = it.requiredFeatures,
        value = it.value,
        featureType = it.featureType,
        stat = it.stat,
        sourceStat = it.sourceStat,
        spellSlots = it.spellSlots
    )
}