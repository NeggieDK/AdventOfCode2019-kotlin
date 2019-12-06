package day5.instructions

import day5.IntInstruction

class InputInstruction(intInstruction: IntInstruction) : Instruction {
    var IntInstruction = intInstruction

    override fun execute(): Int {
        println("Give input:")
        val result = Integer.valueOf(readLine())
        IntInstruction.IntCodesList[IntInstruction.Parameter1?.Value!!] = result
        IntInstruction.Pointer+=2
        return  IntInstruction.Pointer
    }
}