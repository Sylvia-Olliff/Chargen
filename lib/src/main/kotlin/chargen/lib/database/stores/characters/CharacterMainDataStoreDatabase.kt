package chargen.lib.database.stores.characters

import chargen.lib.character.data.dnd.CharacterData
import chargen.lib.character.data.dnd.toEntity
import chargen.lib.database.CharacterRepository
import chargen.lib.utils.toCharacterData
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapIterable

class CharacterMainDataStoreDatabase: CharacterMainDataStoreProvider.Database {
    override val updates: Observable<List<CharacterData>> =
        CharacterRepository.observeAll()
            .mapIterable { it.toCharacterData() }

    override fun addCharacter(item: CharacterData): Completable =
        CharacterRepository.add(item.toEntity())

    override fun deleteCharacter(id: Long): Completable =
        CharacterRepository.delete(id)

}