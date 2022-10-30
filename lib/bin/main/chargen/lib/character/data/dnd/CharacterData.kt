package chargen.lib.character.data.dnd

import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.classes.ClassRegistry
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.features.FeatureRegistry
import chargen.lib.character.data.dnd.races.RaceData
import chargen.lib.character.data.dnd.races.RaceRegistry
import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.skills.SkillRegistry
import chargen.lib.character.data.dnd.templates.DataBuilder
import chargen.lib.character.data.dnd.types.Alignment
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.character.data.dnd.utils.Utils
import chargen.lib.exceptions.KeyNotFoundException
import chargen.lib.exceptions.MissingPropertyException
import chargen.lib.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class CharacterData(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    var playerName: String,
    var characterName: String?,
    var campaignName: String?,
    private val stats: MutableMap<Stats, Int>,
    private var raceData: RaceData?,
    private var classData: ClassData?,
    private var skills: MutableMap<@Serializable(with = UUIDSerializer::class) UUID, Boolean>,
    var alignment: Alignment?,
    var background: String?,
    private val abilities: MutableList<String>,
    private val currentFeatures: MutableList<@Serializable(with = UUIDSerializer::class) UUID>,
    var EXP: Int,
    var level: Int,
    val characteristics: chargen.lib.character.data.dnd.Characteristics,
    var backstory: String?,
    var notes: String?
) {
    fun getDefaultSaveName(): String {
        return when(characterName) {
            null -> playerName
            else -> "$playerName-$characterName"
        }
    }

    fun getStat(stat: Stats): Int {
        return if (stats.containsKey(stat)) stats[stat]!!
        else {
            stats[stat] = 0
            0
        }
    }

    fun setStat(stat: Stats, value: Int) {
        stats[stat] = value
    }

    fun getStatModifier(stat: Stats): Int {
        return Utils.convertAttributeToModifier(stats[stat]!!)
    }

    fun getRaceData(): RaceData? = this.raceData
    suspend fun setRace(id: UUID) {
        if (!RaceRegistry.validate(id)) throw KeyNotFoundException("Unrecognized Race ID, make sure the race you are referring to has been registered")
        this.raceData = RaceRegistry.get(id)
    }

    fun getClassData(): ClassData? = this.classData
    suspend fun setClass(id: UUID) {
        if (!ClassRegistry.validate(id)) throw KeyNotFoundException("Unrecognized Class ID, make sure the class you are referring to has been registered")
        this.classData = ClassRegistry.get(id)
    }

    suspend fun getSkills(): MutableMap<SkillData, Boolean> {
        val data: MutableMap<SkillData, Boolean> = mutableMapOf()
        this.skills.forEach {
            data.put(SkillRegistry.get(it.key), it.value)
        }

        return data
    }
    fun addSkill(id: UUID) {
        if (!SkillRegistry.validate(id)) throw KeyNotFoundException("Unrecognized Skill ID, make sure the skill you are referring to has been registered")
        if (this.skills.containsKey(id)) return
        this.skills[id] = false
    }
    fun setSkillProficiency(id: UUID, isProficient: Boolean) {
        if (!SkillRegistry.validate(id)) throw KeyNotFoundException("Unrecognized Skill ID, make sure the skill you are referring to has been registered")
        this.skills[id] = isProficient
    }

    fun getAbilities(): List<String> = this.abilities
    fun addAbility(ability: String) {
        this.abilities.add(ability)
    }

    suspend fun getCurrentFeatures(): List<FeatureData> {
        val features: MutableList<FeatureData> = mutableListOf()

        this.currentFeatures.forEach {
            features.add(FeatureRegistry.get(it))
        }

        return features
    }

    fun addFeature(id: UUID) {
        if (!FeatureRegistry.validate(id)) throw KeyNotFoundException("Feature must first be registered before it can be added to a character")

        this.currentFeatures.add(id)
    }

    fun removeFeature(id: UUID) {
        if (this.currentFeatures.contains(id)) this.currentFeatures.remove(id)
    }

    suspend fun update() {
        if (this.classData != null) {
            this.classData = ClassRegistry.get(this.classData!!.id)
            updateClass()
        }
        if (this.raceData != null) {
            this.raceData = RaceRegistry.get(this.raceData!!.id)
            updateRace()
        }
        if (this.skills.isNotEmpty()) {
            //TODO: Update which skills the character has proficiency in
        }
    }

    private suspend fun updateRace() {
        this.raceData?.features?.forEach {
            val feature = FeatureRegistry.get(it)
            if (feature.levelGained <= this.level) {
                var qualified = true
                if (feature.requiredFeatures.isNotEmpty()) {
                    feature.requiredFeatures.forEach {
                        if (!this.currentFeatures.contains(it)) qualified = false
                    }
                }
                if (qualified) this.currentFeatures.add(feature.id)
            }
        }
    }

    private suspend fun updateClass() {
        this.classData?.features?.forEach {
            val feature = FeatureRegistry.get(it)
            if (feature.levelGained <= this.level) {
                var qualified = true
                if (feature.requiredFeatures.isNotEmpty()) {
                    feature.requiredFeatures.forEach {
                        if (!this.currentFeatures.contains(it)) qualified = false
                    }
                }
                if (qualified) this.currentFeatures.add(feature.id)
            }
        }
    }

    companion object Builder: DataBuilder<chargen.lib.character.data.dnd.CharacterData> {
        private var playerName: String? = null
        private var characterName: String? = null
        private var campaignName: String? = null
        private var stats: MutableMap<Stats, Int> = mutableMapOf()
        private var raceData: RaceData? = null
        private var classData: ClassData? = null
        private var skills: MutableMap<UUID, Boolean> = mutableMapOf()
        private var alignment: Alignment? = null
        private var background: String? = null
        private var abilities: MutableList<String> = mutableListOf()
        private var currentFeatures: MutableList<UUID> = mutableListOf()
        private var exp: Int? = null
        private var level: Int = 1
        private var characteristics: chargen.lib.character.data.dnd.Characteristics =
            chargen.lib.character.data.dnd.Characteristics(null, null, null, null, null, null)
        private var backstory: String? = null
        private var notes: String? = null

        fun withPlayerName(name: String): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.playerName = name
            return this
        }

        fun withCharacterName(name: String): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.characterName = name
            return this
        }

        fun withCampaignName(name: String): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.campaignName = name
            return this
        }

        fun withStats(set: Map<Stats, Int>): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.stats.clear()
            set.forEach {
                chargen.lib.character.data.dnd.CharacterData.Builder.stats[it.key] = it.value
            }
            return this
        }

        fun withRaceData(data: RaceData): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.raceData = data
            return this
        }

        fun withClassData(data: ClassData): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.classData = data
            return this
        }

        fun withAlignment(alignment: Alignment): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.alignment = alignment
            return this
        }

        fun withBackground(background: String): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.background = background
            return this
        }

        fun withAbilities(abilities: MutableList<String>): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.abilities = abilities
            return this
        }

        fun withFeatures(features: List<UUID>): chargen.lib.character.data.dnd.CharacterData.Builder {
            features.forEach {
                chargen.lib.character.data.dnd.CharacterData.Builder.currentFeatures.add(it)
            }
            return this
        }

        fun withEXP(exp: Int): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.exp = exp
            return this
        }

        fun atLevel(level: Int): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.level = level
            //TODO: Set EXP to be the minimum for the provided level if it is higher than current EXP
            return this
        }

        fun withCharacteristics(characteristics: chargen.lib.character.data.dnd.Characteristics): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.characteristics = characteristics
            return this
        }

        fun withBackstory(backstory: String): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.backstory = backstory
            return this
        }

        fun withNotes(notes: String): chargen.lib.character.data.dnd.CharacterData.Builder {
            chargen.lib.character.data.dnd.CharacterData.Builder.notes = notes
            return this
        }

        override fun build(): chargen.lib.character.data.dnd.CharacterData {
            if (chargen.lib.character.data.dnd.CharacterData.Builder.playerName == null) throw MissingPropertyException("Player name is required when creating new character")

            if (chargen.lib.character.data.dnd.CharacterData.Builder.exp == null) chargen.lib.character.data.dnd.CharacterData.Builder.exp = 0

            // Make sure that the stats all have an entry
            Stats.values().forEach {
                if (!chargen.lib.character.data.dnd.CharacterData.Builder.stats.containsKey(it)) chargen.lib.character.data.dnd.CharacterData.Builder.stats[it] = 0
            }

            SkillRegistry.getList().forEach {
                if (it.untrained) chargen.lib.character.data.dnd.CharacterData.Builder.skills[it.id] = false
            }

            val data = chargen.lib.character.data.dnd.CharacterData(
                UUID.randomUUID(),
                chargen.lib.character.data.dnd.CharacterData.Builder.playerName!!,
                chargen.lib.character.data.dnd.CharacterData.Builder.characterName,
                chargen.lib.character.data.dnd.CharacterData.Builder.campaignName,
                chargen.lib.character.data.dnd.CharacterData.Builder.stats,
                chargen.lib.character.data.dnd.CharacterData.Builder.raceData,
                chargen.lib.character.data.dnd.CharacterData.Builder.classData,
                chargen.lib.character.data.dnd.CharacterData.Builder.skills,
                chargen.lib.character.data.dnd.CharacterData.Builder.alignment,
                chargen.lib.character.data.dnd.CharacterData.Builder.background,
                chargen.lib.character.data.dnd.CharacterData.Builder.abilities,
                chargen.lib.character.data.dnd.CharacterData.Builder.currentFeatures,
                chargen.lib.character.data.dnd.CharacterData.Builder.exp!!,
                chargen.lib.character.data.dnd.CharacterData.Builder.level,
                chargen.lib.character.data.dnd.CharacterData.Builder.characteristics,
                chargen.lib.character.data.dnd.CharacterData.Builder.backstory,
                chargen.lib.character.data.dnd.CharacterData.Builder.notes
            )
            chargen.lib.character.data.dnd.CharacterData.Builder.flush()

            return data
        }

        override suspend fun buildAndRegister(): chargen.lib.character.data.dnd.CharacterData {
            return chargen.lib.character.data.dnd.CharacterData.Builder.build()
        }

        private fun flush() {
            chargen.lib.character.data.dnd.CharacterData.Builder.playerName = null
            chargen.lib.character.data.dnd.CharacterData.Builder.characterName = null
            chargen.lib.character.data.dnd.CharacterData.Builder.campaignName = null
            chargen.lib.character.data.dnd.CharacterData.Builder.stats = mutableMapOf()
            chargen.lib.character.data.dnd.CharacterData.Builder.raceData = null
            chargen.lib.character.data.dnd.CharacterData.Builder.classData = null
            chargen.lib.character.data.dnd.CharacterData.Builder.skills = mutableMapOf()
            chargen.lib.character.data.dnd.CharacterData.Builder.alignment = null
            chargen.lib.character.data.dnd.CharacterData.Builder.background = null
            chargen.lib.character.data.dnd.CharacterData.Builder.abilities = mutableListOf()
            chargen.lib.character.data.dnd.CharacterData.Builder.currentFeatures = mutableListOf()
            chargen.lib.character.data.dnd.CharacterData.Builder.exp = null
            chargen.lib.character.data.dnd.CharacterData.Builder.level = 1
            chargen.lib.character.data.dnd.CharacterData.Builder.characteristics =
                chargen.lib.character.data.dnd.Characteristics(null, null, null, null, null, null)
            chargen.lib.character.data.dnd.CharacterData.Builder.backstory = null
            chargen.lib.character.data.dnd.CharacterData.Builder.notes = null
        }
    }
}
