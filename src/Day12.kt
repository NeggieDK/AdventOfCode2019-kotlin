package day12

import kotlin.math.abs

fun main(){
    //var moon1 = Moon(-1, 0,2, "Moon1")
    //var moon2 = Moon(2, -10,-7, "Moon2")
    //var moon3 = Moon(4, -8,8, "Moon3")
    //var moon4 = Moon(3, 5,-1, "Moon4")
    var moon1 = Moon(-8, -10,0, "Moon1")
    var moon2 = Moon(5, 5,10, "Moon2")
    var moon3 = Moon(2, -7,3, "Moon3")
    var moon4 = Moon(9, -8,-3, "Moon4")
    var jupiter = Jupiter()
    jupiter.Moons.add(moon1)
    jupiter.Moons.add(moon2)
    jupiter.Moons.add(moon3)
    jupiter.Moons.add(moon4)

    jupiter.simulateManySteps(10)
    val totalEnergy = jupiter.getTotalEnergy()
    println("Total energy: $totalEnergy")

    val steps = jupiter.stepsUntilInitialState()
    println("Steps until initial: $steps")
}

class  Jupiter{
    var Moons = mutableListOf<Moon>()
    var currentStep = 1

    fun stepsUntilInitialState(){
        var found = false
        var steps = 1L
        while(!found){
            timestep()
            steps++
            if(steps > 0){
                var test = true
                Moons.forEach { if(!it.isBackToInitialPosition()) test = false}
                if(test){
                    println("Amount: $steps")
                    found = true
                }
            }

        }
    }

    fun simulateManySteps(amount: Int){
        for(i in 0 until amount){
            timestep()
        }
    }

    fun getTotalEnergy(): Int{
        return Moons.map { it.getTotalEnergy() }.sum()
    }

    fun timestep(){
        //println("Step $currentStep")
        currentStep++
        updateVelocities()
        Moons.forEach { it.Move() }
        //Moons.forEach { println(it) }
        //println()
    }

    fun updateVelocities(){
        Moons.forEach {
            Moons.forEach { it3 ->
                if(it != it3){

                        if(it.X > it3.X)
                            it.XVelocity--
                        else if(it.X < it3.X)
                            it.XVelocity++

                        if(it.Y > it3.Y)
                            it.YVelocity--
                        else if(it.Y < it3.Y)
                            it.YVelocity++

                        if(it.Z > it3.Z)
                            it.ZVelocity--
                        else if(it.Z < it3.Z)
                            it.ZVelocity++
                    }

            }
            it.updatePosition()
        }
    }
}

class Moon{
    constructor(x: Int, y: Int, z: Int, name: String){
        X = x
        Y = y
        Z = z
        Name = name
        InitialX = x
        InitialY = y
        InitialZ = z
        NewX = x
        NewY = y
        NewZ = z
    }
    var Name = ""
    var X = 0
    var Y = 0
    var Z = 0
    var InitialX = 0
    var InitialY = 0
    var InitialZ = 0

    var XVelocity = 0
    var YVelocity = 0
    var ZVelocity = 0

    var NewX = 0
    var NewY = 0
    var NewZ = 0

    fun updatePosition(){
        NewX += XVelocity
        NewY += YVelocity
        NewZ += ZVelocity
    }

    override fun toString(): String {
        return "Moon: $Name, ($X, $Y, $Z) with velocity ($XVelocity, $YVelocity, $ZVelocity)"
    }

    private fun getKineticEnergy() : Int{
        return abs(XVelocity) + abs(YVelocity) + abs(ZVelocity)
    }

    private fun getPotentialEnergy() : Int{
        return abs(X) + abs(Y) + abs(Z)
    }

    fun getTotalEnergy() : Int{
        return getKineticEnergy() * getPotentialEnergy()
    }

    fun isBackToInitialPosition(): Boolean{
        return X == InitialX && Y == InitialY && Z == InitialZ
    }

    fun Move(){
        X = NewX
        Y = NewY
        Z = NewZ
    }

}