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
    println("Shortest path from AA to ZZ is ${shortestPath("AA_O", "ZZ_O")}")
    shortestPathPart2("AA_O", "ZZ_O")
}

fun shortestPath(sPoint: String, ePoint: String): Int?{
    val nodes = hashMapOf<String, Int>()
    portalsConn.forEach {
        if(it.key == sPoint) nodes[it.key] = 0
        else nodes[it.key] = Int.MAX_VALUE }
    val visited = hashSetOf<String>()
    while(visited.size < nodes.size){
        val currN = getPoint(nodes, visited)
        val currNeighbors = portalsConn[currN.key] ?: throw IllegalArgumentException()
        for(neighbour in currNeighbors){
            if(visited.contains(neighbour.key)) continue
            val currV = nodes[neighbour.key] ?: throw IllegalArgumentException()
            if(currV > currN.value + neighbour.value)
                nodes[neighbour.key] = currN.value + neighbour.value
        }
        visited.add(currN.key)
    }
    return nodes[ePoint]
    // Starting point = AA
    // Gets the neighbours from AA out portalsConn -> for every neighbour calculate the cost (in step 1 this is just the distance)
    // Put this cost in a new hashmap that consists of key = portal and value = current steps
    // Get the value with the lowest currentsteps and not visited and calulcate the neighbour distance again and so forth until every node has been visited!
}

fun getPoint(nodes: HashMap<String, Int>, visited: HashSet<String>): Map.Entry<String, Int> {
    return nodes.filter{!visited.contains(it.key) }.minBy { it.value} ?: throw IllegalArgumentException()
}


fun calculateDistanceKeys(){
    for(pLoc in portalsLocation){
        val coord = pLoc.value.split(",").map { it.toInt() }
        val keysFound = hashMapOf<String, Int>()
        if(pLoc.key.contains("_I") && pLoc.key != "AA_I" && pLoc.key != "ZZ_I")
            keysFound[pLoc.key.split("_")[0] + "_O"] = 1
        else if(pLoc.key != "AA_O" && pLoc.key != "ZZ_O")
            keysFound[pLoc.key.split("_")[0] + "_I"] = 1
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

fun shortestPathPart2(sPoint: String, ePoint: String): Int?{
    // BFS slow + level + manhattan?? == A*
    // visited is too restrictive at the moment, should try adding lastVisited in PCHeck to make sure we aren't going back
    val nodes = PriorityQueue<PCheck>(PCheckComp)
    nodes.offer(PCheck(sPoint, 0,0))
    while(nodes.isNotEmpty()){
        val currN = nodes.poll() ?: throw IllegalArgumentException()
        if(currN.Level > portalsConn.size) continue
        val currNeighbors = portalsConn[currN.Name] ?: throw IllegalArgumentException()
        for(neighbour in currNeighbors){
            val diff = if(neighbour.key.endsWith("_O")) -1 else 1
            if(currN.LastChecked == neighbour.key) continue
            if(currN.Level == 0){
                if(neighbour.key.endsWith("_O") && (neighbour.key != "ZZ_O" || neighbour.key != "AA_O")) continue
            }
            else if(currN.Level != 0 && neighbour.key == "ZZ_O" || neighbour.key == "AA_O") continue

            if(neighbour.key == "ZZ_O")
                println()

            var newName = ""
            if(neighbour.key.endsWith("_O"))
                newName = neighbour.key.replace("_O", "_I")
            else
                newName = neighbour.key.replace("_I", "_O")

            nodes.offer(PCheck(newName, currN.Steps + neighbour.value+1, currN.Level+diff, neighbour.key))
        }
    }
    return 0
}

data class Check(val X: Int = 0, val Y: Int = 0, val Steps: Int = 0)
data class PCheck(val Name: String = "", val Steps: Int = 0, val Level: Int = 0, val LastChecked: String = "")
var PCheckComp = Comparator<PCheck> { s1, s2 -> s1.Steps - s2.Steps }