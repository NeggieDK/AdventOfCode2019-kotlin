package day20

import java.io.File

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
val portals = hashMapOf<String, String>()
val points = hashMapOf<String, String>()

fun main() {
    parseInput()
}

fun parseInput() {
    val maxX = unparsedInput[2].count { it.toString() == "#" || it.toString() == "." } - 1
    val maxY = unparsedInput.size - 5
    val innerWest = unparsedInput[maxX/2].count { it.toString() == "#" || it.toString() == "." }/2 -1
    val innerEast = maxX - innerWest
    val innerNorth = innerWest // Assumptions be careful!
    val innerSouth = maxY - innerNorth // Square so that means that these values should be the same
    for((y, row) in unparsedInput.withIndex()){
        for((x, element) in row.map { it.toString() }.withIndex()){
            if(element == "#" || element == " ") continue
            points["${x-2},${y-2}"] = element
        }
    }
    // Scan vertical lines
    for (x in 0..maxX){
        if(points["$x,0"] == "."){
            val portalName = points["$x,-2"].toString() + points["$x,-1"].toString()
            portals[portalName + "_O"] = "$x,0"
            points["$x,0"] = portalName + "_O"
        }
        if(points["$x,$innerNorth"]  == "." && x > innerWest && x < innerEast){
            val portalName = points["$x,${innerNorth+1}"].toString() + points["$x,${innerNorth+2}"].toString()
            portals[portalName + "_I"] = "$x,$innerNorth"
            points["$x,$innerNorth"] = portalName + "_I"
        }
        if(points["$x,$innerSouth"] == "." && x > innerWest && x < innerEast){
            val portalName = points["$x,${innerSouth-2}"].toString() + points["$x,${innerSouth-1}"].toString()
            portals[portalName + "_I"] = "$x,$innerSouth"
            points["$x,$innerSouth"] = portalName + "_I"
        }
        if(points["$x,$maxY"] == "."){
            val portalName = points["$x,${maxY+1}"].toString() + points["$x,${maxY+2}"].toString()
            portals[portalName + "_O"] = "$x,$maxY"
            points["$x,$maxY"] = portalName + "_O"
        }
    }
    // Scan horizontal lines
    for (y in 0..maxY){
        if(points["0,$y"] == "."){
            val portalName = points["-2,$y"].toString() + points["-1,$y"].toString()
            portals[portalName + "_O"] = "0,$y"
            points["0,$y"] = portalName + "_O"
        }
        if(points["$innerWest,$y"]  == "." && y > innerNorth && y < innerSouth){
            val portalName = points["${innerWest+1},$y"].toString() + points["${innerWest+2},$y"].toString()
            portals[portalName + "_I"] = "$innerWest,$y"
            points["$innerWest,$y"] = portalName + "_I"
        }
        if(points["$innerEast,$y"]  == "." && y > innerNorth && y < innerSouth){
            val portalName = points["${innerEast-2},$y"].toString() + points["${innerEast-1},$y"].toString()
            portals[portalName + "_I"] = "$innerEast,$y"
            points["$innerEast,$y"] = portalName + "_I"
        }
        if(points["$maxX,$y"] == "."){
            val portalName = points["${maxX+1},$y"].toString() + points["${maxX+2},$y"].toString()
            portals[portalName + "_O"] = "$maxX,$y"
            points["$maxX,$y"] = portalName + "_O"
        }
    }
    println()
}

