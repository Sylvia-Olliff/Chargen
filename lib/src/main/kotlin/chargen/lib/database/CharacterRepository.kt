package chargen.lib.database

import chargen.database.CharacterDataEntity
import chargen.database.ChargenDatabaseQueries
import chargen.lib.character.data.dnd.Characteristics
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.types.Alignment
import chargen.lib.character.data.dnd.types.Stats
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.mapNotNull

class CharacterRepository {
    companion object : Repository<CharacterDataEntity>() {
        override fun observeAll(): Observable<List<CharacterDataEntity>> =
            query(ChargenDatabaseQueries::getAllCharacters)
                .observe { it.executeAsList() }

        override fun select(id: Long): Maybe<CharacterDataEntity> =
            query { it.getCharacterData(id) }
                .mapNotNull { it.executeAsOneOrNull() }

        fun selectCurrentFeatures(id: Long): Maybe<List<Long>> =
            query { it.getCharacterCurrentFeatures(id) }
                .mapNotNull { it.executeAsOneOrNull() }

        override fun delete(id: Long): Completable =
            execute { it.deleteCharacter(id) }

        override fun clear(): Completable =
            execute { it.clearCharacters() }

        override fun add(entity: CharacterDataEntity): Completable =
            execute { it.registerCharacter(entity) }

        fun updatePlayerName(name: String, id: Long): Completable =
            execute { it.updateCharacterPlayerName(name, id) }

        fun updateCharacterName(name: String, id: Long): Completable =
            execute { it.updateCharacterName(name, id) }

        fun updateCampaignName(name: String, id: Long): Completable =
            execute { it.updateCampaignName(name, id) }

        fun updateStat(stat: Stats, value: Int, id: Long): Completable =
            execute {
                val stats = it.getCharacterStats(id).executeAsOneOrNull()
                if (stats != null) {
                    stats[stat] = value
                    it.updateCharacterStats(stats, id)
                }
            }

        fun addFeature(featureId: Long, id: Long): Completable =
            execute {
                var currentFeatures = it.getCharacterCurrentFeatures(id).executeAsOneOrNull()
                if (currentFeatures != null) {
                    currentFeatures.add(featureId)
                } else {
                    currentFeatures = mutableListOf(featureId)
                }
                it.updateCharacterCurrentFeatures(currentFeatures, id)
            }

        fun removeFeature(featureId: Long, id: Long): Completable =
            execute {
                val currentFeatures = it.getCharacterCurrentFeatures(id).executeAsOneOrNull()
                if (currentFeatures != null) {
                    currentFeatures.remove(featureId)
                    it.updateCharacterCurrentFeatures(currentFeatures, id)
                }
            }

        fun updateRaceData(raceId: Long, id: Long): Completable =
            execute { it.updateCharacterRaceId(raceId, id) }

        fun updateClassData(classId: Long, id: Long): Completable =
            execute { it.updateCharacterClassId(classId, id) }

        fun addOrUpdateSkill(skillId: Long, isProficient: Boolean, id: Long): Completable =
            execute {
                var skillIds = it.getCharacterSkills(id).executeAsOneOrNull()
                if (skillIds != null) {
                    skillIds[skillId] = isProficient
                } else {
                    skillIds = mutableMapOf(
                        skillId to isProficient
                    )
                }
                it.updateCharacterSkills(skillIds, id)
            }

        fun removeSkill(skillId: Long, id: Long): Completable =
            execute {
                val skillIds = it.getCharacterSkills(id).executeAsOneOrNull()
                if (skillIds != null) {
                    skillIds.remove(skillId)
                    it.updateCharacterSkills(skillIds, id)
                }
            }

        fun updateAlignment(alignment: Alignment, id: Long): Completable =
            execute { it.updateCharacterAlignment(alignment, id) }

        fun updateBackground(background: String, id: Long): Completable =
            execute { it.updateCharacterBackground(background, id) }

        fun addAbility(ability: String, id: Long): Completable =
            execute {
                var abilities = it.getCharacterAbilities(id).executeAsOneOrNull()
                if (abilities != null) {
                    abilities.add(ability)
                } else {
                    abilities = mutableListOf(ability)
                }
                it.updateCharacterAbilities(abilities, id)
            }

        fun removeAbility(ability: String, id: Long): Completable =
            execute {
                val abilities = it.getCharacterAbilities(id).executeAsOneOrNull()
                if (abilities != null) {
                    abilities.remove(ability)
                    it.updateCharacterAbilities(abilities, id)
                }
            }

        fun updateEXP(exp: Int, id: Long): Completable =
            execute { it.updateCharacterExp(exp, id) }

        fun updateLevel(level: Int, id: Long): Completable =
            execute { it.updateCharacterLevel(level, id) }

        fun updateCharacteristics(characteristics: Characteristics, id: Long): Completable =
            execute { it.updateCharacterCharacteristics(characteristics, id) }

        fun updateBackstory(backstory: String, id: Long): Completable =
            execute { it.updateCharacterBackstory(backstory, id) }

        fun updateNotes(notes: String, id: Long): Completable =
            execute { it.updateCharacterNotes(notes, id) }

    }
}