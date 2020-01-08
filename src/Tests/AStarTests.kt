package Tests.AStarTests

import java.lang.StrictMath.abs
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.system.measureTimeMillis


val unparsedInput =
    """
#####################
#...................#
#..............b....#
#..########.######..#
###..............#..#
#................#..#
#................#..#
#................#..#
#................#..#
#................#..#
#................#..#
#................#..#
#.#..............#..#
#a.###############..#
#...................#
#...................#
#####################
    """.trimMargin()
        .trimIndent()

/*val unparsedInput =
    """
#####################
#..........##..b....#
#.########.....######
#.###################
#...................#
###################.#
#.a.................#
#####################
    """.trimMargin()
        .trimIndent()*/

val input = HashMap<Pair<Int, Int>, String>()
fun main(){
    val inputList = unparsedInput.split("\n").map { it.trim() }
    for((y, row) in inputList.withIndex()){
        for((x, element) in row.withIndex()){
            input[Pair(x,y)] = element.toString()
        }
    }
    val bfsTime = measureTimeMillis {
        shortestDistanceBFS("a", "b")
    }
    println("BFS took $bfsTime miliseconds to execute")
    val AStarTime = measureTimeMillis {
        shortestDistanceAStar("a", "b")
    }
    println("A* took $AStarTime miliseconds to execute")

}

fun shortestDistanceBFS(fromKey: String, toKey: String){
    val closeList = HashSet<Pair<Int, Int>>()
    val rootElement = input.filter { it.value==fromKey }.toList()
    val root = PointOfInterestBFS().apply {
        x = rootElement[0].first.first
        y = rootElement[0].first.second }
    val pointsQueue = ArrayDeque<PointOfInterestBFS>()
    pointsQueue.offer(root)
    val pathsToKey = mutableListOf<PointOfInterestBFS>()
    while(pointsQueue.isNotEmpty()){
        val currentElement = pointsQueue.poll() ?: throw IllegalArgumentException()
        val currentElementValue = input[Pair(currentElement.x, currentElement.y)] ?: throw IllegalArgumentException()
        closeList.add(Pair(currentElement.x, currentElement.y))
        if(currentElementValue == toKey) {
            pathsToKey.add(currentElement)
            break
        }
        if(input[Pair(currentElement.x, currentElement.y-1)] != "#" && !closeList.contains(Pair(currentElement.x, currentElement.y-1))){ //North
            val newPoint = PointOfInterestBFS().apply {
                x = currentElement.x
                y = currentElement.y - 1
                directions = currentElement.directions.toMutableList()
            }
            newPoint.directions.add("N")
            pointsQueue.offer(newPoint)
        }
        if(input[Pair(currentElement.x, currentElement.y+1)] != "#" && !closeList.contains(Pair(currentElement.x, currentElement.y+1))){ //North
            val newPoint = PointOfInterestBFS().apply {
                x = currentElement.x
                y = currentElement.y + 1
                directions = currentElement.directions.toMutableList()
            }
            newPoint.directions.add("S")
            pointsQueue.offer(newPoint)
        }
        if(input[Pair(currentElement.x-1, currentElement.y)] != "#" && !closeList.contains(Pair(currentElement.x-1, currentElement.y))){ //North
            val newPoint = PointOfInterestBFS().apply {
                x = currentElement.x-1
                y = currentElement.y
                directions = currentElement.directions.toMutableList()
            }
            newPoint.directions.add("W")
            pointsQueue.offer(newPoint)
        }
        if(input[Pair(currentElement.x+1, currentElement.y)] != "#"  && !closeList.contains(Pair(currentElement.x+1, currentElement.y))){ //North
            val newPoint = PointOfInterestBFS().apply {
                x = currentElement.x+1
                y = currentElement.y
                directions = currentElement.directions.toMutableList()
            }
            newPoint.directions.add("E")
            pointsQueue.offer(newPoint)
        }
    }
    println("Shortest path takes ${pathsToKey.minBy { it.directions.size }?.directions?.size}")
}

fun shortestDistanceAStar(fromKey: String, toKey: String){
    val openlist = PriorityQueue<PointOfInterestAStar>(pointOfInterestAStarComparator)
    val closeList = HashSet<Pair<Int, Int>>()
    val rootElement = input.filter { it.value==fromKey }.toList().first()
    val destElement = input.filter { it.value==toKey }.toList().first()
    val root = PointOfInterestAStar(rootElement.first.first,rootElement.first.second)
    openlist.offer(root)
    val pathsToKey = mutableListOf<PointOfInterestAStar>()
    while(openlist.isNotEmpty()){
        val currentElement = openlist.poll() ?: throw IllegalArgumentException()
        val currentElementValue = input[Pair(currentElement.x, currentElement.y)] ?: throw IllegalArgumentException()
        closeList.add(Pair(currentElement.x, currentElement.y))
        if(currentElementValue == toKey) {
            pathsToKey.add(currentElement)
            break
        }
        if(input[Pair(currentElement.x, currentElement.y-1)] != "#" && !closeList.contains(Pair(currentElement.x, currentElement.y-1))){ //North
            val newPoint = PointOfInterestAStar(currentElement.x, currentElement.y-1, currentElement.stepsAwayFromOrigin + 1).apply {
                directions = currentElement.directions.toMutableList()
            }
            newPoint.calculatePriority(destElement.first.first, destElement.first.second)
            newPoint.directions.add("N")
            openlist.offer(newPoint)
        }
        if(input[Pair(currentElement.x, currentElement.y+1)] != "#" && !closeList.contains(Pair(currentElement.x, currentElement.y+1))){ //North
            val newPoint = PointOfInterestAStar(currentElement.x, currentElement.y+1, currentElement.stepsAwayFromOrigin + 1).apply {
                directions = currentElement.directions.toMutableList()
            }
            newPoint.calculatePriority(destElement.first.first, destElement.first.second)
            newPoint.directions.add("S")
            openlist.offer(newPoint)
        }
        if(input[Pair(currentElement.x-1, currentElement.y)] != "#" && !closeList.contains(Pair(currentElement.x-1, currentElement.y))){ //North
            val newPoint = PointOfInterestAStar(currentElement.x-1, currentElement.y, currentElement.stepsAwayFromOrigin + 1).apply {
                directions = currentElement.directions.toMutableList()
            }
            newPoint.calculatePriority(destElement.first.first, destElement.first.second)
            newPoint.directions.add("W")
            openlist.offer(newPoint)
        }
        if(input[Pair(currentElement.x+1, currentElement.y)] != "#"  && !closeList.contains(Pair(currentElement.x+1, currentElement.y))){ //North
            val newPoint = PointOfInterestAStar(currentElement.x+1, currentElement.y, currentElement.stepsAwayFromOrigin + 1).apply {
                directions = currentElement.directions.toMutableList()
            }
            newPoint.calculatePriority(destElement.first.first, destElement.first.second)
            newPoint.directions.add("E")
            openlist.offer(newPoint)
        }
    }
    println("Shortest path takes ${pathsToKey.minBy { it.directions.size }?.directions?.size}")
}

class PointOfInterestBFS{
    var x = 0
    var y = 0
    var directions = mutableListOf<String>()
}

class PointOfInterestAStar{
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
    var stepsAwayFromOrigin = 0
    var directions = mutableListOf<String>()
    var priority = 0

    fun calculatePriority(destX : Int, destY: Int){
        priority = stepsAwayFromOrigin + abs(x-destX) + abs(y-destY)
    }
}

var pointOfInterestAStarComparator = Comparator<PointOfInterestAStar> { s1, s2 -> s1.priority - s2.priority }