import java.io.File
import kotlin.system.measureTimeMillis

var orbitInstruction = mutableListOf<String>()
var spaceObjects = hashMapOf<String, SpaceObject>()

fun main(){
    val time = measureTimeMillis {
        parseInput()
        createSpaceObjects()
        var totalOrbits = 0
        spaceObjects.keys.forEach {
            totalOrbits += getOrbitChain(it)
        }
        println("Total amount of direct and indirect orbits: $totalOrbits")
    }
    println("Execution time: $time")
    val time1 = measureTimeMillis {
        val pathYou =getChainToCom("YOU") //Gets the list of objects until COM
        val pathSanta = getChainToCom("SAN")
        val objectsInCommon = mutableListOf<SpaceObject>()
        pathYou.forEach {
            objectsInCommon.addAll(pathSanta.filter { it2 -> it.name == it2.name && it.name != "COM" }) //The objects that the two paths have in common are the crosspoints where the fastest path will have to go through
        }
        val differentRoutesDistance = mutableListOf<Int>()
        objectsInCommon.forEach {
            val youAmountTransfers = getOrbitChainToAny("YOU", it.name) - 1 //calculate the amount of transfers to a common crosspoint (-1 because it gives the chain not the amount of transfers)
            val santaAmountTranfers = getOrbitChainToAny("SAN", it.name) -1
            val sum = youAmountTransfers + santaAmountTranfers
            differentRoutesDistance.add(sum)
        }
        val minimumDistance = differentRoutesDistance.min()
        println("Minimum distance to santa: $minimumDistance")
    }
    println("Execution time part2: $time1")

}

class SpaceObject{
    var orbits : SpaceObject? = null
    var name = "NoName"
}

fun getOrbitChainToAny(name: String, nameTo: String): Int{
    var currentObject = spaceObjects[name]
    var iterator = 0
    if (currentObject != null) {
        while(currentObject?.name != nameTo){
            if(currentObject?.orbits != null){
                currentObject = currentObject.orbits
                iterator++
            }
        }
    }
    return iterator
}

fun getChainToCom(name: String): List<SpaceObject>{
    var currentObject = spaceObjects[name]
    if(currentObject == null) return emptyList()
    val path = mutableListOf(currentObject)
    while(currentObject?.name != "COM"){
        if(currentObject?.orbits != null){
            currentObject = currentObject.orbits
            if(currentObject == null) continue
            path.add(currentObject)
        }
    }
    return path
}

fun getOrbitChain(name: String): Int{
    var currentObject = spaceObjects[name]
    var iterator = 0
    if (currentObject != null) {
        while(currentObject?.name != "COM"){
            if(currentObject?.orbits != null){
                currentObject = currentObject.orbits
                iterator++
            }
        }
    }
    return iterator
}

fun parseInput(){
    File("C:\\Users\\Aaron\\IdeaProjects\\AdventOfCode2019-kotlin\\sources\\inputs\\Day6.txt").forEachLine { orbitInstruction.add(it.trim()) }
}

fun createSpaceObjects(){
    val COM = SpaceObject()
    COM.name = "COM"
    spaceObjects.put("COM", COM)
    orbitInstruction.forEach {
        val instruction = it.split(")")
        val spaceObjectTemp = SpaceObject()
        spaceObjectTemp.name = instruction[1]
        spaceObjects.put(instruction[1], spaceObjectTemp)
    }

    orbitInstruction.forEach {
        val instruction = it.split(")")
        val spaceObject = spaceObjects[instruction[1].toString()]
        val orbitObject = spaceObjects[instruction[0].toString()]
        if (spaceObject != null) {
            spaceObject.orbits = orbitObject
        }
    }
}