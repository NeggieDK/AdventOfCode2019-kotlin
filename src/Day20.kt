package day20

import java.io.File
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

// Need to build a graph of all the portals, do BFS on each key to see which keys are directly in range from each other
// The portals could possible look something like this: ABInner -> ABOuter == 1 step taken (same for reverse), and without teleportation it could look like AB -> CD == 5 steps
// Basically nothing stops us from making a graph and using SPF
// Example: AA (starting point) has portals in range: AB(2), BC(3), DE(4)

// TODO: 1) ParseInput 2) Create Graph 3) Use SPF on Graph

//Parsing input
// List of portals and their coordinates
// Input is a donut, and portals only occur inside or outside the donut, so we can just scan the edges
// List of coordinates that are open space or a portal
// Traverse input
val unparsedInput = File("sources\\inputs\\Day20.txt")
    .readLines()
val portalsLocation = hashMapOf<String, String>()
val points = hashMapOf<String, String>()
val portalsConn = hashMapOf<String, HashMap<String, Int>>()

fun main() {
    parseInput()
    calculateDistanceKeys()
    println()
}

fun calculateDistanceKeys(){
    for(pLoc in portalsLocation){
        val coord = pLoc.value.split(",").map { it.toInt() }
        val keysFound = hashMapOf<String, Int>()
        val visited = hashSetOf<String>()
        val objects = ArrayDeque<Check>()
        objects.offer(Check(coord[0], coord[1]))
        while(objects.isNotEmpty()){
            val currP = objects.poll() ?: throw IllegalArgumentException()
            val currV = points["${currP.X},${currP.Y}"]
            visited.add("${currP.X},${currP.Y}")
            if(currV != null && currV != "." && currP.Steps != 0){
                keysFound[currV] = currP.Steps
            }
            // North
            if(isOpenSpace(currP.X,currP.Y - 1) && !visited(currP.X, currP.Y - 1, visited)){
                objects.offer(Check(currP.X, currP.Y - 1, currP.Steps+1))
            }
            // South
            if(isOpenSpace(currP.X,currP.Y + 1) && !visited(currP.X, currP.Y + 1, visited)){
                objects.offer(Check(currP.X, currP.Y + 1, currP.Steps+1))
            }
            // West
            if(isOpenSpace(currP.X + 1,currP.Y) && !visited(currP.X + 1, currP.Y, visited)){
                objects.offer(Check(currP.X + 1, currP.Y, currP.Steps+1))
            }
            // East
            if(isOpenSpace(currP.X - 1,currP.Y) && !visited(currP.X - 1, currP.Y, visited)){
                objects.offer(Check(currP.X - 1, currP.Y, currP.Steps+1))
            }
        }
        portalsConn[pLoc.key] = keysFound
    }
}

fun isOpenSpace(x: Int, y: Int): Boolean{
 return points["$x,$y"] != null && points["$x,$y"] != " "
}

fun visited(x: Int, y: Int, visited: HashSet<String>): Boolean{
    return visited.contains("$x,$y")
}

fun parseInput() {
    val maxX = unparsedInput[2].count { it.toString() == "#" || it.toString() == "." } - 1
    val maxY = unparsedInput.size - 5
    val innerWest = unparsedInput[maxX/2].count { it.toString() == "#" || it.toString() == "." }/2 -1
    val innerEast = maxX - innerWest
    val innerNorth = innerWest // Assumptions be careful!
    val innerSouth = maxY - innerNorth
    for((y, row) in unparsedInput.withIndex()){
        for((x, element) in row.map { it.toString() }.withIndex()){
            if(element == "#" || element == " ") continue
            points["${x-2},${y-2}"] = element
        }
    }
    // Scan vertical lines
    for (x in 0..maxX){
        if(points["$x,0"] == "."){
            val portalName = getAndDeletePortalName(x,-2) + getAndDeletePortalName(x,-1)
            portalsLocation[portalName + "_O"] = "$x,0"
            points["$x,0"] = portalName + "_O"
        }
        if(points["$x,$innerNorth"]  == "." && x > innerWest && x < innerEast){
            val portalName = getAndDeletePortalName(x, innerNorth+1) + getAndDeletePortalName(x,innerNorth+2)
            portalsLocation[portalName + "_I"] = "$x,$innerNorth"
            points["$x,$innerNorth"] = portalName + "_I"
        }
        if(points["$x,$innerSouth"] == "." && x > innerWest && x < innerEast){
            val portalName = getAndDeletePortalName(x,innerSouth-2) + getAndDeletePortalName(x,innerSouth-1)
            portalsLocation[portalName + "_I"] = "$x,$innerSouth"
            points["$x,$innerSouth"] = portalName + "_I"
        }
        if(points["$x,$maxY"] == "."){
            val portalName = getAndDeletePortalName(x, maxY+1) + getAndDeletePortalName(x, maxY+2)
            portalsLocation[portalName + "_O"] = "$x,$maxY"
            points["$x,$maxY"] = portalName + "_O"
        }
    }
    // Scan horizontal lines
    for (y in 0..maxY){
        if(points["0,$y"] == "."){
            val portalName = getAndDeletePortalName(-2, y) + getAndDeletePortalName(-1, y)
            portalsLocation[portalName + "_O"] = "0,$y"
            points["0,$y"] = portalName + "_O"
        }
        if(points["$innerWest,$y"]  == "." && y > innerNorth && y < innerSouth){
            val portalName = getAndDeletePortalName(innerWest+1 ,y) + getAndDeletePortalName(innerWest+2, y)
            portalsLocation[portalName + "_I"] = "$innerWest,$y"
            points["$innerWest,$y"] = portalName + "_I"
        }
        if(points["$innerEast,$y"]  == "." && y > innerNorth && y < innerSouth){
            val portalName = getAndDeletePortalName(innerEast-2, y) + getAndDeletePortalName(innerEast-1, y)
            portalsLocation[portalName + "_I"] = "$innerEast,$y"
            points["$innerEast,$y"] = portalName + "_I"
        }
        if(points["$maxX,$y"] == "."){
            val portalName = getAndDeletePortalName(maxX+1 ,y) + getAndDeletePortalName(maxX+2 ,y)
            portalsLocation[portalName + "_O"] = "$maxX,$y"
            points["$maxX,$y"] = portalName + "_O"
        }
    }
}

fun getAndDeletePortalName(x: Int, y: Int): String{
    val value = points["$x,$y"].toString()
    points.remove("$x,$y")
    return value
}
data class Check(val X: Int = 0, val Y: Int = 0, val Steps: Int = 0)


