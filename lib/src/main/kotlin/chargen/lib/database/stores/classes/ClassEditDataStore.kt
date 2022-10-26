package chargen.lib.database.stores.classes

import chargen.lib.character.data.dnd.classes.CasterClassData
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.DiceType
import chargen.lib.database.stores.classes.ClassEditDataStore.Intent
import chargen.lib.database.stores.classes.ClassEditDataStore.State
import chargen.lib.database.stores.classes.ClassEditDataStore.Label
import com.arkivanov.mvikotlin.core.store.Store

interface ClassEditDataStore: Store<Intent, State, Label> {

    sealed class Intent {
        data class UpdateName(val name: String): Intent()
        data class SetHitDie(val hitDie: DiceType): Intent()
        data class SetNumAttacks(val numAttacks: Int): Intent()
        data class AddProficiency(val proficiency: Proficiency): Intent()
        data class UpdateCasterFlag(val value: Boolean): Intent()
        data class AddFeature(val feature: Long): Intent()
        data class RemoveFeature(val feature: Long): Intent()
        data class RemoveProficiency(val proficiency: Proficiency): Intent()
        data class SetResources(val resourceName: String, val resource: Int): Intent()
        data class SetCasterData(val casterData: CasterClassData): Intent()
    }

    data class State(
        var name: String = "Class Name",
        var isCaster: Boolean = false,
        var hitDie: DiceType = DiceType.D6,
        var numAttacks: Int = 1,
        var features: MutableList<FeatureData> = mutableListOf(),
        var proficiencies: MutableList<Proficiency> = mutableListOf(),
        var resourceName: String? = null,
        var resource: Int? = null,
        var casterData: CasterClassData? = null
    )

    sealed class Label {
        data class Changed(val item: ClassData): Label()
    }
}