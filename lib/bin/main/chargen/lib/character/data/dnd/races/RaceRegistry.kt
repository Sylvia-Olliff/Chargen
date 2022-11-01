package chargen.lib.character.data.dnd.races

import chargen.lib.character.data.dnd.templates.Registry
import chargen.lib.config.Config
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileOutputStream

@kotlinx.serialization.ExperimentalSerializationApi
internal class RaceRegistry {
    companion object: Registry<RaceData>() {

        override suspend fun load() {
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

        override suspend fun save() {
            val file = File(Config.getConfig().registry.dataLocation + Config.getConfig().registry.racesFileName)

            try {
                registryLock.lock()
                file.outputStream().use {
                    Json.encodeToStream(REGISTRY.values.toList(), it)
                }
            } finally {
                registryLock.unlock()
            }
        }
    }
}