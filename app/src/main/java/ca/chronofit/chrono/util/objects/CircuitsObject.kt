package ca.chronofit.chrono.util.objects

import ca.chronofit.chrono.util.constants.Constants
import java.io.Serializable

class CircuitsObject : Serializable {
    var key: String = Constants.CIRCUITS
    var circuits: ArrayList<CircuitObject>? = ArrayList()
}