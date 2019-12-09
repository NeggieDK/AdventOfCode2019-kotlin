package day7.instructions

import day7.IntInstruction

class InputInstruction(intInstruction: IntInstruction) : Instruction {
    var IntInstruction = intInstruction

    override fun execute(): Int {
        //println("Give input:")
        val result = day7.dynamicInput.first()
        day7.dynamicInput.removeAt(0)
        IntInstruction.IntCodesList[IntInstruction.Parameter1?.Value!!] = result
        IntInstruction.Pointer+=2
        return  IntInstruction.Pointer
    }
}