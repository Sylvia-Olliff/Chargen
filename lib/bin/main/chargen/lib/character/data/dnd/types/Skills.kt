package chargen.lib.character.data.dnd.types

enum class Skills(val stat: Stats) {
    ACROBATICS(Stats.DEX),
    ANIMAL_HANDLING(Stats.WIS),
    ARCANA(Stats.INT),
    ATHLETICS(Stats.STR),
    DECEPTION(Stats.CHA),
    HISTORY(Stats.INT),
    INSIGHT(Stats.WIS),
    INTIMIDATION(Stats.CHA),
    INVESTIGATION(Stats.INT),
    MEDICINE(Stats.WIS),
    NATURE(Stats.INT),
    PERCEPTION(Stats.WIS),
    PERFORMANCE(Stats.CHA),
    PERSUASION(Stats.CHA),
    RELIGION(Stats.INT),
    SLEIGHT_HAND(Stats.DEX),
    STEALTH(Stats.DEX),
    SURVIVAL(Stats.WIS)
}
