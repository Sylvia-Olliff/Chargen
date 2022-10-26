package chargen.lib.database.stores.races

import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.RaceRepository
import chargen.lib.database.stores.races.RaceEditDataStoreProvider.Database
import chargen.lib.utils.toRaceData
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.blockingGet
import com.badoo.reaktive.maybe.map

class RaceEditDataStoreDatabase: Database {
    override fun load(id: Long): Maybe<RaceData> =
        RaceRepository.select(id)
            .map { it.toRaceData() }

    override fun updateName(id: Long, name: String): Completable =
        RaceRepository.updateName(id, name)

    override fun updateNamePlural(id: Long, namePlural: String): Completable =
        RaceRepository.updateNamePlural(id, namePlural)

    override fun updateDescription(id: Long, description: String): Completable =
        RaceRepository.updateDescription(id, description)

    override fun addProficiency(id: Long, proficiency: Proficiency): Completable {
        val proficiencies = RaceRepository.selectProficiencies(id).blockingGet()
        return if (proficiencies != null) {
            val currentProficiencies = proficiencies.toMutableList()
            currentProficiencies.add(proficiency)
            RaceRepository.updateProficiencies(id, currentProficiencies)
        } else {
            RaceRepository.updateProficiencies(id, listOf(proficiency))
        }
    }

    override fun removeProficiency(id: Long, proficiency: Proficiency): Completable {
        val proficiencies = RaceRepository.selectProficiencies(id).blockingGet()
        return if (proficiencies != null) {
            val currentProficiencies = proficiencies.toMutableList()
            currentProficiencies.remove(proficiency)
            RaceRepository.updateProficiencies(id, currentProficiencies)
        } else {
            RaceRepository.updateProficiencies(id, listOf())
        }
    }

    override fun updateStatMods(id: Long, statMods: MutableMap<Stats, Int>): Completable =
        RaceRepository.updateStatMods(id, statMods)

    override fun addFeature(id: Long, featureId: Long): Completable {
        val features = RaceRepository.selectFeatures(id).blockingGet()
        return if (features != null) {
            val currentFeatures = features.toMutableList()
            currentFeatures.add(featureId)
            RaceRepository.updateFeatures(id, currentFeatures)
        } else {
            RaceRepository.updateFeatures(id, listOf(featureId))
        }
    }

    override fun removeFeature(id: Long, featureId: Long): Completable {
        val features = RaceRepository.selectFeatures(id).blockingGet()
        return if (features != null) {
            val currentFeatures = features.toMutableList()
            currentFeatures.remove(featureId)
            RaceRepository.updateFeatures(id, currentFeatures)
        } else {
            RaceRepository.updateFeatures(id, listOf())
        }
    }

}