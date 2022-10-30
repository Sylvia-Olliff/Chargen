package chargen.lib.character.data.dnd.utils

internal class Utils {
    companion object {
        fun convertAttributeToModifier(statValue: Int): Int {
            return (statValue - 10) / 2
        }
    }
}
