package day5

import day5.instructions.HaltInstruction

val cleanIntCodes = listOf(3,225,1,225,6,6,1100,1,238,225,104,0,1001,92,74,224,1001,224,-85,224,4,224,1002,223,8,223,101,1,224,224,1,223,224,223,1101,14,63,225,102,19,83,224,101,-760,224,224,4,224,102,8,223,223,101,2,224,224,1,224,223,223,1101,21,23,224,1001,224,-44,224,4,224,102,8,223,223,101,6,224,224,1,223,224,223,1102,40,16,225,1102,6,15,225,1101,84,11,225,1102,22,25,225,2,35,96,224,1001,224,-350,224,4,224,102,8,223,223,101,6,224,224,1,223,224,223,1101,56,43,225,101,11,192,224,1001,224,-37,224,4,224,102,8,223,223,1001,224,4,224,1,223,224,223,1002,122,61,224,1001,224,-2623,224,4,224,1002,223,8,223,101,7,224,224,1,223,224,223,1,195,87,224,1001,224,-12,224,4,224,1002,223,8,223,101,5,224,224,1,223,224,223,1101,75,26,225,1101,6,20,225,1102,26,60,224,101,-1560,224,224,4,224,102,8,223,223,101,3,224,224,1,223,224,223,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,108,677,226,224,102,2,223,223,1006,224,329,1001,223,1,223,1108,226,677,224,1002,223,2,223,1006,224,344,101,1,223,223,7,226,677,224,102,2,223,223,1006,224,359,1001,223,1,223,1007,226,677,224,1002,223,2,223,1006,224,374,1001,223,1,223,1108,677,226,224,102,2,223,223,1005,224,389,1001,223,1,223,107,226,226,224,102,2,223,223,1006,224,404,101,1,223,223,1107,226,226,224,1002,223,2,223,1005,224,419,1001,223,1,223,1007,677,677,224,102,2,223,223,1006,224,434,101,1,223,223,1107,226,677,224,1002,223,2,223,1006,224,449,101,1,223,223,107,677,677,224,102,2,223,223,1005,224,464,1001,223,1,223,1008,226,226,224,1002,223,2,223,1005,224,479,101,1,223,223,1007,226,226,224,102,2,223,223,1005,224,494,1001,223,1,223,8,677,226,224,1002,223,2,223,1005,224,509,1001,223,1,223,108,677,677,224,1002,223,2,223,1005,224,524,1001,223,1,223,1008,677,677,224,102,2,223,223,1006,224,539,1001,223,1,223,7,677,226,224,1002,223,2,223,1005,224,554,101,1,223,223,1108,226,226,224,1002,223,2,223,1005,224,569,101,1,223,223,107,677,226,224,102,2,223,223,1005,224,584,101,1,223,223,8,226,226,224,1002,223,2,223,1005,224,599,101,1,223,223,108,226,226,224,1002,223,2,223,1006,224,614,1001,223,1,223,7,226,226,224,102,2,223,223,1006,224,629,1001,223,1,223,1107,677,226,224,102,2,223,223,1005,224,644,101,1,223,223,8,226,677,224,102,2,223,223,1006,224,659,1001,223,1,223,1008,226,677,224,1002,223,2,223,1006,224,674,1001,223,1,223,4,223,99,226)
var intCodes = mutableListOf<Int>()
var instructionPointer = 0

fun main(){
    intCodes = cleanIntCodes.toMutableList()
    start()
}

fun start(){
    val factory = InstructionFactory()
    var currentInstruction = factory.getInstruction()
    instructionPointer = 0

    while(currentInstruction !is HaltInstruction) {
        val instruction = factory.getInstruction(getIntInstruction())
        currentInstruction = instruction
        instructionPointer = instruction.execute()
    }
    if(intCodes[0] == 19690720){
        println((100*intCodes[1]) + intCodes[2])
    }
}

fun getIntInstruction() : IntInstruction{
    val opcodeList = intCodes[instructionPointer].toList()
    val opcode = toOpcode(opcodeList)
    if(opcode.code == 1 || opcode.code == 2){
        val param1Mode = opcode.mode1
        val param2Mode = opcode.mode2
        val param3Mode = opcode.mode3
        val param1 = createParameter(param1Mode, intCodes[instructionPointer+1])
        val param2 = createParameter(param2Mode, intCodes[instructionPointer+2])
        val param3 = createParameter(param3Mode, intCodes[instructionPointer+3])
        val intInstruction = IntInstruction(opcode.code, param1, param2, param3, intCodes)
        intInstruction.Pointer = instructionPointer
        return intInstruction
    }
    else if(opcode.code == 3){
        val param = Parameter(intCodes[instructionPointer+1])
        param.Mode = ParameterMode.Immediate
        val intInstruction = IntInstruction(opcode.code, param, null, null, intCodes)
        intInstruction.Pointer = instructionPointer
        return intInstruction
    }
    else if(opcode.code == 4){
        val param1Mode = opcode.mode1
        val param = createParameter(param1Mode, intCodes[instructionPointer+1])
        val intInstruction = IntInstruction(opcode.code, param, null, null, intCodes)
        intInstruction.Pointer = instructionPointer
        return intInstruction
    }
    if(opcode.code == 5){
        val param1Mode = opcode.mode1
        val param2Mode = opcode.mode2
        val param1 = createParameter(param1Mode, intCodes[instructionPointer+1])
        val param2 = createParameter(param2Mode, intCodes[instructionPointer+2])
        val intInstruction = IntInstruction(opcode.code, param1, param2, null, intCodes)
        intInstruction.Pointer = instructionPointer
        return intInstruction
    }
    if(opcode.code == 6){
        val param1Mode = opcode.mode1
        val param2Mode = opcode.mode2
        val param1 = createParameter(param1Mode, intCodes[instructionPointer+1])
        val param2 = createParameter(param2Mode, intCodes[instructionPointer+2])
        val intInstruction = IntInstruction(opcode.code, param1, param2, null, intCodes)
        intInstruction.Pointer = instructionPointer
        return intInstruction
    }
    if(opcode.code == 7){
        val param1Mode = opcode.mode1
        val param2Mode = opcode.mode2
        val param3Mode = opcode.mode3
        val param1 = createParameter(param1Mode, intCodes[instructionPointer+1])
        val param2 = createParameter(param2Mode, intCodes[instructionPointer+2])
        val param3 = createParameter(param3Mode, intCodes[instructionPointer+3])
        val intInstruction = IntInstruction(opcode.code, param1, param2, param3, intCodes)
        intInstruction.Pointer = instructionPointer
        return intInstruction
    }
    if(opcode.code == 8){
        val param1Mode = opcode.mode1
        val param2Mode = opcode.mode2
        val param3Mode = opcode.mode3
        val param1 = createParameter(param1Mode, intCodes[instructionPointer+1])
        val param2 = createParameter(param2Mode, intCodes[instructionPointer+2])
        val param3 = createParameter(param3Mode, intCodes[instructionPointer+3])
        val intInstruction = IntInstruction(opcode.code, param1, param2, param3, intCodes)
        intInstruction.Pointer = instructionPointer
        return intInstruction
    }
    else if(opcode.code == 99){
        val intInstruction = IntInstruction(opcode.code)
        intInstruction.Pointer = instructionPointer
        return intInstruction
    } else{
        throw IllegalArgumentException()
    }
}

fun Int.toList() = this.toString().toList().map { it.toString().toInt() }.toList()

fun toOpcode(list: List<Int>) : OpCode{
    var done = true
    var size = list.size
    var index = size-1
    if(size == 1){
        return OpCode(list[index],0,0,0)
    }
    else if(size == 2){
        return OpCode((list[index-1]*10)+list[index],0,0,0)
    }
    var opcode = OpCode((list[index-1]*10)+list[index],0,0,0)
    index -= 1
    if(--index > -1){
        opcode.mode1 = list[index]
    }
    if(--index > -1){
        opcode.mode2 = list[index]
    }
    if(--index > -1){
        opcode.mode3 = list[index]
    }
    return opcode
}

fun createParameter(modeValue: Int, value: Int): Parameter{
    val param = Parameter(value)
    if(modeValue == 0){
        param.Mode = ParameterMode.Position
    }
    else {
        param.Mode = ParameterMode.Immediate
    }
    return param
}

data class OpCode(var code: Int, var mode1: Int, var mode2: Int, var mode3: Int)
