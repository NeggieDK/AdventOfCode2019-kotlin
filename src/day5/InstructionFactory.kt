package day5

import day5.instructions.*

class InstructionFactory{
    fun getInstruction(intIntstruction: IntInstruction): Instruction {
        when (intIntstruction.OpCode) {
            1 -> {
                return AddInstruction(intIntstruction)
            }
            2 -> {
                return MultiplyInstruction(intIntstruction)
            }
            3 -> {
                return InputInstruction(intIntstruction)
            }
            4 -> {
                return OutputInstruction(intIntstruction)
            }
            5 -> {
                return JumpIfTrueInstruction(intIntstruction)
            }
            6 -> {
                return JumpIfFalseInstruction(intIntstruction)
            }
            7 -> {
                return LessThanInstruction(intIntstruction)
            }
            8 -> {
                return EqualsInstruction(intIntstruction)
            }
            99 -> {
                return HaltInstruction(intIntstruction)
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    fun getInstruction(): Instruction {
        return StartInstruction()
    }
}