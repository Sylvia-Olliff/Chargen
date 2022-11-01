package chargen.lib.database.stores.features

import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.types.FeatureType
import chargen.lib.database.stores.features.FeatureMainDataStore.Intent
import chargen.lib.database.stores.features.FeatureMainDataStore.State
import com.arkivanov.mvikotlin.core.store.Store

interface FeatureMainDataStore: Store<Intent, State, Nothing> {

    sealed class Intent {
        data class DeleteFeature(val id: Long): Intent()
        object AddFeature: Intent()
    }

    data class State(
        val items: List<FeatureData> = emptyList(),
        val selected: FeatureData = FeatureData.DEFAULT
    )
}