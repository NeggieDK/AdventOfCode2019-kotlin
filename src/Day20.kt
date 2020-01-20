package day20

import java.io.File
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

val unparsedInput = File("sources\\inputs\\Day20.txt").readLines()
val portalsLocation = hashMapOf<String, String>()
val points = hashMapOf<String, String>()
val portalsConn = hashMapOf<String, HashMap<String, Int>>()

fun main() {
    parseInput()
    calculateDistanceKeys()
    println("Part1: Shortest path from AA to ZZ is ${shortestPath("AA_O", "ZZ_O")}")
    println("Part2: Shortest path from AA to ZZ is ${shortestPathPart2("AA_O", "ZZ_O")}")
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
            if(currV != null && currV != "." && currP.Steps != 0)
                keysFound[currV] = currP.Steps
            // North
            if(isOpenSpace(currP.X,currP.Y - 1) && !visited(currP.X, currP.Y - 1, visited))
                objects.offer(Check(currP.X, currP.Y - 1, currP.Steps+1))
            // South
            if(isOpenSpace(currP.X,currP.Y + 1) && !visited(currP.X, currP.Y + 1, visited))
                objects.offer(Check(currP.X, currP.Y + 1, currP.Steps+1))
            // West
            if(isOpenSpace(currP.X + 1,currP.Y) && !visited(currP.X + 1, currP.Y, visited))
                objects.offer(Check(currP.X + 1, currP.Y, currP.Steps+1))
            // East
            if(isOpenSpace(currP.X - 1,currP.Y) && !visited(currP.X - 1, currP.Y, visited))
                objects.offer(Check(currP.X - 1, currP.Y, currP.Steps+1))
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
    val nodes = PriorityQueue<PCheck>(PCheckComp)
    nodes.offer(PCheck(sPoint, 0,0))
    while(nodes.isNotEmpty()){
        val currN = nodes.poll() ?: throw IllegalArgumentException()
        if(currN.Level > portalsConn.size) continue
        val currNeighbors = portalsConn[currN.Name] ?: throw IllegalArgumentException()
        for(neighbour in currNeighbors){
            if(currN.LastChecked == neighbour.key) continue // Don't look back
            if(currN.Level == 0 && neighbour.key.endsWith("_O") && neighbour.key != "ZZ_O" && neighbour.key != "AA_O") continue // At level 0 only ZZ, AA and inner portals are visible
            if(currN.Level != 0 && neighbour.key == "ZZ_O" || neighbour.key == "AA_O") continue // On levels > 0 portals ZZ and AA are not visible
            if(neighbour.key == "ZZ_O") return currN.Steps + neighbour.value

            val newName = if(neighbour.key.endsWith("_O") && !neighbour.key.startsWith("AA") && !neighbour.key.startsWith("ZZ"))
                neighbour.key.replace("_O", "_I")
            else
                neighbour.key.replace("_I", "_O")

            val diff = if(neighbour.key.endsWith("_O")) -1 else 1
            nodes.offer(PCheck(newName, currN.Steps + neighbour.value+1, currN.Level+diff, neighbour.key))
        }
    }
    return 0
}

data class Check(val X: Int = 0, val Y: Int = 0, val Steps: Int = 0)
data class PCheck(val Name: String = "", val Steps: Int = 0, val Level: Int = 0, val LastChecked: String = "")
var PCheckComp = Comparator<PCheck> { s1, s2 -> (s1.Steps+s1.Level) - (s2.Steps+s1.Level) }