package chargen.lib.config

data class Registry(
    val saveInterval: Int,
    val dataLocation: String,
    val featuresFileName: String,
    val classesFileName: String,
    val racesFileName: String,
    val skillsFileName: String
)
