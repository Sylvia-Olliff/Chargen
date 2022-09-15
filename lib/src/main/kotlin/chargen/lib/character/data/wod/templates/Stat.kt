package chargen.lib.character.data.wod.templates

interface Stat {
    val Label: String
    val statMax: Int
    val statMin: Int

    fun getValue(): Int

    fun setValue(value: Int)
}
