package chargen.app.ui.window.integrations

import chargen.app.ui.window.CharacterEdit
import chargen.app.ui.window.CharacterMain
import chargen.lib.database.stores.characters.CharacterEditDataStore
import chargen.lib.database.stores.characters.CharacterMainDataStore

val characterMainStateToModel: (CharacterMainDataStore.State) -> CharacterMain.Model = {
    CharacterMain.Model(
        items = it.items,
        selected = it.selected
    )
}

val characterEditStateToModel: (CharacterEditDataStore.State) -> CharacterEdit.Model = {
    CharacterEdit.Model(
        playerName = it.playerName,
        characterName = it.characterName,
        campaignName = it.campaignName,
        stats = it.stats,
        race = it.raceData,
        clazz = it.classData,
        skills = it.skills,
        alignment = it.alignment,
        background = it.background,
        abilities = it.abilities,
        currentFeatures = it.currentFeatures,
        exp = it.experience,
        level = it.level,
        characteristics = it.characteristics,
        backstory = it.backstory,
        notes = it.notes
    )
}