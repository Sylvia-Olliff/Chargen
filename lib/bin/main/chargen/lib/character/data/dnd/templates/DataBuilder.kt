package chargen.lib.character.data.dnd.templates

interface DataBuilder<T> {
    fun build(): T
    suspend fun buildAndRegister(): T
}
