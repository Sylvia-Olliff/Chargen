package chargen.lib.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import com.sksamuel.hoplite.watch.FixedIntervalWatchable
import com.sksamuel.hoplite.watch.ReloadableConfig

class Config {
    companion object {
        private const val configFilePath = "/application.yml"
        private lateinit var appConfig: ReloadableConfig<AppConfig>
        private val watcher = FixedIntervalWatchable(10000)

        fun loadConfig() {
            if(this::appConfig.isInitialized) return

            val loader = ConfigLoaderBuilder.default()
                .addResourceSource(configFilePath)
                .build()

            appConfig = ReloadableConfig(loader, AppConfig::class)
                .addWatcher(watcher)
        }

        fun getConfig(): AppConfig {
            if (!this::appConfig.isInitialized) throw UninitializedPropertyAccessException("AppConfig has not been loaded")

            return this.appConfig.getLatest()
        }
    }
}
