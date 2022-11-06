package chargen.lib.character.data.dnd.loaders

import chargen.database.ChargenDatabaseQueries
import chargen.lib.character.data.dnd.templates.DataEntity
import chargen.lib.database.ChargenDB

abstract class Loader<T : DataEntity> {
    protected lateinit var queries: ChargenDatabaseQueries
    protected val dataSet: MutableList<T> = mutableListOf()

    protected fun loadQueries() {
        if (!this::queries.isInitialized) { this.queries = ChargenDB.getDBQueries() }
    }

    protected abstract fun validateDB(forceReload: Boolean): Boolean

    protected abstract fun loadEntries()

    public abstract fun load(forceReload: Boolean = false)
}