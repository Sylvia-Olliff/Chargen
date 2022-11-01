package chargen.lib.database

import chargen.database.ChargenDatabaseQueries
import chargen.database.ClassDataEntity
import chargen.database.GetFeatureList
import chargen.database.GetProficiencyList
import chargen.lib.character.data.dnd.classes.CasterClassData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.DiceType
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.andThen
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.observable.*
import com.badoo.reaktive.single.*

class ClassRepository {
    companion object: Repository<ClassDataEntity>() {

        override fun observeAll(): Observable<List<ClassDataEntity>> =
            query(ChargenDatabaseQueries::getClassDataAll)
                .observe { it.executeAsList() }

        override fun select(id: Long): Maybe<ClassDataEntity> =
            query { it.getClassData(id) }
                .mapNotNull { it.executeAsOneOrNull() }

        fun selectAll(): Maybe<List<ClassDataEntity>> =
            query(ChargenDatabaseQueries::getClassDataAll)
                .mapNotNull { it.executeAsList() }

        fun selectFeatures(id: Long): Maybe<GetFeatureList> =
            query { it.getFeatureList(id) }
                .mapNotNull { it.executeAsOneOrNull() }

        fun selectProficiencies(id: Long): Maybe<GetProficiencyList> =
            query { it.getProficiencyList(id) }
                .mapNotNull { it.executeAsOneOrNull() }

        fun updateIsCasterFlag(id: Long, value: Boolean): Completable =
            execute { it.updateIsCasterFlag(value, id) }

        fun updateName(id: Long, name: String): Completable =
            execute { it.updateClassName(name, id) }

        fun updateHitDie(id: Long, hitDie: DiceType): Completable =
            execute { it.updateHitDie(hitDie, id) }

        fun updateNumAttacks(id: Long, numAttacks: Int): Completable =
            execute { it.updateNumAttacks(numAttacks, id) }

        fun updateProficiencies(id: Long, profs: List<Proficiency>): Completable =
            execute { it.updateClassProficiencies(profs, id) }

        fun removeProficiency(id: Long, proficiency: Proficiency): Completable =
            execute { chargenDatabaseQueries ->
                val entity = chargenDatabaseQueries.getClassData(id).executeAsOneOrNull()
                if (entity != null) {
                    val proficiencies: MutableList<Proficiency> = (entity.proficiencies?.toMutableList() ?: emptyList()) as MutableList<Proficiency>
                    proficiencies.removeIf { it == proficiency }
                    updateProficiencies(id, proficiencies)
                }
            }

        fun updateFeatures(id: Long, features: List<Long>): Completable =
            execute { it.updateClassFeatures(features, id) }

        fun removeFeature(id: Long, feature: Long): Completable =
            execute { chargenDatabaseQueries ->
                val entity = chargenDatabaseQueries.getClassData(id).executeAsOneOrNull()
                if (entity != null) {
                    val features: MutableList<Long> = (entity.features?.toMutableList() ?: emptyList()) as MutableList<Long>
                    features.removeIf { it == feature }
                    updateFeatures(id, features.toList())
                }
            }

        fun updateResources(id: Long, resource: Int, resourceName: String): Completable =
            execute { it.updateResources(resource, resourceName, id) }

        fun updateCasterData(id: Long, casterData: CasterClassData): Completable =
            execute { it.updateCasterData(casterData, id) }

        override fun add(entity: ClassDataEntity): Completable =
            execute { it.registerClassData(entity) }

        override fun delete(id: Long): Completable =
            execute { it.deleteClassData(id) }

        override fun clear(): Completable =
            execute { it.clearClasses() }
    }
}