package chargen.lib.database.stores.features

import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.types.FeatureType
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.FeatureRepository
import chargen.lib.database.stores.features.FeatureEditDataStoreProvider.Database
import chargen.lib.utils.toFeatureData
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.blockingGet
import com.badoo.reaktive.maybe.map

class FeatureEditDataStoreDatabase: Database {
    override fun load(id: Long): Maybe<FeatureData> =
        FeatureRepository.select(id)
            .map { it.toFeatureData() }

    override fun updateName(id: Long, name: String): Completable =
        FeatureRepository.updateName(id, name)

    override fun updateDescription(id: Long, description: String): Completable =
        FeatureRepository.updateDescription(id, description)

    override fun updateLevelGained(id: Long, level: Int): Completable =
        FeatureRepository.updateLevelGained(id, level)

    override fun updateGroup(id: Long, group: String): Completable =
        FeatureRepository.updateGroupName(id, group)

    override fun addRequiredFeature(id: Long, featureId: Long): Completable {
        val requiredFeatures = FeatureRepository.selectRequiredFeatures(id).blockingGet()
        val currentFeatures: MutableList<Long>
        if (requiredFeatures != null) {
            currentFeatures = requiredFeatures.toMutableList()
            currentFeatures.add(featureId)
        } else {
            currentFeatures = mutableListOf(featureId)
        }
        return FeatureRepository.updateRequiredFeatures(id, currentFeatures)
    }

    override fun removeRequiredFeature(id: Long, featureId: Long): Completable {
        val requiredFeatures = FeatureRepository.selectRequiredFeatures(id).blockingGet()
        val currentFeatures: MutableList<Long>
        if (requiredFeatures != null) {
            currentFeatures = requiredFeatures.toMutableList()
            currentFeatures.remove(featureId)
        } else {
            currentFeatures = mutableListOf()
        }
        return FeatureRepository.updateRequiredFeatures(id, currentFeatures)
    }

    override fun updateFeatureType(id: Long, featureType: FeatureType): Completable =
        FeatureRepository.updateFeatureType(id,featureType)

    override fun setValue(id: Long, value: Int): Completable =
        FeatureRepository.updateValue(id, value)

    override fun setStat(id: Long, stat: Stats?): Completable =
        FeatureRepository.updateStat(id, stat)

    override fun setSourceStat(id: Long, stat: Stats?): Completable =
        FeatureRepository.updateSourceStat(id, stat)

    override fun updateSpellSlots(id: Long, spellSlots: MutableMap<Int, MutableMap<Int, Int>>): Completable =
        FeatureRepository.updateSpellList(id, spellSlots)

}