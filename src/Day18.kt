package day18

import java.io.*
import java.util.*
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
    startVault.createGraph()
    //val paths = getPossiblePathsToAllKeys()
    //println("Part 1: the least amount of steps needed is ${paths.map { it.stepsTaken }.min()}")
}

//fun getPossiblePathsToAllKeys(): List<Vault>{
/*    val startVault = Vault().apply {
        elementMap = elementInput
        indexMap = indexInput
    }
    val proccessedList = mutableListOf<Vault>()
    val processQueue = ArrayDeque<Vault>()
    processQueue.offer(startVault)
    while(processQueue.isNotEmpty()){
        val currentVault = processQueue.poll() ?: continue
        val keysInRange = currentVault.getKeysInRange("@", HashSet<String>())
        if(keysInRange.isEmpty()) {
            proccessedList.add(currentVault)
            continue
        }
        keysInRange.forEach {
            val newVault = Vault().apply {
                stepsTaken = currentVault.stepsTaken + it.second
                keysInOrderTaken = currentVault.keysInOrderTaken.toMutableList()
                indexMap = HashMap(currentVault.indexMap)
                elementMap = HashMap(currentVault.elementMap)
            }
            newVault.takeKeyAndOpenDoor(it)
            processQueue.offer(newVault)
        }
    }
    return proccessedList*/
//}

fun parseInput(unpInput: List<String>): Pair<HashMap<String, String>, HashMap<String, String>>{
    val tempMap = HashMap<String, String>()
    val tempMap2 = HashMap<String, String>()
    for ((y, line) in unparsedInput.withIndex()) {
        for((x, element) in line.withIndex()){
            tempMap["$x,$y"] = element.toString().trim()
            tempMap2[element.toString().trim()] = "$x,$y"
        }
    }
    return Pair(tempMap, tempMap2)
}

class Vault{
    var indexMap = HashMap<String, String>()
    var elementMap = HashMap<String, String>()
    var keysInOrderTaken = mutableListOf<String>()
    var stepsTaken = 0

    fun takeKeyAndOpenDoor(key: Pair<String, Int>){
        val keyIndex = elementMap[key.first] ?: throw IllegalArgumentException()
        val doorIndex = elementMap[key.first.toUpperCase()] ?: ""
        val currentLocation = elementMap["@"]  ?: throw IllegalArgumentException()
        keysInOrderTaken.add(key.first)
        elementMap.remove(key.first)
        elementMap.remove(key.first.toUpperCase())
        elementMap["@"] = keyIndex
        indexMap[currentLocation] = "."
        indexMap[doorIndex] = "."
        indexMap[keyIndex] = "@"
    }

    fun createGraph(): HashMap<String, PointOfInterest>{
        val points = hashMapOf(Pair("@", getKeysInRange("@")))
        var time = measureTimeMillis {
            for (key in elementMap.keys.filter { it[0].isLetter() && it[0].isLowerCase() }) {
                points[key] = getKeysInRange(key)
            }
        }
        println(time)
        return points
        //FIXME: @ -> a -> b -> c
        //FIXME: Option 1: c -> d -> e -> f
        //FIXME: Option 2: c -> e -> d -> f

        //With the PoI above we can check fast which keys are in range, and we can caul
    }

    fun getKeysInRange(key: String): PointOfInterest{
        val rootValue = elementMap[key]?.split(",")?.map { it.toInt() } ?: throw IllegalArgumentException()
        val root = PointOfInterest(key, rootValue[0], rootValue[1])
        while(root.pointsToCheck.isNotEmpty()){
            val pointToCheck = root.pointsToCheck.poll() ?: throw IllegalArgumentException()
            val pointToCheckValue = indexMap["${pointToCheck.x},${pointToCheck.y}"]
            if(pointToCheckValue != null && pointToCheckValue[0].isLetter() && pointToCheckValue[0].isLowerCase() && pointToCheck.pointsChecked.isNotEmpty())
               root.keysAndDoors[pointToCheckValue] = pointToCheck.doorsEncountered
            else{
                if(pointToCheckValue != null && pointToCheckValue[0].isLetter() && pointToCheckValue[0].isUpperCase() && pointToCheckValue != key.toUpperCase())
                    pointToCheck.doorsEncountered.add(pointToCheckValue)
                pointToCheck.updatePointsChecked()
                //Add steps
                if(isElement(pointToCheck.x+1,pointToCheck.y, pointToCheck.pointsChecked)){//East
                    val newCheckPoint = CheckPoint().apply {
                        x = pointToCheck.x+1
                        y = pointToCheck.y
                        doorsEncountered = pointToCheck.doorsEncountered.toMutableList()
                        pointsChecked = pointToCheck.pointsChecked.toHashSet()
                    }
                    root.pointsToCheck.offer(newCheckPoint)
                }
                if(isElement(pointToCheck.x-1,pointToCheck.y, pointToCheck.pointsChecked)){//West
                    val newCheckPoint = CheckPoint().apply {
                        x = pointToCheck.x-1
                        y = pointToCheck.y
                        doorsEncountered = pointToCheck.doorsEncountered.toMutableList()
                        pointsChecked = pointToCheck.pointsChecked.toHashSet()
                    }
                    root.pointsToCheck.offer(newCheckPoint)
                }
                if(isElement(pointToCheck.x,pointToCheck.y+1, pointToCheck.pointsChecked)){//South
                    val newCheckPoint = CheckPoint().apply {
                        x = pointToCheck.x
                        y = pointToCheck.y+1
                        doorsEncountered = pointToCheck.doorsEncountered.toMutableList()
                        pointsChecked = pointToCheck.pointsChecked.toHashSet()
                    }
                    root.pointsToCheck.offer(newCheckPoint)
                }
                if(isElement(pointToCheck.x,pointToCheck.y-1, pointToCheck.pointsChecked)){//North
                    val newCheckPoint = CheckPoint().apply {
                        x = pointToCheck.x
                        y = pointToCheck.y-1
                        doorsEncountered = pointToCheck.doorsEncountered.toMutableList()
                        pointsChecked = pointToCheck.pointsChecked.toHashSet()
                    }
                    root.pointsToCheck.offer(newCheckPoint)
                }
            }
        }
        return root
    }

    private fun isElement(x: Int, y: Int, pointsAlreadyChecked: HashSet<String>): Boolean{
        val element = indexMap["${x},$y"]
        return element != null && element != "#" && !pointsAlreadyChecked.contains("${x},$y")
    }
}

class PointOfInterest{
    constructor(key: String, x: Int, y: Int){
        StartingPoint = key
        pointsToCheck.add((CheckPoint(x, y)))
    }

    var keysAndDoors = hashMapOf<String, List<String>>()
    var StartingPoint = ""
    var steps = 0
    var pointsToCheck = ArrayDeque<CheckPoint>()
}

class CheckPoint{
    constructor(newX: Int, newY: Int){
        x = newX
        y = newY
    }

    constructor()
    var x = 0
    var y = 0
    var doorsEncountered = mutableListOf<String>()
    var pointsChecked = hashSetOf<String>()

    fun updatePointsChecked(){
        pointsChecked.add("$x,$y")
    }
}
