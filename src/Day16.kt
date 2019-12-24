package day16

import java.util.*
import kotlin.math.abs

var inputSignal = "59773775883217736423759431065821647306353420453506194937477909478357279250527959717515453593953697526882172199401149893789300695782513381578519607082690498042922082853622468730359031443128253024761190886248093463388723595869794965934425375464188783095560858698959059899665146868388800302242666479679987279787144346712262803568907779621609528347260035619258134850854741360515089631116920328622677237196915348865412336221562817250057035898092525020837239100456855355496177944747496249192354750965666437121797601987523473707071258599440525572142300549600825381432815592726865051526418740875442413571535945830954724825314675166862626566783107780527347044".toList().map { it.toString().toInt() }.toMutableList()
var phases = 100

var output = mutableListOf<Int>()
fun main() {
    //Part 1
    println(getOutput(100, inputSignal).subList(0,7))

    //Part 2
    val startingSize = inputSignal.size
    for (l in 0..9998) {
       inputSignal.addAll(inputSignal.subList(0, startingSize))
   }
    val offset = parseOffset()
    if(offset < inputSignal.size/2) throw IllegalArgumentException()
    //second half
    var inputSignalSublist = inputSignal.subList(offset, inputSignal.size)
    for (k in 0 until phases) {
        val output = mutableListOf<Int>()
        for (j in inputSignalSublist.size-1 downTo 0) {
            val add = if(!output.any())  0 else output.last()
            val result = ((inputSignalSublist[j]%10) + add)%10
            output.add(result)
        }
        inputSignalSublist = output.toMutableList()
        inputSignalSublist.reverse()
        output.clear()
    }
    println(inputSignalSublist.subList(0,8))
}

fun parseOffset(): Int{
    var offsetList = inputSignal.subList(0, 7)
    var tempString = ""
    offsetList.forEach {
        tempString += it.toString()
    }
    return tempString.toInt()
}

fun getOutput(phases: Int, input: MutableList<Int>): List<Int>{
    var inputSignal = input
    for (k in 0 until phases) {
        val output = mutableListOf<Int>()
        for(i in 0 until inputSignal.size) {
            var result = 0
            for (j in 0 until inputSignal.size) {
                result += ((inputSignal[j] * getRepeatValue(j, i+1)) % 10)
            }
            output.add(abs(result%10))
        }
        inputSignal = output.map { it }.toMutableList()
    }
    return inputSignal
}

fun getSecondHalf(){

}

fun getRepeatValue(index: Int, repeat: Int): Int {
    val moduloValue = repeat * 4 //4 because the repeating array has 4 elements
    val index = index + 1
    val baseIndex = if (index < moduloValue) {
        index / repeat
    } else {
        (index % moduloValue) / repeat
    }

    return when (baseIndex) {
        0 -> 0
        1 -> 1
        2 -> 0
        3 -> -1
        else -> throw IllegalArgumentException()
    }
}

fun getRepeatingPattern(repeatingPattern: List<Int>, repeat: Int, size: Int): MutableList<Int> {
    val repeatingPatternList = mutableListOf<Int>()
    var amountRepeated = 0
    var iterator = 0
    while (repeatingPatternList.size != size + 1) { //size + 1 because we have to remove the first element of the list at the end
        repeatingPatternList.add(repeatingPattern[iterator])
        amountRepeated++
        if (amountRepeated >= repeat) {
            iterator++
            amountRepeated = 0
            if (iterator == repeatingPattern.size) {
                iterator = 0
            }
        }
    }
    repeatingPatternList.removeAt(0)
    return repeatingPatternList
}