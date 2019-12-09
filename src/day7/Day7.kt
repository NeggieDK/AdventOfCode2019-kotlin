package day7

import day7.instructions.HaltInstruction
import kotlin.system.measureTimeMillis

var cleanIntCodes = listOf(3,8,1001,8,10,8,105,1,0,0,21,38,55,80,97,118,199,280,361,442,99999,3,9,101,2,9,9,1002,9,5,9,1001,9,4,9,4,9,99,3,9,101,5,9,9,102,2,9,9,1001,9,5,9,4,9,99,3,9,1001,9,4,9,102,5,9,9,101,4,9,9,102,4,9,9,1001,9,4,9,4,9,99,3,9,1001,9,3,9,1002,9,2,9,101,3,9,9,4,9,99,3,9,101,5,9,9,1002,9,2,9,101,3,9,9,1002,9,5,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,99,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,99)
var intCodes = mutableListOf<Int>()
var instructionPointer = 0
val phases = mutableListOf<Int>()
var allPossiblePhases = mutableListOf<MutableList<Int>>()
var resultList = mutableListOf<Int>()

fun main(){
    for(i in 0..4){
        for(j in 0..4){
            if(j == i) continue
            for(k in 0..4){
                if(k == i || j == k) continue
                for(l in 0..4){
                    if(l == i || j == l || l == k) continue
                    for(m in 0..4){
                        if(m == i || j == m || l == m || m ==k) continue
                        allPossiblePhases.add(mutableListOf(i,j,k,l,m))
                    }
                }
            }
        }
    }

    val time = measureTimeMillis {
        allPossiblePhases.forEach {
            dynamicInput = mutableListOf(it[0], 0)
            newDynamicInput = mutableListOf(it[0])
            val test = mutableListOf(4, 3, 2, 1, 0)
            for (i in 0..4) {
                intCodes = cleanIntCodes.toMutableList()
                start()
                if (i == 4) {
                    resultList.add(newDynamicInput.last())
                    break
                }
                dynamicInput = mutableListOf(it[i+1], newDynamicInput.last())
                newDynamicInput = mutableListOf()
            }
        }
    }
    val max = resultList.max()
    println("Maximum thrust result: $max")
    println("Execution time: $time")
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
