package chargen.lib.character.data.dnd.templates

import chargen.lib.exceptions.KeyNotFoundException
import java.util.*
import java.util.concurrent.locks.ReentrantLock

open class Registry<T : DataEntity> {
    protected val REGISTRY: MutableMap<UUID, T> = mutableMapOf()
    protected val registryLock = ReentrantLock()

    open suspend fun get(id: UUID): T {
        this.registryLock.lock()
        if (REGISTRY.containsKey(id)) {
            this.registryLock.unlock()
            return this.REGISTRY[id]!!
        }
        this.registryLock.unlock()
        throw KeyNotFoundException(null)
    }

    open suspend fun register(entry: T) {
        this.registryLock.lock()
        this.REGISTRY[entry.id] = entry
        this.registryLock.unlock()
    }

    open suspend fun delete(id: UUID) {
        if (validate(id)) {
            this.registryLock.lock()
            this.REGISTRY.remove(id)
            this.registryLock.unlock()
        }
    }

    fun getList() = this.REGISTRY.values.toList()

    open fun validate(id: UUID): Boolean {
        return this.REGISTRY.containsKey(id)
    }

    open fun load() {}
    open fun save() {}
}
