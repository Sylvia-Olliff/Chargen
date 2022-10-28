package chargen.lib.database

import chargen.lib.character.data.dnd.Characteristics
import chargen.lib.character.data.dnd.classes.CasterClassData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.Stats
import com.squareup.sqldelight.ColumnAdapter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


val listOfStringsAdapter = object: ColumnAdapter<MutableList<String>, String> {
    override fun decode(databaseValue: String): MutableList<String> {
        return if (databaseValue.isEmpty()) {
            mutableListOf()
        } else {
            databaseValue.split(",").toMutableList()
        }
    }
    override fun encode(value: MutableList<String>): String { return value.joinToString(separator = ",") }
}

val listOfIntsAdapter = object: ColumnAdapter<List<Int>, String> {
    override fun decode(databaseValue: String): List<Int> {
        return if (databaseValue.isEmpty()) {
            listOf()
        } else {
            val stringList = databaseValue.split(",")
            stringList.map { it.toInt() }
        }
    }

    override fun encode(value: List<Int>): String { return value.joinToString(separator = ",") }
}

val listOfLongsAdapter = object: ColumnAdapter<List<Long>, String> {
    override fun decode(databaseValue: String): List<Long> {
        return if (databaseValue.isEmpty()) {
            listOf()
        } else {
            val stringList = databaseValue.split(",")
            stringList.map { it.toLong() }
        }
    }

    override fun encode(value: List<Long>): String { return value.joinToString(separator = ",") }
}

val mutableListOfLongsAdapter = object: ColumnAdapter<MutableList<Long>, String> {
    override fun decode(databaseValue: String): MutableList<Long> {
        return if (databaseValue.isEmpty()) {
            mutableListOf()
        } else {
            val stringList = databaseValue.split(",")
            stringList.map { it.toLong() }.toMutableList()
        }
    }

    override fun encode(value: MutableList<Long>): String { return value.joinToString(separator = ",") }
}

val listOfProficienciesAdapter = object: ColumnAdapter<List<Proficiency>, String> {
    override fun decode(databaseValue: String): List<Proficiency> {
        return if (databaseValue.isEmpty()) {
            listOf()
        } else {
            val setArray = databaseValue.split(",")
            val finalList: MutableList<Proficiency> = mutableListOf()
            setArray.forEach {
                val keyValue = it.split("=")
                finalList.add(Proficiency(name = keyValue[0], description = keyValue[1]))
            }
            finalList
        }
    }

    override fun encode(value: List<Proficiency>): String {
        return value.joinToString(separator = ",") {
            "${it.name}=${it.description}"
        }
    }
}

val spellSlotsAdapter = object: ColumnAdapter<MutableMap<Int, MutableMap<Int, Int>>, String> {
    override fun decode(databaseValue: String): MutableMap<Int, MutableMap<Int, Int>> {
        if (databaseValue.isEmpty()) {
            return mutableMapOf()
        } else {
            val map: MutableMap<Int, MutableMap<Int, Int>> = mutableMapOf()
            val outerEntries = databaseValue.split(",")
            outerEntries.forEach { outerEntry ->
                val outerLayerKV = outerEntry.split("=")
                val innerLayerEntries = outerLayerKV[1].split("/")
                val innerMap = mutableMapOf<Int, Int>()
                innerLayerEntries.forEach {
                    val innerLayerKV = it.split(":")
                    innerMap[innerLayerKV[0].toInt()] = innerLayerKV[1].toInt()
                }
                map[outerLayerKV[0].toInt()] = innerMap
            }
            return map
        }
    }

    override fun encode(value: MutableMap<Int, MutableMap<Int, Int>>): String {
        val encodedString = value.map { outer ->
            val innerString = outer.value.map { innerEntry ->
                "${innerEntry.key}:${innerEntry.value}"
            }
            "${outer.key}=${innerString.joinToString(separator = "/")}"
        }
        return encodedString.joinToString(separator = ",")
    }
}

val casterClassDataAdapter = object: ColumnAdapter<CasterClassData, String> {
    override fun decode(databaseValue: String): CasterClassData {
        return if (databaseValue.isEmpty()) {
            CasterClassData(null, null, null, null)
        } else {
            Json.decodeFromString(databaseValue)
        }
    }

    override fun encode(value: CasterClassData): String {
        return Json.encodeToString(value)
    }
}

val statMapAdapter = object: ColumnAdapter<Map<Stats, Int>, String> {
    override fun decode(databaseValue: String): Map<Stats, Int> {
        return if (databaseValue.isEmpty()) {
            mapOf()
        } else {
            val setArray = databaseValue.split(",")
            val finalMap: MutableMap<Stats, Int> = mutableMapOf()
            setArray.forEach {
                val keyValue = it.split("=")
                finalMap[Stats.valueOf(keyValue[0])] = keyValue[1].toInt()
            }
            finalMap
        }
    }

    override fun encode(value: Map<Stats, Int>): String {
        return value.map {
            "${it.key.name}=${it.value}"
        }.joinToString(separator = ",")
    }
}

val statMutableMapAdapter = object: ColumnAdapter<MutableMap<Stats, Int>, String> {
    override fun decode(databaseValue: String): MutableMap<Stats, Int> {
        return if (databaseValue.isEmpty()) {
            mutableMapOf()
        } else {
            val setArray = databaseValue.split(",")
            val finalMap: MutableMap<Stats, Int> = mutableMapOf()
            setArray.forEach {
                val keyValue = it.split("=")
                finalMap[Stats.valueOf(keyValue[0])] = keyValue[1].toInt()
            }
            finalMap
        }
    }

    override fun encode(value: MutableMap<Stats, Int>): String {
        return value.map {
            "${it.key.name}=${it.value}"
        }.joinToString(separator = ",")
    }
}

val skillIdsMutableMapAdapter = object: ColumnAdapter<MutableMap<Long, Boolean>, String> {
    override fun decode(databaseValue: String): MutableMap<Long, Boolean> {
        return if (databaseValue.isEmpty()) {
            mutableMapOf()
        } else {
            val setArray = databaseValue.split(",")
            val finalMap: MutableMap<Long, Boolean> = mutableMapOf()
            setArray.forEach {
                val keyValue = it.split("=")
                finalMap[keyValue[0].toLong()] = keyValue[1].toBoolean()
            }
            finalMap
        }
    }

    override fun encode(value: MutableMap<Long, Boolean>): String {
        return value.map {
            "${it.key}=${it.value}"
        }.joinToString(separator = ",")
    }
}

val characteristicsAdapter = object: ColumnAdapter<Characteristics, String> {
    override fun decode(databaseValue: String): Characteristics {
        return if (databaseValue.isEmpty()) {
            Characteristics(null, null, null, null, null, null)
        } else {
            Json.decodeFromString(databaseValue)
        }
    }

    override fun encode(value: Characteristics): String {
        return Json.encodeToString(value)
    }
}

