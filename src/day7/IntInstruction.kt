package day7

class IntInstruction(opCode: Int, parameter1: Parameter? = null, parameter2: Parameter? = null, parameter3: Parameter? = null, intCodesList: MutableList<Int> = mutableListOf<Int>()){
    var OpCode = opCode
    val Parameter1 = parameter1
    val Parameter2 = parameter2
    val Parameter3 = parameter3
    val IntCodesList = intCodesList
    var Pointer = 0
}