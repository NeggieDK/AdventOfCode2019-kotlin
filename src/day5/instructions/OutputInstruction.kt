package day5.instructions

import day5.IntInstruction
import day5.ParameterMode

class OutputInstruction(intInstruction: IntInstruction) : Instruction {
    var IntInstruction = intInstruction

    override fun execute(): Int {
        val param1Value : Int
        if(IntInstruction.Parameter1?.Mode  == ParameterMode.Position){
            param1Value = IntInstruction.IntCodesList[IntInstruction.Parameter1?.Value!!]
        }
        else{
            param1Value = IntInstruction.Parameter1?.Value!!
        }
        println(param1Value)
        IntInstruction.Pointer+=2
        return IntInstruction.Pointer
    }
}