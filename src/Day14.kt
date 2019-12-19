package day14

import java.io.File
import kotlin.math.ceil

var reactions = HashMap<String, Reaction>()

var conversionReactions = mutableListOf<Element>()
var overSupplyElements = HashMap<String, Int>()
fun main() {
    val input = File("C:\\Users\\aarondk\\Kotlin Projects\\Test\\sources\\inputs\\Day14.txt")
        .readLines()
        .map {it.trim().split("=>")}
    parseInput(input)
    conversionReactions.add(Element("FUEL", 1))
    while(conversionReactions.any { !it.IsRootElement }){
        val newConversionReactionsList = mutableListOf<Element>()
        for (it in conversionReactions) {
            if(it.IsRootElement){
                newConversionReactionsList.add(it)
                continue
            }
            val reactionFromElement = reactions[it.Name] ?: continue
            it.Amount -= checkOverSupply(it.Name, it.Amount)
            for (it2 in reactionFromElement.Ingredients) {
                val amountMainElement = if(it.Amount > reactionFromElement.MainElement.Amount)
                    ceil(it.Amount.toDouble()/reactionFromElement.MainElement.Amount.toDouble()).toInt()*reactionFromElement.MainElement.Amount
                else if(it.Amount <= reactionFromElement.MainElement.Amount && it.Amount != 0)
                    reactionFromElement.MainElement.Amount
                else
                    0

                if(amountMainElement > it.Amount){
                    addOverSupply(it.Name, amountMainElement-it.Amount)
                }

                val multiplyIngredient = amountMainElement/reactionFromElement.MainElement.Amount
                it2.Amount *= multiplyIngredient
                it2.Amount -= checkOverSupply(it2.Name, it2.Amount)
                val element = reactions[it2.Name]?.MainElement?.IsRootElement
                if(element != null)
                    it2.IsRootElement = element
                newConversionReactionsList.add(it2)
            }
        }
        conversionReactions = consolidateConversionReactions(newConversionReactionsList)
    }
    var ores = 0
    for (it in conversionReactions) {
        val reaction = reactions[it.Name]
        if(reaction != null){
            var newAmount = 0
            //it.Amount -= checkOverSupply(it.Name, it.Amount)
            if(it.Amount > reaction.MainElement.Amount)
                while(newAmount < it.Amount){
                    newAmount += reaction.MainElement.Amount
                }
            else
                newAmount = reaction.MainElement.Amount
            //if(newAmount > it.Amount){
             //   addOverSupply(it.Name, newAmount-it.Amount)
            //}
            newAmount /= reaction.MainElement.Amount
            it.Amount = reaction.Ingredients.first().Amount*newAmount
        }
        ores += it.Amount
    }
    println("Ores needed: $ores")
}

fun addOverSupply(Name: String, Amount: Int){
    if(overSupplyElements.containsKey(Name)){
        val value = overSupplyElements[Name]
        if(value != null)
            overSupplyElements[Name] = value+Amount
    }
    else{
        overSupplyElements[Name] = Amount
    }
}

fun checkOverSupply(Name: String, Amount: Int): Int{
    if(overSupplyElements.containsKey(Name)){
        val value = overSupplyElements[Name]
        if(value != null) {
            if (value > Amount) {
                overSupplyElements[Name] = value - Amount
                return Amount
            } else {
                overSupplyElements.remove(Name)
                return value
            }
        }
    }
    return 0
}

fun consolidateConversionReactions(list: MutableList<Element>): MutableList<Element>{
    val consolidateHashMap = HashMap<String, Int>()
    val newList = mutableListOf<Element>()
    list.forEach {
        if(consolidateHashMap.containsKey(it.Name)){
            val value = consolidateHashMap[it.Name]
            if(value != null)
                consolidateHashMap[it.Name] = value + it.Amount
        }
        else{
            consolidateHashMap[it.Name] = it.Amount
        }
    }
    consolidateHashMap.forEach { (t, u) ->
        val isRootElement = list.first { it.Name == t }.IsRootElement
        newList.add(Element(t, u, isRootElement))
    }
    return newList
}

























fun parseInput(input: List<List<String>>){
    input.forEach {
        var reaction = Reaction()
        var mainElementList = it[1].trim().split(" ")
        var mainElement = Element(mainElementList[1].trim(), mainElementList[0].trim().toInt())
        reaction.MainElement = mainElement
        it[0].split(",").forEach {
            var ingredientElementList = it.trim().split(" ")
            var ingredient = Element(ingredientElementList[1].trim(), ingredientElementList[0].trim().toInt())
            reaction.Ingredients.add(ingredient)
        }
        if(reaction.Ingredients.all{it.Name == "ORE"}) reaction.MainElement.IsRootElement = true
        reactions.put(reaction.MainElement.Name, reaction)
    }
}

class Reaction{
    var MainElement = Element()
    var Ingredients = mutableListOf<Element>()
}

class Element{
    constructor(){
        Name = ""
        Amount = 0
        IsRootElement = false
    }

    constructor(name: String, amount: Int, isRootElement: Boolean = false){
        Name = name
        Amount = amount
        IsRootElement = isRootElement
    }
    var Name = ""
    var Amount = 0
    var IsRootElement = false
}

