package ca.chronofit.chrono.util.objects

import android.content.res.Resources
import ca.chronofit.chrono.R

class CircuitObject {
    var name: String? = null
    var sets: Int? = null
    var work: Int? = null
    var rest: Int? = null
    var iconId: Int? = null

    override fun toString(): String {
        return "Circuit [name: ${this.name}, sets: ${this.sets}, work: ${this.work}, rest: ${this.rest}]"
    }

    fun generateDeeplinkURL(): String {
        return "https://www.chronofit.ca/${Resources.getSystem().getString(R.string.share_circuit_url_suffix)}?${Resources.getSystem().getString(R.string.name)}=\"$name\"&${Resources.getSystem().getString(R.string.sets)}=\"$sets\"&${Resources.getSystem().getString(R.string.work)}=\"$work\"&${Resources.getSystem().getString(R.string.rest)}=\"$rest\""
    }

}