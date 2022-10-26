package chargen.lib.database

import chargen.database.*
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.EnumColumnAdapter

class ChargenDB {
    companion object {
        private val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:data/chargenData.db")
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
                )
            )
        }

        fun getDBQueries(): ChargenDatabaseQueries = this.DATA.chargenDatabaseQueries
    }
}