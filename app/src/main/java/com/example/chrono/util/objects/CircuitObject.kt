package com.example.chrono.util.objects

class CircuitObject {
    var name: String? = null
    var sets: Int? = null
    var work: Int? = null
    var rest: Int? = null

    override fun toString(): String {
        return "Circuit [name: ${this.name}, sets: ${this.sets}, work: ${this.work}, rest: ${this.rest}]"
    }
}