package day7.instructions

import day7.IntInstruction
import day7.ParameterMode

class JumpIfFalseInstruction(intInstruction: IntInstruction) : Instruction {
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
        if(param1Value == 0){
            return param2Value
        }
        IntInstruction.Pointer+=3
        return IntInstruction.Pointer
    }
}