package chargen.lib.database

import chargen.database.ChargenDatabaseQueries
import chargen.database.SkillDataEntity
import chargen.lib.character.data.dnd.types.Stats
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.mapNotNull

class SkillRepository {
    companion object: Repository<SkillDataEntity>() {

        override fun observeAll(): Observable<List<SkillDataEntity>> =
            query(ChargenDatabaseQueries::getAllSkills)
                .observe { it.executeAsList() }

        fun selectAll(): Maybe<List<SkillDataEntity>> =
            query(ChargenDatabaseQueries::getAllSkills)
                .mapNotNull { it.executeAsList() }

        override fun select(id: Long): Maybe<SkillDataEntity> =
            query { it.getSkillData(id) }
                .mapNotNull { it.executeAsOneOrNull() }

        override fun add(entity: SkillDataEntity): Completable =
            execute { it.registerSkill(entity) }

        override fun delete(id: Long): Completable =
            execute { it.deleteSkill(id) }

        override fun clear(): Completable =
            execute { it.clearSkills() }

        fun updateName(id: Long, name: String): Completable =
            execute { it.updateSkillName(name, id) }

        fun updateStat(id: Long, stat: Stats): Completable =
            execute { it.updateSkillStat(stat, id) }

        fun updateUntrained(id: Long, untrained: Boolean): Completable =
            execute { it.updateSkillUntrained(untrained, id) }

        fun updateDescription(id: Long, description: String): Completable =
            execute { it.updateSkillDescription(description, id) }
    }
}