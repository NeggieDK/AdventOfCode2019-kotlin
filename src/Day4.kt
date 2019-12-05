import kotlin.math.abs
import kotlin.math.log10

val list = (178416..676461).toList()
val validList = mutableListOf<Int>()
val validListPart2 = mutableListOf<Int>()
fun main(){
    list.forEach {
        if(it.isValid()){
            validList.add(it)
        }
        if(it.isValidPart2()){
            validListPart2.add(it)
        }
    }
    println("Amount of codes for part1: ${validList.size}")
    println("Amount of codes for part2: ${validListPart2.size}")
}

fun Int.isValid() : Boolean{
    if(this.amountOfDigits() == 6 && this.atleastTwoSameAdjacent() && this.nextNumberAlwaysIncrease()){
        return true
    }
    return false
}
fun Int.isValidPart2() : Boolean{
    if(this.amountOfDigits() == 6 && this.hasExactTwoSameAdjacent() && this.nextNumberAlwaysIncrease()){
        return true
    }
    return false
}


fun Int.amountOfDigits() = when(this) {
        0 -> 1
        else -> log10(abs(toDouble())).toInt() + 1
}

fun Int.atleastTwoSameAdjacent() : Boolean {
    val intList = this.toList()
    var previous = Int.MIN_VALUE;
    intList.forEach {
        if(it == previous){
            return true
        }
        previous = it
    }
    return false
}

fun Int.hasExactTwoSameAdjacent() : Boolean {
    val intList = this.toList()
    var previous = Int.MIN_VALUE
    var previousPrevious = Int.MIN_VALUE
    for(i in intList.indices){
        val current = intList[i]
        val next = if(i+1 < intList.size){
            intList[i+1]
        } else{
            Int.MIN_VALUE
        }

        if(current == previous && current != previousPrevious && next != current){
            return true
        }
        previousPrevious = previous
        previous = current
    }
    return false
}

fun Int.nextNumberAlwaysIncrease() : Boolean {
    val intList = this.toList()
    var previous = Int.MIN_VALUE;
    intList.forEach {
        if(previous > it){
            return false
        }
        previous = it
    }
    return true
}

fun Int.toList() = this.toString().toList().map { it.toString().toInt() }.toList()