package chargen.lib.database.stores.characters

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.Characteristics
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.types.Alignment
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.CharacterRepository
import chargen.lib.database.ClassRepository
import chargen.lib.database.FeatureRepository
import chargen.lib.database.RaceRepository
import chargen.lib.utils.toCharacterData
import chargen.lib.utils.toClassData
import chargen.lib.utils.toFeatureData
import chargen.lib.utils.toRaceData
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.map

class CharacterEditDataStoreDatabase: CharacterEditDataStoreProvider.Database {
    override fun load(id: Long): Maybe<CharacterData> =
        CharacterRepository.select(id)
            .map { it.toCharacterData() }

    override fun loadFeature(featureId: Long): Maybe<FeatureData> =
        FeatureRepository.select(featureId)
            .map { it.toFeatureData() }

    override fun loadRace(raceId: Long): Maybe<RaceData> =
        RaceRepository.select(raceId)
            .map { it.toRaceData() }

    override fun loadClass(classId: Long): Maybe<ClassData> =
        ClassRepository.select(classId)
            .map { it.toClassData() }

    override fun setPlayerName(id: Long, name: String): Completable =
        CharacterRepository.updatePlayerName(name, id)

    override fun setCharacterName(id: Long, name: String): Completable =
        CharacterRepository.updateCharacterName(name, id)

    override fun setCampaignName(id: Long, name: String): Completable =
        CharacterRepository.updateCampaignName(name, id)

    override fun setStat(id: Long, stat: Stats, value: Int): Completable =
        CharacterRepository.updateStat(stat, value, id)

    override fun setRace(id: Long, raceId: Long): Completable =
        CharacterRepository.updateRaceData(raceId, id)

    override fun setClass(id: Long, classId: Long): Completable =
        CharacterRepository.updateClassData(classId, id)

    override fun updateSkillIsTrained(id: Long, skillID: Long, isTrained: Boolean): Completable =
        CharacterRepository.addOrUpdateSkill(skillID, isTrained, id)

    override fun removeSkill(id: Long, skillId: Long): Completable =
        CharacterRepository.removeSkill(skillId, id)

    override fun setAlignment(id: Long, alignment: Alignment): Completable =
        CharacterRepository.updateAlignment(alignment, id)

    override fun setBackground(id: Long, background: String): Completable =
        CharacterRepository.updateBackground(background, id)

    override fun addAbility(id: Long, ability: String): Completable =
        CharacterRepository.addAbility(ability, id)

    override fun removeAbility(id: Long, ability: String): Completable =
        CharacterRepository.removeAbility(ability, id)

    override fun addFeature(id: Long, featureId: Long): Completable =
        CharacterRepository.addFeature(featureId, id)

    override fun removeFeature(id: Long, featureId: Long): Completable =
        CharacterRepository.removeFeature(featureId, id)

    override fun setExp(id: Long, exp: Int): Completable =
        CharacterRepository.updateEXP(exp, id)

    override fun setLevel(id: Long, level: Int): Completable =
        CharacterRepository.updateLevel(level, id)

    override fun setCharacteristics(id: Long, characteristics: Characteristics): Completable =
        CharacterRepository.updateCharacteristics(characteristics, id)

    override fun setBackstory(id: Long, backstory: String): Completable =
        CharacterRepository.updateBackstory(backstory, id)

    override fun setNotes(id: Long, notes: String): Completable =
        CharacterRepository.updateNotes(notes, id)
}