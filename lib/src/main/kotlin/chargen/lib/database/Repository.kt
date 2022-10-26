package chargen.lib.database

import chargen.database.ChargenDatabaseQueries
import chargen.database.ClassDataEntity
import com.badoo.reaktive.base.setCancellable
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.observable.*
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.single.*
import com.squareup.sqldelight.Query

abstract class Repository<T: Any> {
    private lateinit var queries: Single<ChargenDatabaseQueries>
    protected var isLoaded: Boolean = false

    fun loadRepo() {
        this.queries = singleOf(ChargenDB.getDBQueries())
            .asObservable()
            .replay()
            .autoConnect()
            .firstOrError()
    }

    abstract fun observeAll():  Observable<List<T>>

    abstract fun select(id: Long): Maybe<T>

    abstract fun add(entity: T): Completable

    abstract fun delete(id: Long): Completable

    abstract fun clear(): Completable

    protected fun <T: Any> query(query: (ChargenDatabaseQueries) -> Query<T>): Single<Query<T>> =
        queries
            .observeOn(ioScheduler)
            .map(query)

    protected fun execute(query: (ChargenDatabaseQueries) -> Unit): Completable =
        queries
            .observeOn(ioScheduler)
            .doOnBeforeSuccess(query)
            .asCompletable()

    protected fun <T : Any, R> Single<Query<T>>.observe(get: (Query<T>) -> R): Observable<R> =
        flatMapObservable { it.observed() }
            .observeOn(ioScheduler)
            .map(get)

    protected fun <T : Any> Query<T>.observed(): Observable<Query<T>> =
        observable { emitter ->
            val listener =
                object : Query.Listener {
                    override fun queryResultsChanged() {
                        emitter.onNext(this@observed)
                    }
                }

            emitter.onNext(this@observed)
            addListener(listener)
            emitter.setCancellable { removeListener(listener) }
        }
}