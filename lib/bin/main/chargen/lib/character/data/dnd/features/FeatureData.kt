package chargen.lib.character.data.dnd.features

import chargen.lib.character.data.dnd.templates.DataBuilder
import chargen.lib.character.data.dnd.templates.DataEntity
import chargen.lib.character.data.dnd.types.FeatureType
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.exceptions.MissingPropertyException
import chargen.lib.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class FeatureData(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID,
    var name: String,
    var levelGained: Int,
    var description: String,
    var group: String,
    val requiredFeatures: List<@Serializable(with = UUIDSerializer::class) UUID>,
    val featureType: FeatureType,
    val value: Int?,
    val stat: Stats?,
    val sourceStat: Stats?,
    val spellSlots: MutableMap<Int, MutableMap<Int, Int>?>?
): DataEntity {
    companion object Builder : DataBuilder<FeatureData> {
        private var name: String = ""
        private var levelGained = 1
        private var description: String = ""
        private var group: String = ""
        private var requiredFeatures: MutableList<UUID> = mutableListOf()
        private var featureType: FeatureType? = null
        private var value: Int? = null
        private var stat: Stats? = null
        private var sourceStat: Stats? = null
        private var spellSlots: MutableMap<Int, MutableMap<Int, Int>?>? = null

        fun withName(name: String): Builder {
            this.name = name
            return this
        }

        fun atLevel(level: Int): Builder {
            this.levelGained = level
            return this
        }

        fun withDescription(description: String): Builder {
            this.description = description
            return this
        }

        fun withGroup(group: String): Builder {
            this.group = group
            return this
        }

        fun withRequiredFeatures(features: List<UUID>): Builder {
            features.forEach {
                this.requiredFeatures.add(it)
            }
            return this
        }

        fun ofType(type: FeatureType): Builder {
            this.featureType = type
            return this
        }

        fun withValue(value: Int): Builder {
            this.value = value
            return this
        }

        fun withTargetStat(stat: Stats): Builder {
            this.stat = stat
            return this
        }

        fun withSourceStat(stat: Stats): Builder {
            this.sourceStat = stat
            return this
        }

        fun withSpellSlots(slots: Map<Int, Map<Int, Int>?>): Builder {
            if (this.spellSlots == null) this.spellSlots = mutableMapOf()

            slots.forEach {
                if (it.value != null) {
                    this.spellSlots!![it.key] = it.value as MutableMap<Int, Int>?
                }
            }
            return this
        }

        override fun build(): FeatureData {
            if (this.name == "") throw MissingPropertyException("Feature Data must have a name")
            if (this.group == "") this.group = "UNIVERSAL"
            if (this.featureType == null) throw MissingPropertyException("Feature Data must have a Feature Type")

            val data = FeatureData(
                UUID.randomUUID(),
                this.name,
                this.levelGained,
                this.description,
                this.group,
                this.requiredFeatures,
                this.featureType!!,
                this.value,
                this.stat,
                this.sourceStat,
                this.spellSlots
            )
            flush()

            return data
        }

        override suspend fun buildAndRegister(): FeatureData {
            val data = build()
            FeatureRegistry.register(data)
            return data
        }

        private fun flush() {
            this.name = ""
            this.levelGained = 1
            this.description = ""
            this.group = ""
            this.requiredFeatures = mutableListOf()
            this.featureType = null
            this.value = null
            this.stat = null
            this.sourceStat = null
            this.spellSlots = null
        }
    }
}
