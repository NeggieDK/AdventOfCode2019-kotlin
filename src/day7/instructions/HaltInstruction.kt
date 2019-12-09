package day7.instructions

import day7.IntInstruction

class HaltInstruction(intInstruction: IntInstruction) : Instruction {
    var IntInstruction = intInstruction

    override fun execute(): Int {
        val pointer = IntInstruction.Pointer++
        return pointer
    }
}