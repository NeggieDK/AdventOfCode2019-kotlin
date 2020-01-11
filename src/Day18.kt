package day18

import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.system.measureTimeMillis

//Part1
val unparsedInput = File("sources\\inputs\\Day18.txt")
        .readLines()
        .map { it.trim() }
var indexInput = parseInput(unparsedInput).first
var elementInput = parseInput(unparsedInput).second

//Part2
val unparsedInputA = File("sources\\inputs\\Day18Part2A.txt")
        .readLines()
        .map { it.trim() }
val unparsedInputB = File("sources\\inputs\\Day18Part2B.txt")
        .readLines()
        .map { it.trim() }
val unparsedInputC = File("sources\\inputs\\Day18Part2C.txt")
        .readLines()
        .map { it.trim() }
val unparsedInputD = File("sources\\inputs\\Day18Part2D.txt")
        .readLines()
        .map { it.trim() }
var indexInputA = parseInput(unparsedInputA).first
var elementInputA = parseInput(unparsedInputA).second
var indexInputB = parseInput(unparsedInputB).first
var elementInputB = parseInput(unparsedInputB).second
var indexInputC = parseInput(unparsedInputC).first
var elementInputC = parseInput(unparsedInputC).second
var indexInputD = parseInput(unparsedInputD).first
var elementInputD = parseInput(unparsedInputD).second


fun main() {
    val time = measureTimeMillis {
        val startVault = Vault().apply {
            elementMap = elementInput
            indexMap = indexInput
        }
        val distanceBetweenKeys = startVault.createRelations() //740ms~
        val readyList = mutableListOf<Pair<KeyToCheck, Int>>()
        val root = KeyToCheck()
        val keysCache = HashMap<String, Int>()
        keysCache[root.toHashKey()] = 0
        val keysQueue = PriorityQueue<KeyToCheck>(KeyToCheckComparator)
        keysQueue.add(root)
        var currentMinStepsToCompletion = Int.MAX_VALUE
        var iterator = 0
        while (keysQueue.isNotEmpty() && readyList.isEmpty()) {
            iterator++
            val currentPoint = keysQueue.poll() ?: throw IllegalArgumentException()
            val currentPointSteps = keysCache[currentPoint.toHashKey()] ?: 0
            if (currentPointSteps >= currentMinStepsToCompletion) {
                continue
            }
            val keysInRange = getKeysInRange(distanceBetweenKeys, currentPoint)

            if (keysInRange.size == 0) {
                readyList.add(Pair(currentPoint, currentPointSteps))
                currentMinStepsToCompletion = currentPointSteps
            }

            for (key in keysInRange) {
                val newKeyToCheck = KeyToCheck()
                newKeyToCheck.keysTaken = HashSet(currentPoint.keysTaken)
                newKeyToCheck.keysTaken.add(key.key)
                newKeyToCheck.lastKeyTaken = key.key
                newKeyToCheck.priority = currentPointSteps + key.value.stepsAwayFromOrigin + newKeyToCheck.keysTaken.size
                if (!keysCache.contains(newKeyToCheck.toHashKey())) {
                    keysQueue.offer(newKeyToCheck)
                    keysCache[newKeyToCheck.toHashKey()] = currentPointSteps + key.value.stepsAwayFromOrigin
                } else {
                    val similarElementSteps = keysCache[newKeyToCheck.toHashKey()] ?: throw IllegalArgumentException()
                    if (similarElementSteps > currentPointSteps + key.value.stepsAwayFromOrigin) {
                        keysQueue.offer(newKeyToCheck)
                        keysCache[newKeyToCheck.toHashKey()] = currentPointSteps + key.value.stepsAwayFromOrigin
                    }
                }
            }
        }
        println("Part 1: $currentMinStepsToCompletion")
    }
    println("Time to run part1: $time")
    //Part2
    val time2 = measureTimeMillis {
        val vaultA = Vault().apply {
            elementMap = elementInputA
            indexMap = indexInputA
        }
        val vaultB = Vault().apply {
            elementMap = elementInputB
            indexMap = indexInputB
        }
        val vaultC = Vault().apply {
            elementMap = elementInputC
            indexMap = indexInputC
        }
        val vaultD = Vault().apply {
            elementMap = elementInputD
            indexMap = indexInputD
        }
        val distanceBetweenKeysA = vaultA.createRelations() //740ms~
        val distanceBetweenKeysB = vaultB.createRelations() //740ms~
        val distanceBetweenKeysC = vaultC.createRelations() //740ms~
        val distanceBetweenKeysD = vaultD.createRelations() //740ms~
        val readyList = mutableListOf<Pair<KeyToCheckPart2, Int>>()
        val root = KeyToCheckPart2()
        val keysCache = HashMap<String, Int>()
        keysCache[root.toHashKey()] = 0
        val keysQueue = PriorityQueue<KeyToCheckPart2>(KeyToCheckComparatorPart2)
        keysQueue.add(root)
        var currentMinStepsToCompletion = Int.MAX_VALUE
        var iterator = 0
        while (keysQueue.isNotEmpty()) {
            iterator++
            val currentPoint = keysQueue.poll() ?: throw IllegalArgumentException()
            val currentPointSteps = keysCache[currentPoint.toHashKey()] ?: 0
            if (currentPointSteps >= currentMinStepsToCompletion) {
                continue
            }
            val keysInRangeA = getKeysInRangePart2(distanceBetweenKeysA, currentPoint, "A")
            keysInRangeA[null] = null
            val keysInRangeB = getKeysInRangePart2(distanceBetweenKeysB, currentPoint, "B")
            keysInRangeB[null] = null
            val keysInRangeC = getKeysInRangePart2(distanceBetweenKeysC, currentPoint, "C")
            keysInRangeC[null] = null
            val keysInRangeD = getKeysInRangePart2(distanceBetweenKeysD, currentPoint, "D")
            keysInRangeD[null] = null

            if (currentPoint.keysTaken.size == 27) {
                //2414 too high
                readyList.add(Pair(currentPoint, currentPointSteps))
                currentMinStepsToCompletion = currentPointSteps
            }

            for (keyA in keysInRangeA) {
                for(keyB in keysInRangeB){
                    for(keyC in keysInRangeC){
                        for(keyD in keysInRangeD){
                            if(keyA.key == null && keyB.key == null && keyC.key == null && keyD.key == null) continue
                            val newKeyToCheck = KeyToCheckPart2()
                            newKeyToCheck.keysTaken = HashSet(currentPoint.keysTaken)
                            if(keyA.key != null){
                                newKeyToCheck.keysTaken.add(keyA.key!!)
                                newKeyToCheck.lastKeyTakenA = keyA.key!!
                            }else{
                                newKeyToCheck.lastKeyTakenA = currentPoint.lastKeyTakenA
                            }
                            if(keyB.key != null){
                                newKeyToCheck.keysTaken.add(keyB.key!!)
                                newKeyToCheck.lastKeyTakenB = keyB.key!!
                            }else{
                                newKeyToCheck.lastKeyTakenB = currentPoint.lastKeyTakenB
                            }
                            if(keyC.key != null){
                                newKeyToCheck.lastKeyTakenC = keyC.key!!
                                newKeyToCheck.keysTaken.add(keyC.key!!)
                            }else{
                                newKeyToCheck.lastKeyTakenC = currentPoint.lastKeyTakenC
                            }
                            if(keyD.key != null) {
                                newKeyToCheck.lastKeyTakenD = keyD.key!!
                                newKeyToCheck.keysTaken.add(keyD.key!!)
                            }else{
                                newKeyToCheck.lastKeyTakenD = currentPoint.lastKeyTakenD
                            }

                            var totalSteps = keyA.value?.stepsAwayFromOrigin ?: 0
                            totalSteps += keyB.value?.stepsAwayFromOrigin ?: 0
                            totalSteps += keyC.value?.stepsAwayFromOrigin ?: 0
                            totalSteps += keyD.value?.stepsAwayFromOrigin ?: 0
                            if (!keysCache.contains(newKeyToCheck.toHashKey())) {
                                keysQueue.offer(newKeyToCheck)
                                keysCache[newKeyToCheck.toHashKey()] = currentPointSteps + totalSteps
                            } else {
                                val similarElementSteps = keysCache[newKeyToCheck.toHashKey()] ?: throw IllegalArgumentException()
                                if (similarElementSteps > currentPointSteps + totalSteps) {
                                    keysQueue.offer(newKeyToCheck)
                                    keysCache[newKeyToCheck.toHashKey()] = currentPointSteps + totalSteps
                                }
                            }
                        }
                    }
                }
                //newKeyToCheck.priority = currentPointSteps + key.value.stepsAwayFromOrigin + newKeyToCheck.keysTaken.size
            }
        }
        println("Part 2: $currentMinStepsToCompletion")
    }
}

fun getKeysInRange(distanceBetweenKeys: HashMap<Pair<String, String>, PointOfInterest>, currentPoint: KeyToCheck): HashMap<String, PointOfInterest>{
    val keysInRange = HashMap<String, PointOfInterest>()
    val temp =
            distanceBetweenKeys.filter { (it.key.second == currentPoint.lastKeyTaken || it.key.first == currentPoint.lastKeyTaken) }
    for ((key, value) in temp) {
        val toPointKey = if (key.first == currentPoint.lastKeyTaken)
            key.second
        else
            key.first

        if (currentPoint.keysTaken.contains(toPointKey)) continue
        if (value.doorsEncountered.size == 0 || currentPoint.keysTaken.containsAll(value.doorsEncountered.map { it.toLowerCase() }))
            keysInRange[toPointKey] = value
    }
    return keysInRange
}

fun getKeysInRangePart2(distanceBetweenKeys: HashMap<Pair<String, String>, PointOfInterest>, currentPoint: KeyToCheckPart2, part: String): HashMap<String?, PointOfInterest?>{
    val keysInRange = HashMap<String?, PointOfInterest?>()
    val lastKeyTaken = when (part) {
        "A" -> currentPoint.lastKeyTakenA
        "B" -> currentPoint.lastKeyTakenB
        "C" -> currentPoint.lastKeyTakenC
        else -> currentPoint.lastKeyTakenD
    }

    val temp =
            distanceBetweenKeys.filter { (it.key.second == lastKeyTaken || it.key.first == lastKeyTaken) }
    for ((key, value) in temp) {
        val toPointKey = if (key.first == lastKeyTaken)
            key.second
        else
            key.first

        if (currentPoint.keysTaken.contains(toPointKey)) continue
        if (value.doorsEncountered.size == 0 || currentPoint.keysTaken.containsAll(value.doorsEncountered.map { it.toLowerCase() }))
            keysInRange[toPointKey] = value
    }
    return keysInRange
}

class KeyToCheck {
    constructor() {
        keysTaken.add("@")
    }

    var lastKeyTaken = "@"
    var keysTaken = HashSet<String>()
    var priority = 0

    fun toHashKey(): String {
        var tempKey = ""
        keysTaken.sorted().forEach {
            tempKey += "$it;"
        }
        return "$tempKey%$lastKeyTaken"
    }
}

class KeyToCheckPart2 {
    constructor() {
        keysTaken.add("@")
    }

    var lastKeyTakenA = "@"
    var lastKeyTakenB = "@"
    var lastKeyTakenC = "@"
    var lastKeyTakenD = "@"
    var keysTaken = HashSet<String>()
    var priority = 0

    fun toHashKey(): String {
        var tempKey = ""
        keysTaken.sorted().forEach {
            tempKey += "$it;"
        }
        return "$tempKey%$lastKeyTakenA%$lastKeyTakenB%$lastKeyTakenC%$lastKeyTakenD"
    }
}

fun parseInput(unpInput: List<String>): Pair<HashMap<Pair<Int, Int>, String>, HashMap<String, String>> {
    val tempMap = HashMap<Pair<Int, Int>, String>()
    val tempMap2 = HashMap<String, String>()
    for ((y, line) in unpInput.withIndex()) {
        for ((x, element) in line.withIndex()) {
            tempMap[Pair(x, y)] = element.toString().trim()
            tempMap2[element.toString().trim()] = "$x,$y"
        }
    }
    return Pair(tempMap, tempMap2)
}

class Vault {
    var indexMap = HashMap<Pair<Int, Int>, String>()
    var elementMap = HashMap<String, String>()

    //Gets the distance between all possible key combinations and if the robot has to pass a door the get there
    fun createRelations(): HashMap<Pair<String, String>, PointOfInterest> {
        val keys = HashMap<Pair<String, String>, PointOfInterest>()
        for (keyFrom in elementMap.keys.filter { (it[0].isLetter() && !it[0].isUpperCase()) || it == "@" }) {
            for (keyTo in elementMap.keys.filter { (it[0].isLetter() && !it[0].isUpperCase()) || it == "@" }) {
                if (keyFrom == keyTo) continue
                if (keys.containsKey(Pair(keyTo, keyFrom)) || keys.containsKey(Pair(keyFrom, keyTo))) continue
                keys[Pair(keyFrom, keyTo)] = shortestDistance(keyFrom, keyTo) ?: throw IllegalArgumentException()
            }
        }
        return keys
    }

    private fun shortestDistance(fromKey: String, toKey: String): PointOfInterest? {
        val openList = PriorityQueue<PointOfInterest>(pointOfInterestAStarComparator)
        val closeList = HashSet<Pair<Int, Int>>()
        val rootElement = indexMap.filter { it.value == fromKey }.toList().first()
        val destElement = indexMap.filter { it.value == toKey }.toList().first()
        val root = PointOfInterest(rootElement.first.first, rootElement.first.second)
        openList.offer(root)
        while (openList.isNotEmpty()) {
            val currentElement = openList.poll() ?: throw IllegalArgumentException()
            val currentElementValue = indexMap[Pair(currentElement.x, currentElement.y)]
                    ?: throw IllegalArgumentException()
            closeList.add(Pair(currentElement.x, currentElement.y))
            if (currentElementValue == toKey) return currentElement
            else if (currentElementValue[0].isLetter() && currentElementValue[0].isUpperCase()) currentElement.doorsEncountered.add(currentElementValue)
            if (isElement(currentElement.x, currentElement.y - 1, closeList)) { //North
                val newPoint = PointOfInterest(currentElement.x, currentElement.y - 1, currentElement.stepsAwayFromOrigin + 1).apply {
                    directions = currentElement.directions.toMutableList()
                    doorsEncountered = currentElement.doorsEncountered.toMutableList()
                }
                newPoint.calculatePriority(destElement.first.first, destElement.first.second)
                newPoint.directions.add("N")
                openList.offer(newPoint)
            }
            if (isElement(currentElement.x, currentElement.y + 1, closeList)) { //North
                val newPoint = PointOfInterest(currentElement.x, currentElement.y + 1, currentElement.stepsAwayFromOrigin + 1).apply {
                    directions = currentElement.directions.toMutableList()
                    doorsEncountered = currentElement.doorsEncountered.toMutableList()
                }
                newPoint.calculatePriority(destElement.first.first, destElement.first.second)
                newPoint.directions.add("S")
                openList.offer(newPoint)
            }
            if (isElement(currentElement.x - 1, currentElement.y, closeList)) { //North
                val newPoint = PointOfInterest(currentElement.x - 1, currentElement.y, currentElement.stepsAwayFromOrigin + 1).apply {
                    directions = currentElement.directions.toMutableList()
                    doorsEncountered = currentElement.doorsEncountered.toMutableList()
                }
                newPoint.calculatePriority(destElement.first.first, destElement.first.second)
                newPoint.directions.add("W")
                openList.offer(newPoint)
            }
            if (isElement(currentElement.x + 1, currentElement.y, closeList)) { //North
                val newPoint = PointOfInterest(currentElement.x + 1, currentElement.y, currentElement.stepsAwayFromOrigin + 1).apply {
                    directions = currentElement.directions.toMutableList()
                    doorsEncountered = currentElement.doorsEncountered.toMutableList()
                }
                newPoint.calculatePriority(destElement.first.first, destElement.first.second)
                newPoint.directions.add("E")
                openList.offer(newPoint)
            }
        }
        return null
    }

    private fun isElement(x: Int, y: Int, pointsAlreadyChecked: HashSet<Pair<Int, Int>>): Boolean {
        val element = indexMap[Pair(x, y)]
        return element != null && element != "#" && !pointsAlreadyChecked.contains(Pair(x, y))
    }
}

class PointOfInterest {
    constructor(newX: Int, newY: Int, steps: Int) {
        x = newX
        y = newY
        stepsAwayFromOrigin = steps
    }

    constructor(newX: Int, newY: Int) {
        x = newX
        y = newY
        priority = 0
    }

    var x = 0
    var y = 0
    var priority = 0
    var stepsAwayFromOrigin = 0
    var doorsEncountered = mutableListOf<String>()
    var directions = mutableListOf<String>()

    fun calculatePriority(destX: Int, destY: Int) {
        priority = stepsAwayFromOrigin + StrictMath.abs(x - destX) + StrictMath.abs(y - destY)
    }
}

var pointOfInterestAStarComparator = Comparator<PointOfInterest> { s1, s2 -> s1.priority - s2.priority }
var KeyToCheckComparator = Comparator<KeyToCheck> { s1, s2 -> s1.priority - s2.priority }
var KeyToCheckComparatorPart2 = Comparator<KeyToCheckPart2> { s1, s2 -> s1.priority - s2.priority }
