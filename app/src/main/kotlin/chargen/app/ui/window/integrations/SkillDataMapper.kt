package chargen.app.ui.window.integrations

import chargen.app.ui.window.SkillEdit
import chargen.app.ui.window.SkillMain
import chargen.lib.database.stores.skills.SkillEditDataStore
import chargen.lib.database.stores.skills.SkillMainDataStore

val skillStateToModel: (SkillMainDataStore.State) -> SkillMain.Model = {
    SkillMain.Model(
        items = it.items,
        selected = it.selected
    )
}

val skillEditStateToModel: (SkillEditDataStore.State) -> (SkillEdit.Model) = {
    SkillEdit.Model(
        name = it.name,
        description = it.description,
        stat = it.stat,
        untrained = it.untrained
    )
}