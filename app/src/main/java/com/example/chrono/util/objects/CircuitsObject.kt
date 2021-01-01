package com.example.chrono.util.objects

import java.io.Serializable

class CircuitsObject : Serializable {
    var key: String = "CIRCUITS"
    var circuits: ArrayList<CircuitObject>? = ArrayList()
}