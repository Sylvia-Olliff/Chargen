package chargen.lib.character.data.dnd.classes

import chargen.lib.character.data.dnd.templates.DataBuilder
import chargen.lib.character.data.dnd.templates.DataEntity
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.DiceType
import chargen.lib.exceptions.MissingPropertyException
import chargen.lib.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ClassData(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID,
    val name: String,
    var isCaster: Boolean,
    var hitDie: DiceType,
    var numAttacks: Int,
    val proficiencies: MutableList<Proficiency>,
    val features: MutableList<@Serializable(with = UUIDSerializer::class) UUID>,
    var resourceName: String?,
    var resource: Int?,
    val casterData: CasterClassData?
): DataEntity {
    companion object Builder : DataBuilder<ClassData> {
        private var name: String = ""
        private var isCaster = false
        private var hitDie: DiceType? = null
        private var numAttacks = 1
        private var proficiencies: MutableList<Proficiency> = mutableListOf()
        private var features: MutableList<UUID> = mutableListOf()
        private var resourceName: String? = null
        private var resource: Int? = null
        private var casterData: CasterClassData? = null

        fun withName(name: String): Builder {
            this.name = name
            return this
        }

        fun isCaster(): Builder {
            this.isCaster = true
            return this
        }

        fun withHitDie(dieType: DiceType): Builder {
            this.hitDie = dieType
            return this
        }

        fun withNumberOfAttacks(num: Int): Builder {
            this.numAttacks = num
            return this
        }

        fun withProficiencies(profs: List<Proficiency>): Builder {
            proficiencies = mutableListOf()
            profs.forEach {
                proficiencies.add(it)
            }
            return this
        }

        fun withFeatures(features: MutableList<UUID>): Builder {
            this.features = mutableListOf()
            features.forEach {
                this.features.add(it)
            }
            return this
        }

        fun withResourceNameAndResource(name: String, amount: Int): Builder {
            resourceName = name
            resource = amount
            return this
        }

        fun withCasterData(data: CasterClassData): Builder {
            this.casterData = data
            return this
        }

        override fun build(): ClassData {
            if (this.name == "") throw MissingPropertyException("Class Data must have a name")
            if (this.hitDie == null) throw MissingPropertyException("Class Data must have a defined HitDie")

            val data = ClassData(
                UUID.randomUUID(),
                name,
                isCaster,
                hitDie!!,
                numAttacks,
                proficiencies,
                features,
                resourceName,
                resource,
                casterData
            )

            flush()
            return data
        }

        override suspend fun buildAndRegister(): ClassData {
            val data = build()
            ClassRegistry.register(data)
            return data
        }

        private fun flush() {
            name = ""
            isCaster = false
            numAttacks = 1
            proficiencies = mutableListOf()
            features = mutableListOf()
            resourceName = null
            resource = null
            casterData = null
            hitDie = null
        }
    }
}
