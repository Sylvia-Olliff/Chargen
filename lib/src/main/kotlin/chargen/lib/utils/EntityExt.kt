package chargen.lib.utils

import chargen.database.*
import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.database.ClassRepository
import chargen.lib.database.FeatureRepository
import chargen.lib.database.RaceRepository
import chargen.lib.database.SkillRepository
import com.badoo.reaktive.maybe.blockingGet

fun ClassDataEntity.toClassData(): ClassData {
    val featureDataSetIds = resolveList(features)
    val featureDataSet: MutableList<FeatureData> = mutableListOf()

    featureDataSetIds.forEach {
        val featureEntity: FeatureDataEntity? = FeatureRepository.select(it).blockingGet()
        var feature: FeatureData
        if (featureEntity != null) {
            feature = FeatureData(
                id = featureEntity.id,
                name = featureEntity.name,
                levelGained = featureEntity.levelGained,
                description = featureEntity.description,
                group = featureEntity.groupName,
                featureType = featureEntity.featureType,
                requiredFeatures = featureEntity.requiredFeatures?.toMutableList() ?: mutableListOf(),
                value = featureEntity.value_,
                stat = featureEntity.stat,
                sourceStat = featureEntity.sourceStat,
                spellSlots = featureEntity.spellSlots
            )
            featureDataSet.add(feature)
        }
    }
    return ClassData(
        id,
        name,
        isCaster,
        hitDie,
        numAttacks,
        resolveList(proficiencies),
        featureDataSet,
        resourceName,
        resource,
        casterClassData
    )
}

fun FeatureDataEntity.toFeatureData(): FeatureData {
    return FeatureData(
        id,
        name,
        levelGained,
        description,
        groupName,
        resolveList(requiredFeatures),
        featureType,
        value_,
        stat,
        sourceStat,
        spellSlots
    )
}

fun RaceDataEntity.toRaceData(): RaceData {
    return RaceData(
        id = id,
        name = name,
        raceNamePlural = raceNamePlural,
        description = description,
        proficiencies = resolveList(proficiencies),
        statMods = statMods?.toMutableMap() ?: mutableMapOf(),
        features = resolveList(features)
    )
}

fun SkillDataEntity.toSkillData(): SkillData {
    return SkillData(
        id, name, description, stat, untrained
    )
}

fun CharacterDataEntity.toCharacterData(): CharacterData {
    val raceData = raceDataId?.let { RaceRepository.select(it).blockingGet()?.toRaceData() }
    val classData = classDataId?.let { ClassRepository.select(it).blockingGet()?.toClassData() }
    val skillMap: MutableMap<SkillData, Boolean> = mutableMapOf()
    val featureList: MutableList<FeatureData> = mutableListOf()

    skillIds.forEach {
        val skill = SkillRepository.select(it.key).blockingGet()?.toSkillData()
        skillMap[skill!!] = it.value
    }

    currentFeatureIds.forEach {
        val feature = FeatureRepository.select(it).blockingGet()?.toFeatureData()
        featureList.add(feature!!)
    }

    return CharacterData(
        id = id,
        playerName = playerName,
        characterName = characterName,
        campaignName = campaignName,
        stats = stats,
        raceData = raceData,
        classData = classData,
        skills = skillMap,
        alignment = alignment,
        background = background,
        abilities = abilities,
        currentFeatures = featureList,
        EXP = experience,
        level = level,
        characteristics = characteristics,
        backstory = backstory,
        notes = notes
    )
}

private fun <T> resolveList(list: List<T>?): MutableList<T> {
    if (list.isNullOrEmpty()) return mutableListOf()
    return list.toMutableList()
}