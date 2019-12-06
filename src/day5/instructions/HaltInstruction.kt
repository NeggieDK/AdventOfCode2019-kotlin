package day5.instructions

import day5.IntInstruction

class HaltInstruction(intInstruction: IntInstruction) : Instruction {
    var IntInstruction = intInstruction

    override fun execute(): Int {
        val pointer = IntInstruction.Pointer++
        return pointer
    }
}