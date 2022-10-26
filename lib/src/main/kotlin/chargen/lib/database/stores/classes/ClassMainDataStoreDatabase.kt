package chargen.lib.database.stores.classes

import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.classes.toEntity
import chargen.lib.database.ClassRepository
import chargen.lib.utils.*
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapIterable

class ClassMainDataStoreDatabase: ClassMainDataStoreProvider.Database {
    override val updates: Observable<List<ClassData>> =
        ClassRepository.observeAll()
            .mapIterable { it.toClassData() }

    override fun addClass(item: ClassData): Completable =
        ClassRepository.add(item.toEntity())

    override fun deleteClass(id: Long): Completable =
        ClassRepository.delete(id)

}