package day24

import java.io.File
import kotlin.math.pow

fun main() {
    val unpInput = File("sources\\inputs\\Day24.txt").readLines().map { it.toList().map { it.toString() } }.toList()
    // Part 1
    val previousGens = mutableListOf<HashSet<String>>()
    var newGen = getNextGeneration(unpInput)
    previousGens.add(toHashSet(unpInput))
    previousGens.add(toHashSet(newGen))
    while (true) {
        newGen = getNextGeneration(newGen)
        if (previousGens.contains(toHashSet(newGen))) {
            var divRating = 0.0
            var currDivExp = 0
            for ((y, row) in newGen.withIndex()) {
                for ((x, el) in row.withIndex()) {
                    if (el == "#")
                        divRating += 2.0.pow(currDivExp)
                    currDivExp++
                }
            }
            println("Part 1: Biodiversity rating = ${divRating.toInt()}")
            break
        }
        previousGens.add(toHashSet(newGen))
    }

    // Part 2
    var levels = hashMapOf<Int, List<List<String>>>()
    levels[0] = unpInput
    var iterator = 0
    while (iterator < 200) {
        levels = getNextGenerationPart2(levels)
        iterator++
    }
    var amountOfBugs = 0
    for (level in levels.values) {
        for (row in level) {
            for (el in row) {
                if(el == "#") amountOfBugs++
            }
        }
    }
    println("Part 2: Amount of bugs after 200 iterations = $amountOfBugs")
}

fun getNextGenerationPart2(input: HashMap<Int, List<List<String>>>): HashMap<Int, List<List<String>>> {
    val minLvl = input.keys.min() ?: throw IllegalArgumentException()
    val maxLvl = input.keys.max() ?: throw IllegalArgumentException()
    input[minLvl - 1] = createEmptyLevel()
    input[maxLvl + 1] = createEmptyLevel()
    val output = hashMapOf<Int, List<List<String>>>()
    for (level in input) {
        val lvlNr = level.key
        val newLevel = mutableListOf<MutableList<String>>()
        for ((y, row) in level.value.withIndex()) {
            newLevel.add(mutableListOf())
            for ((x, el) in row.withIndex()) {
                var amountNeighbours = 0
                if (y != 0 && level.value[y - 1][x] == "#")
                    amountNeighbours++
                if (y != level.value.size - 1 && level.value[y + 1][x] == "#")
                    amountNeighbours++
                if (x != 0 && level.value[y][x - 1] == "#")
                    amountNeighbours++
                if (x != level.value[y].size - 1 && level.value[y][x + 1] == "#")
                    amountNeighbours++

                val dwnLvl = input.getOrDefault(lvlNr - 1, null)
                if (dwnLvl != null) {
                    if (x == 2 && y == 1)
                        amountNeighbours += getNeighboursFromWall(dwnLvl, "N")
                    if (x == 2 && y == 3)
                        amountNeighbours += getNeighboursFromWall(dwnLvl, "S")
                    if (x == 1 && y == 2)
                        amountNeighbours += getNeighboursFromWall(dwnLvl, "W")
                    if (x == 3 && y == 2)
                        amountNeighbours += getNeighboursFromWall(dwnLvl, "E")
                }

                val upLvl = input.getOrDefault(lvlNr + 1, null)
                if (upLvl != null) {
                    if (x == 0) {
                        if (upLvl[2][1] == "#") amountNeighbours++
                    }
                    if (y == 0) {
                        if (upLvl[1][2] == "#") amountNeighbours++
                    }
                    if (x == level.value[y].size - 1) {
                        if (upLvl[2][3] == "#") amountNeighbours++
                    }
                    if (y == level.value.size - 1) {
                        if (upLvl[3][2] == "#") amountNeighbours++
                    }
                }
                //println("Level $lvlNr: $x, $y has $amountNeighbours neighbours")
                if (x == 2 && y == 2)
                    newLevel[2].add(".")
                else
                    newLevel[y].add(getNewValue(el, amountNeighbours))

            }
        }
        output[lvlNr] = newLevel
    }
    return output
}

fun getNeighboursFromWall(input: List<List<String>>, flank: String): Int {
    if (flank == "N")
        return input.first().count { it == "#" }
    else if (flank == "S")
        return input.last().count { it == "#" }
    else if (flank == "W") {
        var amount = 0
        for (i in 0 until 5)
            if (input[i][0] == "#") amount++
        return amount
    } else if (flank == "E") {
        var amount = 0
        for (i in 0 until 5)
            if (input[i][input.lastIndex] == "#") amount++
        return amount
    }
    return 0
}


fun createEmptyLevel(): List<List<String>> {
    val output = mutableListOf<MutableList<String>>()
    for (i in 0 until 5) {
        output.add(mutableListOf(".", ".", ".", ".", "."))
    }
    return output
}

fun getNextGeneration(input: List<List<String>>): List<List<String>> {
    val output = mutableListOf<MutableList<String>>()
    for ((y, row) in input.withIndex()) {
        output.add(mutableListOf())
        for ((x, el) in row.withIndex()) {
            var amountNeighbours = 0
            if (y != 0 && input[y - 1][x] == "#")
                amountNeighbours++
            if (y != input.size - 1 && input[y + 1][x] == "#")
                amountNeighbours++
            if (x != 0 && input[y][x - 1] == "#")
                amountNeighbours++
            if (x != input[y].size - 1 && input[y][x + 1] == "#")
                amountNeighbours++

            output[y].add(getNewValue(el, amountNeighbours))
        }
    }
    return output
}

fun getNewValue(el: String, amountNeighbours: Int): String {
    if (el == "#") {
        if (amountNeighbours == 1)
            return "#"
        else
            return "."
    } else {
        if (amountNeighbours == 1 || amountNeighbours == 2)
            return "#"
        else
            return "."
    }
}

fun toHashSet(input: List<List<String>>): HashSet<String> {
    val output = hashSetOf<String>()
    for ((y, row) in input.withIndex()) {
        for ((x, el) in row.withIndex()) {
            if (el == ".") continue
            output.add("$x$y");
        }
    }
    return output
}

fun print(input: List<List<String>>) {
    for (row in input) {
        for (el in row) {
            print(el)
        }
        println()
    }
    println()
}

