package chargen.lib.database.stores.races

import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.races.toEntity
import chargen.lib.database.RaceRepository
import chargen.lib.utils.toRaceData
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapIterable

class RaceMainDataStoreDatabase: RaceMainDataStoreProvider.Database {
    override val updates: Observable<List<RaceData>> =
        RaceRepository.observeAll()
            .mapIterable { it.toRaceData() }

    override fun addRace(item: RaceData): Completable =
        RaceRepository.add(item.toEntity())

    override fun deleteRace(id: Long): Completable =
        RaceRepository.delete(id)
}