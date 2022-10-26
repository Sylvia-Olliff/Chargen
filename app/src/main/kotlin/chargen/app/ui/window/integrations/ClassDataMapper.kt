package chargen.app.ui.window.integrations

import chargen.app.ui.window.ClassMain
import chargen.app.ui.window.ClassEdit
import chargen.lib.database.stores.classes.ClassMainDataStore
import chargen.lib.database.stores.classes.ClassEditDataStore

val classMainStateToModel: (ClassMainDataStore.State) -> ClassMain.Model = {
    ClassMain.Model(
        items = it.items,
        selected = it.selected
    )
}

val classEditStateToModel: (ClassEditDataStore.State) -> ClassEdit.Model = {
    ClassEdit.Model(
        name = it.name,
        isCaster = it.isCaster,
        hitDie = it.hitDie,
        numAttacks = it.numAttacks,
        features = it.features,
        proficiencies = it.proficiencies,
        resource = it.resource,
        resourceName = it.resourceName,
        casterData = it.casterData
    )
}

