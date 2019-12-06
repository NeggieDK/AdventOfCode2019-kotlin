package day5

import day5.instructions.*

class InstructionFactory{
    fun getInstruction(intIntstruction: IntInstruction): Instruction {
        if(intIntstruction.OpCode == 1){
            return AddInstruction(intIntstruction)
        }
        else if(intIntstruction.OpCode == 2){
            return MultiplyInstruction(intIntstruction)
        }
        else if(intIntstruction.OpCode == 3){
            return InputInstruction(intIntstruction)
        }
        else if(intIntstruction.OpCode == 4){
            return OutputInstruction(intIntstruction)
        }
        else if(intIntstruction.OpCode == 5){
            return JumpIfTrueInstruction(intIntstruction)
        }
        else if(intIntstruction.OpCode == 6){
            return JumpIfFalseInstruction(intIntstruction)
        }
        else if(intIntstruction.OpCode == 7){
            return LessThanInstruction(intIntstruction)
        }
        else if(intIntstruction.OpCode == 8){
            return EqualsInstruction(intIntstruction)
        }
        else if(intIntstruction.OpCode == 99){
            return HaltInstruction(intIntstruction)
        }
        else{
            throw IllegalArgumentException()
        }
    }

    fun getInstruction(): Instruction {
        return StartInstruction()
    }
}