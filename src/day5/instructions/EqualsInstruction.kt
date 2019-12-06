package day5.instructions

import day5.IntInstruction
import day5.ParameterMode

class EqualsInstruction(intInstruction: IntInstruction) : Instruction {
    var IntInstruction = intInstruction

    override fun execute(): Int {
        val param1Value : Int
        val param2Value : Int
        if(IntInstruction.Parameter1?.Mode  == ParameterMode.Position){
            param1Value = IntInstruction.IntCodesList[IntInstruction.Parameter1?.Value!!]
        }
        else{
            param1Value = IntInstruction.Parameter1?.Value!!
        }
        if(IntInstruction.Parameter2?.Mode  == ParameterMode.Position){
            param2Value = IntInstruction.IntCodesList[IntInstruction.Parameter2?.Value!!]
        }
        else{
            param2Value = IntInstruction.Parameter2?.Value!!
        }
        if(param1Value == param2Value){
            IntInstruction.IntCodesList[IntInstruction.Parameter3?.Value!!] = 1
        }
        else{
            IntInstruction.IntCodesList[IntInstruction.Parameter3?.Value!!] = 0
        }
        IntInstruction.Pointer+=4
        return IntInstruction.Pointer
    }
}