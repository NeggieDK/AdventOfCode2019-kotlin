package day24

import java.io.File
import kotlin.math.pow

fun main() {
    val unpInput = File("sources\\inputs\\Day24.txt").readLines().map { it.toList().map { it.toString() } }.toList()
    val previousGens = mutableListOf<HashSet<String>>()
    var newGen = getNextGeneration(unpInput)
    previousGens.add(toHashSet(unpInput))
    previousGens.add(toHashSet(newGen))
    while (true) {
        newGen = getNextGeneration(newGen)
        if(previousGens.contains(toHashSet(newGen))){
            var divRating = 0.0
            var currDivExp = 0
            for ((y, row) in newGen.withIndex()) {
                for ((x, el) in row.withIndex()) {
                    if(el == "#")
                        divRating += 2.0.pow(currDivExp)
                    currDivExp++
                }
            }
            println("Part 1: Biodiversity rating = ${divRating.toInt()}")
        break
        }
        previousGens.add(toHashSet(newGen))
    }
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

            if (el == "#") {
                if (amountNeighbours == 1)
                    output[y].add("#")
                else
                    output[y].add(".")
            } else {
                if (amountNeighbours == 1 || amountNeighbours == 2)
                    output[y].add("#")
                else
                    output[y].add(".")
            }
        }
    }
    return output
}

fun toHashSet(input: List<List<String>>) : HashSet<String>{
    val output = hashSetOf<String>()
    for ((y, row) in input.withIndex()) {
        for ((x, el) in row.withIndex()) {
            if(el == ".") continue
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

