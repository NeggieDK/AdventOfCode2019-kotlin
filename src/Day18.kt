package day18

import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.system.measureTimeMillis
import kotlin.test.currentStackTrace

val unparsedInput = File("sources\\inputs\\Day18.txt")
    .readLines()
    .map { it.trim() }
var indexInput = parseInput(unparsedInput).first
var elementInput = parseInput(unparsedInput).second

fun main() {
    val startVault = Vault().apply {
        elementMap = elementInput
        indexMap = indexInput
    }
    val distanceBetweenKeys = startVault.createRelations() //740ms~
    val readyList = mutableListOf<Pair<KeyToCheck, Int>>()
    val root = KeyToCheck()
    val keysCache = HashMap<String, Int>()
    keysCache[root.toHashKey()] = 0
    val keysQueue = Stack<KeyToCheck>()
    keysQueue.add(root)
    var currentMinStepsToCompletion = Int.MAX_VALUE
    while(keysQueue.isNotEmpty()){
            val currentPoint = keysQueue.pop() ?: throw IllegalArgumentException()
            val currentPointSteps = keysCache[currentPoint.toHashKey()] ?: 0
            if(currentPointSteps >= currentMinStepsToCompletion) {
                continue
            }
            val keysInRange = HashMap<String, PointOfInterest>()
            val temp =
                distanceBetweenKeys.filter { (it.key.second == currentPoint.lastKeyTaken || it.key.first == currentPoint.lastKeyTaken) }
            for ((key, value) in temp) {
                val toPointKey = if (key.first == currentPoint.lastKeyTaken)
                    key.second
                else
                    key.first

                if(currentPoint.keysTaken.contains(toPointKey)) continue
                if (value.doorsEncountered.size == 0 || currentPoint.keysTaken.containsAll(value.doorsEncountered.map { it.toLowerCase()}))
                    keysInRange[toPointKey] = value
            }

            if(keysInRange.size == 0){
                readyList.add(Pair(currentPoint, currentPointSteps))
                if(currentMinStepsToCompletion > currentPointSteps){
                    currentMinStepsToCompletion = currentPointSteps
                }
            }

            for (key in keysInRange) {
                val newKeyToCheck = KeyToCheck()
                newKeyToCheck.keysTaken = HashSet(currentPoint.keysTaken)
                newKeyToCheck.keysTaken.add(key.key)
                newKeyToCheck.lastKeyTaken = key.key
                if(!keysCache.contains(newKeyToCheck.toHashKey())){
                    keysQueue.push(newKeyToCheck)
                    keysCache[newKeyToCheck.toHashKey()] = currentPointSteps + key.value.stepsAwayFromOrigin
                }
                else{
                    //println("CacheHit")
                    val similarElementSteps = keysCache[newKeyToCheck.toHashKey()] ?: throw IllegalArgumentException()
                    if(similarElementSteps > currentPointSteps + key.value.stepsAwayFromOrigin){
                        keysQueue.push(newKeyToCheck)
                        keysCache[newKeyToCheck.toHashKey()] = currentPointSteps + key.value.stepsAwayFromOrigin
                    }
                }
            }
    }
    println("Part 1: $currentMinStepsToCompletion")
    //println("Part1: amount of steps ${pathsToAllKeys.minBy { it.stepsTaken }?.stepsTaken}")
}

class KeyToCheck{
    constructor(){
        keysTaken.add("@")
    }
    var lastKeyTaken = "@"
    var keysTaken = HashSet<String>()

    fun toHashKey() : String{
        var tempKey = ""
        keysTaken.sorted().forEach {
            tempKey += "$it;"
        }
        return "$tempKey%$lastKeyTaken"
    }
}

fun parseInput(unpInput: List<String>): Pair<HashMap<Pair<Int, Int>, String>, HashMap<String, String>>{
    val tempMap = HashMap<Pair<Int, Int>, String>()
    val tempMap2 = HashMap<String, String>()
    for ((y, line) in unparsedInput.withIndex()) {
        for((x, element) in line.withIndex()){
            tempMap[Pair(x,y)] = element.toString().trim()
            tempMap2[element.toString().trim()] = "$x,$y"
        }
    }
    return Pair(tempMap, tempMap2)
}

class Vault{
    var indexMap = HashMap<Pair<Int, Int>, String>()
    var elementMap = HashMap<String, String>()

    //Gets the distance between all possible key combinations and if the robot has to pass a door the get there
    fun createRelations(): HashMap<Pair<String, String>, PointOfInterest>{
        val keys = HashMap<Pair<String, String>, PointOfInterest>()
        for(keyFrom in elementMap.keys.filter { (it[0].isLetter() && !it[0].isUpperCase()) || it == "@" }){
            for(keyTo in elementMap.keys.filter { (it[0].isLetter() && !it[0].isUpperCase()) || it == "@"  }){
                if(keyFrom == keyTo) continue
                if(keys.containsKey(Pair(keyTo, keyFrom)) || keys.containsKey(Pair(keyFrom, keyTo))) continue
                keys[Pair(keyFrom, keyTo)] = shortestDistance(keyFrom, keyTo) ?: throw IllegalArgumentException()
            }
        }
        return keys
    }

    private fun shortestDistance(fromKey: String, toKey: String): PointOfInterest?{
        val openList = PriorityQueue<PointOfInterest>(pointOfInterestAStarComparator)
        val closeList = HashSet<Pair<Int, Int>>()
        val rootElement = indexMap.filter { it.value==fromKey }.toList().first()
        val destElement = indexMap.filter { it.value==toKey }.toList().first()
        val root = PointOfInterest(rootElement.first.first,rootElement.first.second)
        openList.offer(root)
        while(openList.isNotEmpty()){
            val currentElement = openList.poll() ?: throw IllegalArgumentException()
            val currentElementValue = indexMap[Pair(currentElement.x, currentElement.y)] ?: throw IllegalArgumentException()
            closeList.add(Pair(currentElement.x, currentElement.y))
            if(currentElementValue == toKey)return currentElement
            else if(currentElementValue[0].isLetter() && currentElementValue[0].isUpperCase()) currentElement.doorsEncountered.add(currentElementValue)
            if(isElement(currentElement.x, currentElement.y-1, closeList)){ //North
                val newPoint = PointOfInterest(currentElement.x, currentElement.y-1, currentElement.stepsAwayFromOrigin + 1).apply {
                    directions = currentElement.directions.toMutableList()
                    doorsEncountered = currentElement.doorsEncountered.toMutableList()
                }
                newPoint.calculatePriority(destElement.first.first, destElement.first.second)
                newPoint.directions.add("N")
                openList.offer(newPoint)
            }
            if(isElement(currentElement.x, currentElement.y+1, closeList)){ //North
                val newPoint = PointOfInterest(currentElement.x, currentElement.y+1, currentElement.stepsAwayFromOrigin + 1).apply {
                    directions = currentElement.directions.toMutableList()
                    doorsEncountered = currentElement.doorsEncountered.toMutableList()
                }
                newPoint.calculatePriority(destElement.first.first, destElement.first.second)
                newPoint.directions.add("S")
                openList.offer(newPoint)
            }
            if(isElement(currentElement.x-1, currentElement.y, closeList)){ //North
                val newPoint = PointOfInterest(currentElement.x-1, currentElement.y, currentElement.stepsAwayFromOrigin + 1).apply {
                    directions = currentElement.directions.toMutableList()
                    doorsEncountered = currentElement.doorsEncountered.toMutableList()
                }
                newPoint.calculatePriority(destElement.first.first, destElement.first.second)
                newPoint.directions.add("W")
                openList.offer(newPoint)
            }
            if(isElement(currentElement.x+1, currentElement.y, closeList)){ //North
                val newPoint = PointOfInterest(currentElement.x+1, currentElement.y, currentElement.stepsAwayFromOrigin + 1).apply {
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

    private fun isElement(x: Int, y: Int, pointsAlreadyChecked: HashSet<Pair<Int, Int>>): Boolean{
        val element = indexMap[Pair(x,y)]
        return element != null && element != "#" && !pointsAlreadyChecked.contains(Pair(x,y))
    }
}

class PointOfInterest{
    constructor(newX: Int, newY: Int, steps: Int){
        x = newX
        y = newY
        stepsAwayFromOrigin = steps
    }
    constructor(newX: Int, newY: Int){
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

    fun calculatePriority(destX : Int, destY: Int){
        priority = stepsAwayFromOrigin + StrictMath.abs(x - destX) + StrictMath.abs(y - destY)
    }
}

var pointOfInterestAStarComparator = Comparator<PointOfInterest> { s1, s2 -> s1.priority - s2.priority }
