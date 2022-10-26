package chargen.lib.database.stores.classes

import chargen.lib.character.data.dnd.classes.CasterClassData
import chargen.lib.character.data.dnd.classes.ClassData
import chargen.lib.character.data.dnd.features.FeatureData
import chargen.lib.character.data.dnd.templates.Proficiency
import chargen.lib.character.data.dnd.types.DiceType
import chargen.lib.database.stores.classes.ClassEditDataStore.Intent
import chargen.lib.database.stores.classes.ClassEditDataStore.State
import chargen.lib.database.stores.classes.ClassEditDataStore.Label
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.maybe.Maybe
import com.badoo.reaktive.maybe.blockingGet
import com.badoo.reaktive.maybe.map
import com.badoo.reaktive.maybe.observeOn
import com.badoo.reaktive.scheduler.mainScheduler

class ClassEditDataStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: Database,
    private val id: Long
) {

    fun provide(): ClassEditDataStore =
        object: ClassEditDataStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ClassEditStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class Loaded(val item: ClassData): Msg()
        data class NameChanged(val name: String): Msg()
        data class IsCasterFlagChanged(val value: Boolean): Msg()
        data class HitDieChanged(val hitDie: DiceType): Msg()
        data class NumAttacksChanged(val numAttacks: Int): Msg()
        data class AddedFeature(val features: List<FeatureData>): Msg()
        data class AddedProficiency(val proficiencies: List<Proficiency>): Msg()
        data class RemovedFeature(val features: List<FeatureData>): Msg()
        data class RemovedProficiency(val proficiencies: List<Proficiency>): Msg()
        data class ResourcesChanged(val resource: Int, val resourceName: String): Msg()
        data class CasterDataChanged(val casterData: CasterClassData): Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeAction(action: Unit, getState: () -> State) {
            database
                .load(id)
                .map(Msg::Loaded)
                .observeOn(mainScheduler)
                .subscribeScoped(onSuccess = ::dispatch)
        }

        override fun executeIntent(intent: Intent, getState: () -> State) =
            when (intent) {
                is Intent.UpdateName -> updateName(intent.name, getState())
                is Intent.UpdateCasterFlag -> updateIsCasterFlag(intent.value, getState())
                is Intent.RemoveFeature -> removeFeature(intent.feature, getState())
                is Intent.RemoveProficiency -> removeProficiency(intent.proficiency, getState())
                is Intent.SetCasterData -> setCasterData(intent.casterData, getState())
                is Intent.SetHitDie -> setHitDie(intent.hitDie, getState())
                is Intent.SetNumAttacks -> setNumAttacks(intent.numAttacks, getState())
                is Intent.SetResources -> setResources(intent.resource, intent.resourceName, getState())
                is Intent.AddFeature -> addFeature(intent.feature, getState())
                is Intent.AddProficiency -> addProficiency(intent.proficiency, getState())
            }

        private fun updateName(name: String, state: State) {
            dispatch(Msg.NameChanged(name = name))
            publish(Label.Changed(ClassData(id,
                name = name,
                isCaster = state.isCaster,
                hitDie = state.hitDie,
                numAttacks = state.numAttacks,
                features = state.features,
                proficiencies = state.proficiencies,
                resourceName = state.resourceName,
                resource = state.resource,
                casterData = state.casterData
            )))
            database.setName(id, name = name).subscribeScoped()
        }

        private fun updateIsCasterFlag(value: Boolean, state: State) {
            dispatch(Msg.IsCasterFlagChanged(value = value))
            publish(Label.Changed(ClassData(id,
                name = state.name,
                isCaster = value,
                hitDie = state.hitDie,
                numAttacks = state.numAttacks,
                features = state.features,
                proficiencies = state.proficiencies,
                resourceName = state.resourceName,
                resource = state.resource,
                casterData = state.casterData
            )))
            database.setIsCasterFlag(id, value)
        }

        private fun removeFeature(feature: Long, state: State) {
            state.features = state.features.filterNot { it.id == feature }.toMutableList()
            dispatch(Msg.RemovedFeature(state.features))
            publish(Label.Changed(
                ClassData(id,
                    name = state.name,
                    isCaster = state.isCaster,
                    hitDie = state.hitDie,
                    numAttacks = state.numAttacks,
                    features = state.features,
                    proficiencies = state.proficiencies,
                    resourceName = state.resourceName,
                    resource = state.resource,
                    casterData = state.casterData
            )))
            database.removeFeature(id, feature).subscribeScoped()
        }

        private fun addFeature(feature: Long, state: State) {
            val featureData = database.loadFeature(feature).blockingGet()
            if (featureData != null) {
                state.features.add(featureData)
                dispatch(Msg.AddedFeature(state.features))
                publish(Label.Changed(
                    ClassData(id,
                        name = state.name,
                        isCaster = state.isCaster,
                        hitDie = state.hitDie,
                        numAttacks = state.numAttacks,
                        features = state.features,
                        proficiencies = state.proficiencies,
                        resourceName = state.resourceName,
                        resource = state.resource,
                        casterData = state.casterData
                )))
                database.addFeature(id, featureData)
            }
        }

        private fun removeProficiency(proficiency: Proficiency, state: State) {
            state.proficiencies = state.proficiencies.filterNot { it == proficiency }.toMutableList()
            dispatch(Msg.RemovedProficiency(state.proficiencies))
            publish(Label.Changed(
                ClassData(id,
                    name = state.name,
                    isCaster = state.isCaster,
                    hitDie = state.hitDie,
                    numAttacks = state.numAttacks,
                    features = state.features,
                    proficiencies = state.proficiencies,
                    resourceName = state.resourceName,
                    resource = state.resource,
                    casterData = state.casterData
            )))
            database.removeProficiency(id, proficiency).subscribeScoped()
        }

        private fun addProficiency(proficiency: Proficiency, state: State) {
            state.proficiencies.add(proficiency)
            dispatch(Msg.AddedProficiency(state.proficiencies))
            publish(Label.Changed(
                ClassData(id,
                    name = state.name,
                    isCaster = state.isCaster,
                    hitDie = state.hitDie,
                    numAttacks = state.numAttacks,
                    features = state.features,
                    proficiencies = state.proficiencies,
                    resourceName = state.resourceName,
                    resource = state.resource,
                    casterData = state.casterData
            )))
            database.addProficiency(id, proficiency)
        }

        private fun setCasterData(casterData: CasterClassData, state: State) {
            dispatch(Msg.CasterDataChanged(casterData))
            publish(Label.Changed(
                ClassData(id,
                    name = state.name,
                    isCaster = state.isCaster,
                    hitDie = state.hitDie,
                    numAttacks = state.numAttacks,
                    features = state.features,
                    proficiencies = state.proficiencies,
                    resourceName = state.resourceName,
                    resource = state.resource,
                    casterData = casterData
            )))
            database.setCasterData(id, casterData).subscribeScoped()
        }

        private fun setHitDie(hitDie: DiceType, state: State) {
            dispatch(Msg.HitDieChanged(hitDie))
            publish(Label.Changed(
                ClassData(id,
                    name = state.name,
                    isCaster = state.isCaster,
                    hitDie = hitDie,
                    numAttacks = state.numAttacks,
                    features = state.features,
                    proficiencies = state.proficiencies,
                    resourceName = state.resourceName,
                    resource = state.resource,
                    casterData = state.casterData
            )))
            database.setHitDie(id, hitDie).subscribeScoped()
        }

        private fun setNumAttacks(numAttacks: Int, state: State) {
            dispatch(Msg.NumAttacksChanged(numAttacks))
            publish(Label.Changed(
                ClassData(id,
                    name = state.name,
                    isCaster = state.isCaster,
                    hitDie = state.hitDie,
                    numAttacks = numAttacks,
                    features = state.features,
                    proficiencies = state.proficiencies,
                    resourceName = state.resourceName,
                    resource = state.resource,
                    casterData = state.casterData
            )))
            database.setAttacks(id, numAttacks).subscribeScoped()
        }

        private fun setResources(resource: Int, resourceName: String, state: State) {
            dispatch(Msg.ResourcesChanged(resource, resourceName))
            publish(Label.Changed(
                ClassData(id,
                    name = state.name,
                    isCaster = state.isCaster,
                    hitDie = state.hitDie,
                    numAttacks = state.numAttacks,
                    features = state.features,
                    proficiencies = state.proficiencies,
                    resource = resource,
                    resourceName = resourceName,
                    casterData = state.casterData
            )))
            database.setResource(id, resourceName, resource).subscribeScoped()
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.Loaded -> copy(name = msg.item.name,
                    isCaster = msg.item.isCaster,
                    hitDie = msg.item.hitDie,
                    numAttacks = msg.item.numAttacks,
                    features = msg.item.features,
                    proficiencies = msg.item.proficiencies,
                    resourceName = msg.item.resourceName,
                    resource = msg.item.resource,
                    casterData = msg.item.casterData
                )
                is Msg.IsCasterFlagChanged -> copy(isCaster = msg.value)
                is Msg.AddedFeature -> copy(features = msg.features.toMutableList())
                is Msg.AddedProficiency -> copy(proficiencies = msg.proficiencies.toMutableList())
                is Msg.CasterDataChanged -> copy(casterData = msg.casterData)
                is Msg.HitDieChanged -> copy(hitDie = msg.hitDie)
                is Msg.NameChanged -> copy(name = msg.name)
                is Msg.NumAttacksChanged -> copy(numAttacks = msg.numAttacks)
                is Msg.RemovedFeature -> copy(features = msg.features.toMutableList())
                is Msg.RemovedProficiency -> copy(proficiencies = msg.proficiencies.toMutableList())
                is Msg.ResourcesChanged -> copy(resourceName = msg.resourceName, resource = msg.resource)
            }
    }

    interface Database {
        fun load(id: Long): Maybe<ClassData>

        fun loadFeature(id: Long): Maybe<FeatureData>

        fun setIsCasterFlag(id: Long, value: Boolean): Completable

        fun setName(id: Long, name: String): Completable

        fun setHitDie(id: Long, hitDie: DiceType): Completable

        fun setAttacks(id: Long, numAttacks: Int): Completable

        fun addFeature(id: Long, feature: FeatureData): Completable

        fun addProficiency(id: Long, proficiency: Proficiency): Completable

        fun removeFeature(id: Long, feature: Long): Completable

        fun removeProficiency(id: Long, proficiency: Proficiency): Completable

        fun setResource(id: Long, resourceName: String, resource: Int): Completable

        fun setCasterData(id: Long, casterData: CasterClassData): Completable
    }
}