package chargen.lib.character.data.dnd.features

import chargen.lib.character.data.dnd.templates.Registry
import chargen.lib.config.Config
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileOutputStream

@kotlinx.serialization.ExperimentalSerializationApi
internal class FeatureRegistry {
    companion object : Registry<FeatureData>() {

        override suspend fun load() {
            val file = File(Config.getConfig().registry.dataLocation + Config.getConfig().registry.featuresFileName)
            val entries: List<FeatureData>
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
            val file = File(Config.getConfig().registry.dataLocation + Config.getConfig().registry.featuresFileName)

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
