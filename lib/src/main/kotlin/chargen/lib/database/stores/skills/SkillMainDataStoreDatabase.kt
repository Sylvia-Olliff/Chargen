package chargen.lib.database.stores.skills

import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.skills.toEntity
import chargen.lib.database.SkillRepository
import chargen.lib.utils.toSkillData
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapIterable

class SkillMainDataStoreDatabase: SkillMainDataStoreProvider.Database {
    override val updates: Observable<List<SkillData>> =
        SkillRepository.observeAll()
            .mapIterable { it.toSkillData() }

    override fun addSkill(item: SkillData): Completable =
        SkillRepository.add(item.toEntity())

    override fun deleteSkill(id: Long): Completable =
        SkillRepository.delete(id)
}