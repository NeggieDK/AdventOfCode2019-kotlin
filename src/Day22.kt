import java.io.File
import kotlin.math.abs


val commands = File("sources\\inputs\\Day22.txt").readLines()

fun main(){
    var currentDeck = hashMapOf<Long, Long>()
    val cardsAmount = 10_007L
    //Initiates the cards
    for(i in 0 until cardsAmount){
        currentDeck[i] = i
    }
    for(command in commands){
        if(command.trim() == "deal into new stack"){
            currentDeck = createNewStack(currentDeck)
        }
        else if(command.trim().startsWith("cut")){
            currentDeck = cutCards(currentDeck, getNumber(command))
        }
        else{
            currentDeck = incrementStack(currentDeck, getNumber(command))
        }
    }
    println(currentDeck.filter { it.value == 2019L }.toList())
}

fun getNumber(input: String) : Long{
    var result = ""
    for(el in input.reversed()){
        if(el.isDigit() || el.toString() == "-") result = el.toString() + result
        else break
    }
    val newInput = result.trim().toLong()
    return newInput
}

fun createNewStack(oldDeck: HashMap<Long, Long> ) : HashMap<Long, Long>{
    var max = (oldDeck.size-1).toLong()
    val newDeck = hashMapOf<Long, Long>()
    for(card in oldDeck){
        newDeck[max] = card.value
        max--
    }
    return newDeck
}

fun incrementStack(oldDeck: HashMap<Long, Long>, amount: Long ) : HashMap<Long, Long> {
    val newDeck = hashMapOf<Long, Long>()
    newDeck[0] = oldDeck[0] ?: throw IllegalArgumentException()
    var iterator = 1L
    var index = amount
    for(i in 1 until oldDeck.size){
        newDeck[index] = oldDeck[iterator] ?: throw IllegalArgumentException()
        index += amount
        if(index > oldDeck.size-1) index -= oldDeck.size
        iterator++
    }
    return  newDeck
}

fun cutCards(oldDeck: HashMap<Long, Long>, amount: Long ) : HashMap<Long, Long>{
    val newDeck = hashMapOf<Long, Long>()
    if(amount > 0){
        var iterator = 0L
        for(i in amount until oldDeck.size){
            newDeck[iterator] = oldDeck[i] ?: throw IllegalArgumentException()
            iterator++
        }
        for(i in 0 until amount){
            newDeck[iterator] = oldDeck[i] ?: throw IllegalArgumentException()
            iterator++
        }
    }
    else if(amount < 0){
        val newAmount = abs(amount)
        var iterator = 0L
        for(i in oldDeck.size-newAmount until oldDeck.size){
            newDeck[iterator] = oldDeck[i] ?: throw IllegalArgumentException()
            iterator++
        }
        for(i in 0 until oldDeck.size-newAmount){
            newDeck[iterator] = oldDeck[i] ?: throw IllegalArgumentException()
            iterator++
        }
    }
    return newDeck
}