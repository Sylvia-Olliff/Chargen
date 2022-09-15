package chargen.lib.character.data.dnd.types

enum class CasterType(val spellSet: MutableMap<Int, MutableMap<Int, Int>?>) {
    THIRD(mutableMapOf(
        1 to mutableMapOf(),
        2 to mutableMapOf(),
        3 to mutableMapOf(
            1 to 2
        ),
        4 to mutableMapOf(
            1 to 3
        ),
        5 to mutableMapOf(
            1 to 3
        ),
        6 to mutableMapOf(
            1 to 3
        ),
        7 to mutableMapOf(
            1 to 4,
            2 to 2
        ),
        8 to mutableMapOf(
            1 to 4,
            2 to 2
        ),
        9 to mutableMapOf(
            1 to 4,
            2 to 2
        ),
        10 to mutableMapOf(
            1 to 4,
            2 to 3
        ),
        11 to mutableMapOf(
            1 to 4,
            2 to 3
        ),
        12 to mutableMapOf(
            1 to 4,
            2 to 3
        ),
        13 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 2
        ),
        14 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 2
        ),
        15 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 2
        ),
        16 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3
        ),
        17 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3
        ),
        18 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3
        ),
        19 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 1
        ),
        20 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 1
        )
    )),
    HALF(mutableMapOf(
        1 to mutableMapOf(
        ),
        2 to mutableMapOf(
            1 to 2
        ),
        3 to mutableMapOf(
            1 to 3
        ),
        4 to mutableMapOf(
            1 to 3
        ),
        5 to mutableMapOf(
            1 to 4,
            2 to 2
        ),
        6 to mutableMapOf(
            1 to 4,
            2 to 2
        ),
        7 to mutableMapOf(
            1 to 4,
            2 to 3
        ),
        8 to mutableMapOf(
            1 to 4,
            2 to 3
        ),
        9 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 2
        ),
        10 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 2
        ),
        11 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3
        ),
        12 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3
        ),
        13 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 1
        ),
        14 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 1
        ),
        15 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 2
        ),
        16 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 2
        ),
        17 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 1
        ),
        18 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 1
        ),
        19 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 2
        ),
        20 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 2
        )
    )),
    FULL(mutableMapOf(
        1 to mutableMapOf(
            1 to 2
        ),
        2 to mutableMapOf(
            1 to 3
        ),
        3 to mutableMapOf(
            1 to 4,
            2 to 2
        ),
        4 to mutableMapOf(
            1 to 4,
            2 to 3
        ),
        5 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 2
        ),
        6 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3
        ),
        7 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 1
        ),
        8 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 2
        ),
        9 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 1
        ),
        10 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 2
        ),
        11 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 2,
            6 to 1
        ),
        12 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 2,
            6 to 1
        ),
        13 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 2,
            6 to 1,
            7 to 1
        ),
        14 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 2,
            6 to 1,
            7 to 1
        ),
        15 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 2,
            6 to 1,
            7 to 1,
            8 to 1
        ),
        16 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 2,
            6 to 1,
            7 to 1,
            8 to 1
        ),
        17 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 2,
            6 to 1,
            7 to 1,
            8 to 1,
            9 to 1
        ),
        18 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 3,
            6 to 1,
            7 to 1,
            8 to 1,
            9 to 1
        ),
        19 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 3,
            6 to 2,
            7 to 1,
            8 to 1,
            9 to 1
        ),
        20 to mutableMapOf(
            1 to 4,
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 3,
            6 to 2,
            7 to 2,
            8 to 1,
            9 to 1
        )
    ));
}
