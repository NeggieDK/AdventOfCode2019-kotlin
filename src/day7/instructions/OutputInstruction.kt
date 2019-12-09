package day7.instructions

import day7.IntInstruction
import day7.ParameterMode
import day7.dynamicInput
import day7.newDynamicInput

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
        dynamicInput.add(param1Value)
        newDynamicInput.add(param1Value)
        println(param1Value)
        IntInstruction.Pointer+=2
        return IntInstruction.Pointer
    }
}