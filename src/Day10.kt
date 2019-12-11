package day10

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

    var asteroids = parsedInput.map {it.filter { it2 -> it2.isAsteroid } }.flatten()
    var max = 0
    var maxPoint = Point()
    asteroids.forEach {
        val angleList = mutableListOf<Double>()
        asteroids.forEach { it2 ->
            if(it != it2)
                angleList.add(it.angleTo(it2))
        }
        var amount = angleList.distinct().size
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
    asteroids = parsedInput.map {it.filter { it2 -> it2.isAsteroid } }.flatten()

    val asteroidsWithAngles = getAnglesFromCenter(center, asteroids)
    asteroidsWithAngles.forEach {
        if (it.first != center && it.first.isAsteroid) {
            var closestPoint = Point()
            var closestDistance = Int.MAX_VALUE
            val pointsWithSameAngle = asteroidsWithAngles
                .filter { it2 -> it.second == it2.second }
                .forEach { it3 ->
                    val distance = center.manhattanDistanceTo(it3.first)
                    if(distance < closestDistance){
                        closestPoint = it3.first
                        closestDistance = distance
                    }
            }
        }
    }

    println(listOf(1,2,2,3,4,5).distinct())

}

fun parseInput(){
    var yIndex = 0
    input.forEach {
        var xIndex = 0
        parsedInput.add(mutableListOf())
        it.forEach {
            var point = Point()
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

fun getAnglesFromCenter(center: Point, asteroids: List<Point>): MutableList<Pair<Point, Double>> {
    val list = mutableListOf<Pair<Point, Double>>()
    asteroids.forEach {
        if(center != it)
            list.add(Pair(it, center.angleTo(it)))
    }
    return list
}

class Point{
    var isAsteroid = false
    var x = 0
    var y = 0

    fun angleTo(otherPoint: Point): Double{
        val deltaX = otherPoint.x - x
        val deltaY = y - otherPoint.y
        return atan2(deltaY.toDouble(), deltaX.toDouble())
    }

    fun manhattanDistanceTo(otherPoint: Point): Int{
        return abs(x - otherPoint.x) + abs(y - otherPoint.y)
    }
}