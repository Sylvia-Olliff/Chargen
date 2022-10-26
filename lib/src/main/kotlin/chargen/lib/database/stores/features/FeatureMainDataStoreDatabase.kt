package chargen.lib.database.stores.features

import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.features.toEntity
import chargen.lib.database.FeatureRepository
import chargen.lib.utils.toFeatureData
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapIterable

class FeatureMainDataStoreDatabase: FeatureMainDataStoreProvider.Database {
    override val updates: Observable<List<FeatureData>> =
        FeatureRepository.observeAll()
            .mapIterable { it.toFeatureData() }

    override fun addFeature(item: FeatureData): Completable =
        FeatureRepository.add(item.toEntity())

    override fun deleteFeature(id: Long): Completable =
        FeatureRepository.delete(id)
}