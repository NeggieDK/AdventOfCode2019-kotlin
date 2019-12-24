package day12

import kotlin.math.abs

fun main(){
    var moon1 = Moon(-10, -10,-13, "Moon1")
    var moon2 = Moon(5, 5,-9,"Moon2")
    var moon3 = Moon(3, 8,-16, "Moon3")
    var moon4 = Moon(1, 3,-3, "Moon4")
    var jupiter = Jupiter()
    jupiter.Moons.add(moon1)
    jupiter.Moons.add(moon2)
    jupiter.Moons.add(moon3)
    jupiter.Moons.add(moon4)

    //Uncomment for part 1
    //jupiter.simulateManySteps(2773)
    //val totalEnergy = jupiter.getTotalEnergy()
    //println("Total energy: $totalEnergy")

    val steps = jupiter.stepsUntilInitialState()
    var lcm = lcm(listOf(jupiter.xStep, jupiter.yStep, jupiter.zStep))
    println("Steps until initial: ${lcm*2}")
}

class  Jupiter{
    var Moons = mutableListOf<Moon>()
    var currentStep = 1L

    var xStep = 0L
    var yStep = 0L
    var zStep = 0L

    fun stepsUntilInitialState(){
        while(xStep == 0L || yStep == 0L || zStep == 0L){
            timestep()
            if(currentStep > 1){
                if(xStep == 0L && Moons.all { it.XVelocity == 0 })
                    xStep = currentStep -1

                if(yStep == 0L && Moons.all { it.YVelocity == 0 })
                    yStep = currentStep -1

                if(zStep == 0L && Moons.all { it.ZVelocity == 0 })
                    zStep = currentStep -1
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
        updatePositions()
        //Moons.forEach { println(it) }
        //println()
    }

    fun updatePositions(){
        Moons.forEach { it.updatePosition() }
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

    fun updatePosition(){
        X += XVelocity
        Y += YVelocity
        Z += ZVelocity
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
}

fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

fun lcm(numbers: List<Long>): Long{
    val numbersList = numbers.toMutableList()
    while(numbersList.size > 1){
        val result = lcm(numbersList[0], numbersList[1])
        numbersList.removeAt(0)
        numbersList[0] = result
    }
    return numbersList[0]
}