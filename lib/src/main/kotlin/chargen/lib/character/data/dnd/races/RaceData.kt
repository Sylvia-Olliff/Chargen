package chargen.lib.character.data.dnd.races

import chargen.lib.character.data.dnd.templates.DataBuilder
import chargen.lib.character.data.dnd.templates.DataEntity
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.wod.templates.Stat
import chargen.lib.exceptions.MissingPropertyException
import chargen.lib.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class RaceData(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID,
    val name: String,
    val raceNamePlural: String,
    val description: String,
    val statMods: Map<Stat, Int>,
    val proficiencies: List<Proficiency>,
    val features: MutableList<@Serializable(with = UUIDSerializer::class) UUID>,
): DataEntity {
    companion object Builder : DataBuilder<RaceData> {
        private var name: String = ""
        private var raceNamePlural = ""
        private var description = ""
        private var statMods: MutableMap<Stat, Int> = mutableMapOf()
        private var proficiencies: MutableList<Proficiency> = mutableListOf()
        private var features: MutableList<UUID> = mutableListOf()

        fun withName(name: String, namePlural: String): Builder {
            this.name = name
            this.raceNamePlural = namePlural
            return this
        }

        fun withDescription(description: String): Builder {
            this.description = description
            return this
        }

        fun withStatMods(mods: Map<Stat, Int>): Builder {
            mods.forEach {
                this.statMods[it.key] = it.value
            }
            return this
        }

        fun withProficiencies(profs: List<Proficiency>): Builder {
            profs.forEach {
                this.proficiencies.add(it)
            }
            return this
        }

        fun withFeatures(features: List<UUID>): Builder {
            features.forEach {
                this.features.add(it)
            }
            return this
        }

        override fun build(): RaceData {
            if (name == "") throw MissingPropertyException("Race Data must have a name")
            if (raceNamePlural == "") throw MissingPropertyException("Race Data must have a plural name")
            if (description == "") throw MissingPropertyException("Race Data must have a description")

            val data = RaceData(
                UUID.randomUUID(),
                this.name,
                this.raceNamePlural,
                this.description,
                this.statMods,
                this.proficiencies,
                this.features
            )
            flush()

            return data
        }

        override suspend fun buildAndRegister(): RaceData {
            val data = build()
            RaceRegistry.register(data)
            return data
        }

        private fun flush() {
            this.name = ""
            this.raceNamePlural = ""
            this.description = ""
            this.statMods = mutableMapOf()
            this.proficiencies = mutableListOf()
            this.features = mutableListOf()
        }
    }
}
