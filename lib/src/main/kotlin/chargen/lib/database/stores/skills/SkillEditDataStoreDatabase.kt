package chargen.lib.database.stores.skills

import chargen.lib.character.data.dnd.skills.SkillData
import chargen.lib.character.data.dnd.types.Stats
import chargen.lib.database.SkillRepository
import chargen.lib.utils.toSkillData
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.map

class SkillEditDataStoreDatabase : SkillEditDataStoreProvider.Database {
    override fun load(id: Long): Maybe<SkillData> =
        SkillRepository.select(id)
            .map { it.toSkillData() }

    override fun updateName(id: Long, name: String): Completable =
        SkillRepository.updateName(id, name)

    override fun updateDescription(id: Long, description: String): Completable =
        SkillRepository.updateDescription(id, description)

    override fun updateStat(id: Long, stat: Stats): Completable =
        SkillRepository.updateStat(id, stat)

    override fun updateUntrained(id: Long, untrained: Boolean): Completable =
        SkillRepository.updateUntrained(id, untrained)
}