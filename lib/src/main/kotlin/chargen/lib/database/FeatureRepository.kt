package chargen.lib.database

import chargen.database.ChargenDatabaseQueries
import chargen.database.FeatureDataEntity
import chargen.lib.character.data.dnd.types.FeatureType
import chargen.lib.character.data.dnd.types.Stats
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.mapNotNull

class FeatureRepository {
    companion object: Repository<FeatureDataEntity>() {

        override fun observeAll(): Observable<List<FeatureDataEntity>> =
            query(ChargenDatabaseQueries::getAllFeatures)
                .observe { it.executeAsList() }

        fun selectAll(): Maybe<List<FeatureDataEntity>> =
            query(ChargenDatabaseQueries::getAllFeatures)
                .mapNotNull { it.executeAsList() }

        override fun select(id: Long): Maybe<FeatureDataEntity> =
            query { it.getFeature(id) }
                .mapNotNull { it.executeAsOneOrNull() }

        fun selectList(ids: List<Long>): Maybe<List<FeatureDataEntity>> =
            query { it.getFeatures(ids) }
                .mapNotNull { it.executeAsList() }

        override fun add(entity: FeatureDataEntity): Completable =
            execute { it.registerFeatureData(entity) }

        override fun delete(id: Long): Completable =
            execute { it.deleteFeature(id) }

        override fun clear(): Completable =
            execute { it.clearFeatures() }

        fun updateName(id: Long, name: String): Completable =
            execute { it.updateFeatureName(name, id) }

        fun updateDescription(id: Long, description: String): Completable =
            execute { it.updateFeatureDescription(description, id) }

        fun updateLevelGained(id: Long, level: Int): Completable =
            execute { it.updateFeatureLevelGained(level, id) }

        fun updateGroupName(id: Long, group: String): Completable =
            execute { it.updateFeatureGroupName(group, id) }

        fun updateRequiredFeatures(id: Long, requiredFeatures: List<Long>): Completable =
            execute { it.updateRequiredFeatures(requiredFeatures, id) }

        fun updateFeatureType(id: Long, featureType: FeatureType): Completable =
            execute { it.updateFeatureType(featureType, id) }

        fun updateValue(id: Long, value: Int): Completable =
            execute { it.updateFeatureValue(value, id) }

        fun updateStat(id: Long, stat: Stats?): Completable =
            execute { it.updateFeatureStat(stat, id) }

        fun updateSourceStat(id: Long, stat: Stats?): Completable =
            execute { it.updateFeatureSourceStat(stat, id) }

        fun updateSpellList(id: Long, spellList: MutableMap<Int, MutableMap<Int, Int>>): Completable =
            execute { it.updateFeatureSpellSlots(spellList, id) }

        fun selectByGroup(group: String): Maybe<List<FeatureDataEntity>> =
            query { it.getFeaturesByGroup(group) }
                .mapNotNull { it.executeAsList() }

        fun selectByType(featureType: FeatureType): Maybe<List<FeatureDataEntity>> =
            query { it.getFeaturesByType(featureType) }
                .mapNotNull { it.executeAsList() }

        fun selectRequiredFeatures(id: Long): Maybe<List<Long>> =
            query { it.getFeatureRequiredFeatures(id) }
                .mapNotNull { it.executeAsOneOrNull()?.requiredFeatures }
    }
}