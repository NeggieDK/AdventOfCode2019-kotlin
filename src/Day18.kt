package day18

import java.io.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.test.currentStackTrace

val unparsedInput = File("C:\\Users\\aarondk\\Kotlin Projects\\Test\\sources\\inputs\\Day18.txt")
    .readLines()
    .map { it.trim() }
var indexInput = parseInput(unparsedInput).first
var elementInput = parseInput(unparsedInput).second

fun main(){
    val paths = getPossiblePathsToAllKeys()
    println("Part 1: the least amount of steps needed is ${paths.map { it.stepsTaken }.min()}")
}

fun getPossiblePathsToAllKeys(): List<Vault>{
    val startVault = Vault().apply {
        elementMap = elementInput
        indexMap = indexInput
    }
    val proccessedList = mutableListOf<Vault>()
    val processQueue = ArrayDeque<Vault>()
    processQueue.offer(startVault)
    while(processQueue.isNotEmpty()){
        val currentVault = processQueue.poll() ?: continue
        val keysInRange = currentVault.getKeysInRange()
        if(keysInRange.isEmpty()) {
            proccessedList.add(currentVault)
            continue
        }
        keysInRange.forEach {
            val newVault = deepCopy(currentVault) ?: throw IllegalArgumentException()
            newVault.takeKeyAndOpenDoor(it)
            newVault.stepsTaken+= it.second //This is not the correct amount of steps
            processQueue.offer(newVault)
        }
    }
    return proccessedList
}

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

class Vault : java.io.Serializable{
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

    fun getKeysInRange(): List<Pair<String, Int>>{
        //also return the distance from the original point so that stepstaken is already calculated here
        val currentLocation = elementMap["@"] ?: throw IllegalArgumentException()
        val pointsToInspect = ArrayDeque<Pair<String, Int>>()
        pointsToInspect.offer(Pair(currentLocation,0))
        val keysInRange = mutableListOf<Pair<String, Int>>()
        val pointsChecked = HashSet<String>()
        while(pointsToInspect.isNotEmpty()){
            val currentElementIndex = pointsToInspect.poll() ?: throw IllegalArgumentException()
            pointsChecked.add(currentElementIndex.first)
            val currentElement = indexMap[currentElementIndex.first] ?: throw IllegalArgumentException()
            if(currentElement[0].isLetter() && currentElement[0].isLowerCase())
                keysInRange.add(Pair(currentElement, currentElementIndex.second))
            else if(currentElement[0].isLetter() && currentElement[0].isUpperCase())
                continue
            else{
                val currentElementLocation = currentElementIndex.first.split(",").map { it.trim().toInt() }
                val x = currentElementLocation[0]
                val y = currentElementLocation[1]
                val steps = currentElementIndex.second +1
                if(indexMap["${x+1},$y"] != null && indexMap["${x+1},$y"] != "#" && !pointsChecked.contains("${x+1},$y")){//East

                    pointsToInspect.offer(Pair("${x+1},$y", steps))
                }
                if(indexMap["${x-1},$y"] != null && indexMap["${x-1},$y"] != "#" && !pointsChecked.contains("${x-1},$y")){//West
                    pointsToInspect.offer(Pair("${x-1},$y", steps))
                }
                if(indexMap["$x,${y+1}"] != null && indexMap["$x,${y+1}"] != "#" && !pointsChecked.contains("$x,${y+1}")){//South
                    pointsToInspect.offer(Pair("$x,${y+1}", steps))
                }
                if(indexMap["$x,${y-1}"] != null && indexMap["$x,${y-1}"] != "#" && !pointsChecked.contains("$x,${y-1}")){//North
                    pointsToInspect.offer(Pair("$x,${y-1}", steps))
                }
            }
        }
        return keysInRange.distinct()
    }
}

fun <T : java.io.Serializable> deepCopy(obj: T?): T? {
    if (obj == null) return null
    val baos = ByteArrayOutputStream()
    val oos  = ObjectOutputStream(baos)
    oos.writeObject(obj)
    oos.close()
    val bais = ByteArrayInputStream(baos.toByteArray())
    val ois  = ObjectInputStream(bais)
    @Suppress("unchecked_cast")
    return ois.readObject() as T
}
