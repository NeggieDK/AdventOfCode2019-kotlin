val cleanIntCodes = listOf(1,12,2,3,1,1,2,3,1,3,4,3,1,5,0,3,2,6,1,19,2,19,9,23,1,23,5,27,2,6,27,31,1,31,5,35,1,35,5,39,2,39,6,43,2,43,10,47,1,47,6,51,1,51,6,55,2,55,6,59,1,10,59,63,1,5,63,67,2,10,67,71,1,6,71,75,1,5,75,79,1,10,79,83,2,83,10,87,1,87,9,91,1,91,10,95,2,6,95,99,1,5,99,103,1,103,13,107,1,107,10,111,2,9,111,115,1,115,6,119,2,13,119,123,1,123,6,127,1,5,127,131,2,6,131,135,2,6,135,139,1,139,5,143,1,143,10,147,1,147,2,151,1,151,13,0,99,2,0,14,0)
var intCodes = mutableListOf<Int>()
var instructionPointer = 0

fun main(){
    for(i in 0..100){
        for(j in 0..100){
            intCodes = cleanIntCodes.toMutableList()
            intCodes[1] = i
            intCodes[2] = j
            start()
        }
    }
}

fun start(){
    var currentOpcode = 0
    instructionPointer = 0
    while(currentOpcode != 99){
        val instruction = getInstruction()
        currentOpcode = instruction[0]
        executeInstruction(instruction)
    }
    if(intCodes[0] == 19690720){
        println((100*intCodes[1]) + intCodes[2])
    }
}

fun getInstruction() : List<Int>{
    val opcode = intCodes[instructionPointer]
    return if(opcode == 1 || opcode == 2){
        val instruction = intCodes.subList(instructionPointer, instructionPointer+4)
        instructionPointer += 4
        instruction
    } else if(opcode == 99){
        instructionPointer++
        mutableListOf(99)
    } else{
        throw IllegalArgumentException()
    }
}

fun executeInstruction(instruction: List<Int>){
    val opcode = instruction[0]
    if(opcode == 99) return
    val val1 = intCodes[instruction[1]]
    val val2 = intCodes[instruction[2]]
    var result = 0
    if(opcode == 1){
        result = val1 + val2
    }
    else if(opcode == 2){
        result = val1 * val2
    }
    intCodes[instruction[3]] = result
}