import java.io.File

val input = File("C:\\Users\\aarondk\\Kotlin Projects\\Test\\sources\\inputs\\Day8.txt").readText().trim().toList().map { it.toString().trim().toInt() }
val parsedInput = mutableListOf<MutableList<Int>>()
var buildImage = mutableListOf<Int>()
const val imageWidth = 25
const val imageHeight = 6

fun main(){
    val layerLength = imageHeight*imageWidth
    var tempList = mutableListOf<Int>()
    for(element in input){
        tempList.add(element)
        if(tempList.size == layerLength){
            parsedInput.add(tempList)
            tempList = mutableListOf()
        }
    }
    val leastAmountOfZeroes = parsedInput.map { it.filter { it2 -> it2 == 0 }.size }.min()
    val correctList = parsedInput.filter { it.filter { it2 -> it2 == 0 }.size == leastAmountOfZeroes}.toList()[0]
    val amountOne = correctList.filter { it == 1 }.size
    val amountTwo = correctList.filter { it == 2 }.size
    val result = amountOne*amountTwo
    println("Part 1: $result")

    parsedInput.forEach {
        if(buildImage.isEmpty()){
            buildImage = it
        }
        else{
            val temp = mutableListOf<Int>()
            it.zip(buildImage).forEach {
                if(it.second == 2)
                    temp.add(it.first)
                else
                    temp.add(it.second)
            }
            buildImage = temp
        }
    }
    for(i in 0 until buildImage.size) {
        if(i%imageWidth==0) println("")

        if(buildImage[i] == 1)
            print("*")
        else
            print(" ")
    }
}