package ca.chronofit.chrono.util.objects

class CircuitObject {
    var name: String? = null
    var sets: Int? = null
    var work: Int? = null
    var rest: Int? = null
    var iconId: Int? = null

    override fun toString(): String {
        return "Circuit [name: ${this.name}, sets: ${this.sets}, work: ${this.work}, rest: ${this.rest}]"
    }

    fun shareString(): String {
        return "Check out my Chrono Circuit: https://chronofit.page.link/circuitShare?name=\"$name\"\nSets=\"$sets\"\nWork=\"$work\"\nRest=\"$rest\""
    }

    fun shareURL(): String {
        return "https://www.chronofit.ca/?name=$name&Sets=$sets&Work=$work&Rest=$rest"
    }

}