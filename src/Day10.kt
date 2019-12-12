package day10

import java.lang.StrictMath.toDegrees
import kotlin.math.abs
import kotlin.math.atan2

val input =
    """
#..#.#.#.######..#.#...##
##.#..#.#..##.#..######.#
.#.##.#..##..#.#.####.#..
.#..##.#.#..#.#...#...#.#
#...###.##.##..##...#..#.
##..#.#.#.###...#.##..#.#
###.###.#.##.##....#####.
.#####.#.#...#..#####..#.
.#.##...#.#...#####.##...
######.#..##.#..#.#.#....
###.##.#######....##.#..#
.####.##..#.##.#.#.##...#
##...##.######..##..#.###
...###...#..#...#.###..#.
.#####...##..#..#####.###
.#####..#.#######.###.##.
#...###.####.##.##.#.##.#
.#.#.#.#.#.##.#..#.#..###
##.#.####.###....###..##.
#..##.#....#..#..#.#..#.#
##..#..#...#..##..####..#
....#.....##..#.##.#...##
.##..#.#..##..##.#..##..#
.##..#####....#####.#.#.#
#..#..#..##...#..#.#.#.##
    """.trimMargin()
        .trimIndent()
        .split("\n").map { it.trim().toList() }

var parsedInput = mutableListOf<MutableList<Point>>()
var parsedInputPart2 = mutableListOf<MutableList<Point>>()
fun main() {
    parseInput()

    var asteroids = parsedInput.map {it.filter { it2 -> it2.isAsteroid } }.flatten().toMutableList()
    var max = 0
    var maxPoint = Point()
    asteroids.forEach {
        val angleList = mutableListOf<Double>()
        asteroids.forEach { it2 ->
            if(it != it2)
                angleList.add(it.angleTo(it2))
        }
        val amount = angleList.distinct().size
        if(amount > max){
            max = amount
            maxPoint = it
        }
    }
    val maxX = maxPoint.x
    val maxY = maxPoint.y
    println("Part 1, asteroid with most asteroids in vision: $maxX, $maxY with $max asteroids")


    val center = parsedInput[maxY][maxX]
    center.isAsteroid = false
    center.isCenter = true
    asteroids = parsedInput.map {it.filter { it2 -> it2.isAsteroid } }.flatten().toMutableList()
    val angleUp = 90
    var amountLasered = 0
    while(amountLasered < 200) {
        asteroids = parsedInput.map {it.filter { it2 -> it2.isAsteroid } }.flatten().toMutableList()
        val asteroidsWithAngles = getAnglesFromCenter(center, asteroids)
        val asteroidsWithAnglesKeySorted = mutableListOf<Double>()
        asteroidsWithAnglesKeySorted.addAll(asteroidsWithAngles.keys.sorted().filter { it <= angleUp && it >= 0}.sortedDescending())
        asteroidsWithAnglesKeySorted.addAll(asteroidsWithAngles.keys.sorted().filter { it <360 && it > angleUp }.sortedDescending())
        asteroidsWithAnglesKeySorted.forEach {
            var minDistance = Int.MAX_VALUE
            var closestPoint = Point()
            asteroidsWithAngles[it]?.forEach { it2 ->
                if(it2.isAsteroid){
                    val distance = center.manhattanDistanceTo(it2)
                    if (distance < minDistance) {
                        minDistance = distance
                        closestPoint = it2
                    }
                }
            }
            asteroids.remove(closestPoint)

            amountLasered++
            if(amountLasered == 200){
                val x = closestPoint.x
                val y = closestPoint.y
                val result = x*100+y
                println("Part 2: 200th asteroid to be lasered is $x, $y, result is $result")
            }
        }
    }
}

fun parseInput(){
    var yIndex = 0
    input.forEach {
        var xIndex = 0
        parsedInput.add(mutableListOf())
        it.forEach {
            val point = Point()
            if(it.toString() == "#"){
                point.isAsteroid = true
            }
            point.x = xIndex
            point.y = yIndex
            parsedInput[yIndex].add(point)
            xIndex++
        }
        yIndex++
    }
    println(parsedInput)
}

fun getAnglesFromCenter(center: Point, asteroids: List<Point>): HashMap<Double, MutableList<Point>> {
    val angleHashMap = hashMapOf<Double, MutableList<Point>>()
    asteroids.forEach {
        if(center != it){
            if(angleHashMap.containsKey(center.angleTo(it))){
                angleHashMap[center.angleTo(it)]?.add(it)
            }
            else{
                val newList = mutableListOf(it)
                angleHashMap.put(center.angleTo(it), newList)
            }
        }
    }
    return angleHashMap
}

class Point{
    var isAsteroid = false
    var isCenter = false
    var x = 0
    var y = 0

    fun angleTo(otherPoint: Point): Double{
        val deltaX = otherPoint.x - x
        val deltaY = y - otherPoint.y
        var angleInDegrees = toDegrees(atan2(deltaY.toDouble(), deltaX.toDouble()))
        if(angleInDegrees < 0) angleInDegrees += 360
        return angleInDegrees
    }

    fun manhattanDistanceTo(otherPoint: Point): Int{
        return abs(x - otherPoint.x) + abs(y - otherPoint.y)
    }
}