package ca.chronofit.chrono.util.objects

class CircuitObject {
    var name: String? = null
    var sets: Int? = null
    var work: Int? = null
    var rest: Int? = null
    var count: Int? = null
    var iconId: Int? = null

    override fun toString(): String {
        return "Circuit [name: ${this.name}, sets: ${this.sets}, work: ${this.work}, rest: ${this.rest}]"
    }

    fun shareString(): String {
        return "Check out my Chrono Circuit: $name\nSets: $sets\nWork: $work sec\nRest: $rest sec"
    }
}