package chargen.lib.character.data.dnd.races

import chargen.lib.character.data.dnd.templates.Registry
import chargen.lib.config.Config
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileOutputStream

@kotlinx.serialization.ExperimentalSerializationApi
class RaceRegistry {
    companion object: Registry<RaceData>() {

        override fun load() {
            val file = File(Config.getConfig().registry.dataLocation + Config.getConfig().registry.racesFileName)
            val entries: List<RaceData>
            file.inputStream().use {
                entries = Json.decodeFromStream(it)
            }

            registryLock.lock()
            REGISTRY.clear()
            entries.forEach {
                REGISTRY[it.id] = it
            }
            registryLock.unlock()
        }

        override fun save() {
            val file = File(Config.getConfig().registry.dataLocation + Config.getConfig().registry.racesFileName)

            try {
                registryLock.lock()
                FileOutputStream(file).use {
                    Json.encodeToStream(REGISTRY.values.toList(), it)
                }
            } finally {
                registryLock.unlock()
            }
        }
    }
}
