package day5

class Parameter(value: Int) {
    var Value = value
    lateinit var Mode : ParameterMode
}

enum class ParameterMode{
    Position,
    Immediate
}