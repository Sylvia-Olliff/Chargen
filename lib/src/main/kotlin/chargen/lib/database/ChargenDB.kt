package chargen.lib.database

import chargen.database.*
import chargen.lib.config.AppConfig
import chargen.lib.config.Config
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.EnumColumnAdapter

class ChargenDB {
    companion object {
        private val config: AppConfig = Config.getConfig()
        private val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${config.registry.dataLocation}${config.registry.dbName}.db")
        private val DATA: ChargenDatabase

        init {
            ChargenDatabase.Schema.create(driver)
            DATA = ChargenDatabase(
                driver = driver,
                ClassDataEntityAdapter = ClassDataEntity.Adapter(
                    hitDieAdapter = EnumColumnAdapter(),
                    featuresAdapter = listOfLongsAdapter,
                    proficienciesAdapter = listOfProficienciesAdapter,
                    casterClassDataAdapter = casterClassDataAdapter
                ),
                FeatureDataEntityAdapter = FeatureDataEntity.Adapter(
                    requiredFeaturesAdapter = listOfLongsAdapter,
                    featureTypeAdapter = EnumColumnAdapter(),
                    statAdapter = EnumColumnAdapter(),
                    sourceStatAdapter = EnumColumnAdapter(),
                    spellSlotsAdapter = spellSlotsAdapter
                ),
                RaceDataEntityAdapter = RaceDataEntity.Adapter(
                    statModsAdapter = statMapAdapter,
                    proficienciesAdapter = listOfProficienciesAdapter,
                    featuresAdapter = listOfLongsAdapter
                ),
                SkillDataEntityAdapter = SkillDataEntity.Adapter(
                    statAdapter = EnumColumnAdapter()
                ),
                CharacterDataEntityAdapter = CharacterDataEntity.Adapter(
                    abilitiesAdapter = listOfStringsAdapter,
                    alignmentAdapter = EnumColumnAdapter(),
                    currentFeatureIdsAdapter = mutableListOfLongsAdapter,
                    statsAdapter = statMutableMapAdapter,
                    characteristicsAdapter = characteristicsAdapter,
                    skillIdsAdapter = skillIdsMutableMapAdapter
                )
            )
        }

        fun getDBQueries(): ChargenDatabaseQueries = this.DATA.chargenDatabaseQueries
    }
}