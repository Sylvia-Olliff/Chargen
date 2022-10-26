package chargen.lib.database.stores.classes

import chargen.lib.character.data.dnd.classes.CasterClassData
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.DiceType
import chargen.lib.database.ClassRepository
import chargen.lib.utils.*
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.blockingGet
import com.badoo.reaktive.maybe.map

class ClassEditDataStoreDatabase: ClassEditDataStoreProvider.Database {
    override fun load(id: Long): Maybe<ClassData> =
        ClassRepository.select(id)
            .map { it.toClassData() }

    override fun loadFeature(id: Long): Maybe<FeatureData> {
        TODO("Not yet implemented")
    }

    override fun setName(id: Long, name: String): Completable =
        ClassRepository.updateName(id, name)

    override fun setIsCasterFlag(id: Long, value: Boolean): Completable =
        ClassRepository.updateIsCasterFlag(id, value)

    override fun setHitDie(id: Long, hitDie: DiceType): Completable =
        ClassRepository.updateHitDie(id, hitDie)

    override fun setAttacks(id: Long, numAttacks: Int): Completable =
        ClassRepository.updateNumAttacks(id, numAttacks)

    override fun addFeature(id: Long, feature: FeatureData): Completable {
        val currentFeatures = ClassRepository.selectFeatures(id).blockingGet()
        return if (currentFeatures?.features != null) {
            val newFeatures = currentFeatures.features.toMutableList()
            newFeatures.add(feature.id)
            ClassRepository.updateFeatures(id, newFeatures)
        } else {
            ClassRepository.updateFeatures(id, mutableListOf(feature.id))
        }
    }

    override fun addProficiency(id: Long, proficiency: Proficiency): Completable {
        val currentProficiencies = ClassRepository.selectProficiencies(id).blockingGet()
        return if (currentProficiencies?.proficiencies != null) {
            val newProficiencies = currentProficiencies.proficiencies.toMutableList()
            newProficiencies.add(proficiency)
            ClassRepository.updateProficiencies(id, newProficiencies)
        } else {
            ClassRepository.updateProficiencies(id, mutableListOf(proficiency))
        }
    }

    override fun removeFeature(id: Long, feature: Long): Completable =
        ClassRepository.removeFeature(id, feature)

    override fun removeProficiency(id: Long, proficiency: Proficiency): Completable =
        ClassRepository.removeProficiency(id, proficiency)

    override fun setResource(id: Long, resourceName: String, resource: Int): Completable =
        ClassRepository.updateResources(id, resource, resourceName)

    override fun setCasterData(id: Long, casterData: CasterClassData): Completable =
        ClassRepository.updateCasterData(id, casterData)
}