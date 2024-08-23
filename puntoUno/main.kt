fun divisionR(dividendo:Int, divisor:Int):Int
{
    if(dividendo < divisor ){
        return 0
    }
    
    return 1 + divisionR(dividendo - divisor, divisor)
}

fun main() {
    print("Ingrese el dividendo: ")
    var dividendo = readln().toInt()
    print("Ingrese el divisor: ")
    var divisor = readln().toInt()
    
	val result = divisionR(dividendo,divisor)
    print("El resultado es: $result")
}