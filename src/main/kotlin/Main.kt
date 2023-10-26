import kotlin.system.exitProcess

fun main(args: Array<String>) {
    var parking: Parking? = null

    while (true) {
        val commandLine = readln().split("""\s+""".toRegex())
        val command = commandLine.first()

        if (parking == null && command != "create" && command != "exit") {
            println("Sorry, a parking lot has not been created.")
            continue
        }

        when (command) {
            "exit" -> exitProcess(0)
            "create" -> {
                parking = Parking(commandLine[1].toInt())
                println("Created a parking lot with ${commandLine[1]} spots.")
            }
            else -> handleParkingCommand(commandLine, parking!!)
        }
    }
}

fun handleParkingCommand(commandLine: List<String>, parking: Parking) {
    val command = commandLine.first()
    when {
        command == "park" -> {
            val registration = commandLine[1]
            val color = commandLine[2]
            val car = Car(registration, color)
            val spotNr = parking.park(car)
            if (spotNr != -1) {
                println("$color car parked in spot $spotNr.")
            } else {
                println("Sorry, the parking lot is full.")
            }
        }
        command == "leave" -> {
            val spotNr = commandLine[1].toInt()
            parking.leave(spotNr)
            println("Spot $spotNr is free.")
        }
        command == "status" -> parking.printStatus()
        command == "reg_by_color" -> parking.printRegistrationByColor(commandLine[1])
        command == "spot_by_color" -> parking.printSpotByColor(commandLine[1])
        command == "spot_by_reg" -> parking.printSpotsByRegestration(commandLine[1])
    }
}

data class Car(val registration: String, var color: String)


class Parking(val nrOfSpots: Int) {
    fun park(car: Car): Int{
        val foundSpot = spots.indexOfFirst {it == null}
        if (foundSpot == -1) {
            return -1
        }
        spots[foundSpot] = car
        return foundSpot + 1

    }

    fun leave(spotNr: Int){
        spots[spotNr - 1] = null
    }

    fun printStatus(){
        val parkingIsEmpty = spots.all{ it == null }
        if (parkingIsEmpty){
            println("Parking lot is empty.")
        }else{
            spots.forEachIndexed { index, car ->
                val spotNr = index + 1
                if (car != null){
                    val regestartion = car.registration
                    val color = car.color
                    println("$spotNr $regestartion $color")
                }
            }
        }
    }

    fun printSpotByColor(color: String) {
        val foundSpots = spots.indices.filter { index -> spots[index]?.color?.equals(color, ignoreCase = true) == true }
        if (foundSpots.isNotEmpty()) {
            println(foundSpots.map { it + 1 }.joinToString())
        } else {
            println("No cars with color $color were found.")
        }
    }

    fun printSpotsByRegestration(registration: String) {
        val found = spots.indexOfFirst { car -> car?.registration == registration }
        if (found == -1) {
            println("No cars with registration number $registration were found.")
        } else {
            println(found + 1)
        }
    }

    fun printRegistrationByColor(color: String) {
        val foundCars = spots.filter { it?.color?.equals(color, ignoreCase = true) == true }
        if (foundCars.isNotEmpty()) {
            val foundRegistrations = foundCars.map { car -> car?.registration }
            println(foundRegistrations.joinToString())
        } else {
            println("No cars with color $color were found.")
        }
    }
    val spots = MutableList<Car?>(nrOfSpots){ null }
}