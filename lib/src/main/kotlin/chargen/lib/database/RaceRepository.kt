package chargen.lib.database

import chargen.database.ChargenDatabaseQueries
import chargen.database.RaceDataEntity
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.Stats
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.mapNotNull

class RaceRepository {
    companion object: Repository<RaceDataEntity>() {

        override fun observeAll(): Observable<List<RaceDataEntity>> =
            query(ChargenDatabaseQueries::getAllRaces)
                .observe { it.executeAsList() }

        override fun select(id: Long): Maybe<RaceDataEntity> =
            query { it.getRaceData(id) }
                .mapNotNull { it.executeAsOneOrNull() }

        override fun add(entity: RaceDataEntity): Completable =
            execute { it.registerRaceData(entity) }

        override fun delete(id: Long): Completable =
            execute { it.deleteRaceData(id) }

        override fun clear(): Completable =
            execute { it.clearRaceData() }

        fun updateName(id: Long, name: String): Completable =
            execute { it.updateRaceName(name, id) }

        fun updateNamePlural(id: Long, name: String): Completable =
            execute { it.updateRaceNamePlural(name, id) }

        fun updateDescription(id: Long, description: String): Completable =
            execute { it.updateRaceDescription(description, id) }

        fun selectProficiencies(id: Long): Maybe<List<Proficiency>> =
            query { it.selectRaceProficiencies(id) }
                .mapNotNull { it.executeAsOneOrNull()?.proficiencies }

        fun selectFeatures(id: Long): Maybe<List<Long>> =
            query { it.selectRaceFeatures(id) }
                .mapNotNull { it.executeAsOneOrNull()?.features }

        fun updateProficiencies(id: Long, proficiencies: List<Proficiency>): Completable =
            execute { it.updateRaceProficiencies(proficiencies, id) }

        fun updateFeatures(id: Long, features: List<Long>): Completable =
            execute { it.updateRaceFeatures(features, id) }

        fun updateStatMods(id: Long, statMods: Map<Stats, Int>): Completable =
            execute { it.updateRaceStatMods(statMods, id) }
    }
}