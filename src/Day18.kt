package day18

import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.system.measureTimeMillis

val unparsedInput = File("C:\\Users\\aarondk\\Kotlin Projects\\Test\\sources\\inputs\\Day18.txt")
    .readLines()
    .map { it.trim() }
var indexInput = parseInput(unparsedInput).first
var elementInput = parseInput(unparsedInput).second

fun main(){
    val startVault = Vault().apply {
        elementMap = elementInput
        indexMap = indexInput
    }
    val distanceBetweenKeys =  startVault.createRelations()
    val pathsToAllKeys = mutableListOf<KeyToCheck>()
    val root = KeyToCheck("@", 0)
    root.keysTaken.add("@")
    val keysQueue = ArrayDeque<KeyToCheck>()
    keysQueue.offer(root)

    // This works but takes long and a lot of memory, for the first test case that fails we (possibly) have 12! combinations (500million)
    //But there is an optimalization we can make: if you have keys A-B-C-D taken, and B-A-D-C keys taken. We only have to calculate the case where the steps is the lowest
    //Because the same amount of keys have been taken but less steps have been used
    while(keysQueue.isNotEmpty()){
        var test = measureTimeMillis {
            val currentKey = keysQueue.poll() ?: throw IllegalArgumentException()
            val keysInRange = HashMap<String, PointOfInterest>()
            val temp =
                distanceBetweenKeys.filter { (it.key.second == currentKey.key || it.key.first == currentKey.key) }
            for ((key, value) in temp) {
                val keyName = if (key.first == currentKey.key)
                    key.second
                else
                    key.first
                if (currentKey.keysTaken.contains(keyName)) continue
                if (value.doorsEncountered.size == 0 || currentKey.doorsOpened.containsAll(value.doorsEncountered))
                    keysInRange[keyName] = value
            }
            if (keysInRange.size == 0) {
                pathsToAllKeys.add(currentKey)
            }
            for (key in keysInRange) {
                val doors = HashSet<String>(currentKey.doorsOpened)
                val keys = HashSet<String>(currentKey.keysTaken)
                doors.add(key.key.toUpperCase())
                keys.add(key.key)
                keysQueue.add(KeyToCheck(key.key, key.value.directions.size + currentKey.stepsTaken, doors, keys))
            }
        }
        //println()
    }
    println("Part1: amount of steps ${pathsToAllKeys.minBy { it.stepsTaken }?.stepsTaken}")
}

class KeyToCheck{
    constructor(newKey: String, steps: Int, doors: HashSet<String>, keys: HashSet<String>){
        key = newKey
        stepsTaken = steps
        doorsOpened = doors
        keysTaken = keys
    }

    constructor(newKey: String, steps: Int){
        key = newKey
        stepsTaken = steps
    }
    var key = ""
    var stepsTaken = 0
    var doorsOpened = HashSet<String>()
    var keysTaken = HashSet<String>()
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
