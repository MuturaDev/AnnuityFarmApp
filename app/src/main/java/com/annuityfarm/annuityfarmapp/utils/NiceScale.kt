package com.annuityfarm.annuityfarmapp.utils

import java.lang.Math.*

/**
 * Instantiates a new instance of the NiceScale class.
 *
 * @param min Double The minimum data point.
 * @param max Double The maximum data point.
 */
class NiceScale(private var minPoint: Double, private var maxPoint: Double) {

    private var maxTicks = 15.0
    private var range: Double = 0.0
    var niceMin: Double = 0.0
    var niceMax: Double = 0.0
    var tickSpacing: Double = 0.0

    init {
        calculate()
    }

    /**
     * Calculate and update values for tick spacing and nice
     * minimum and maximum data points on the axis.
     */
    private fun calculate() {
        range = niceNum(maxPoint - minPoint, false)
        tickSpacing = niceNum(range / (maxTicks - 1), true)
        niceMin = floor(minPoint / tickSpacing) * tickSpacing
        niceMax = ceil(maxPoint / tickSpacing) * tickSpacing
    }

    /**
     * Returns a "nice" number approximately equal to range. Rounds
     * the number if round = true. Takes the ceiling if round = false.
     *
     * @param range Double The data range.
     * @param round Boolean Whether to round the result.
     * @return Double A "nice" number to be used for the data range.
     */
    private fun niceNum(range: Double, round: Boolean): Double {
        /** Exponent of range  */
        val exponent: Double = floor(log10(range))
        /** Fractional part of range  */
        val fraction: Double
        /** Nice, rounded fraction  */
        val niceFraction: Double

        fraction = range / pow(10.0, exponent)

        niceFraction = if (round) {
            when {
                fraction < 1.5 -> 1.0
                fraction < 3 -> 2.0
                fraction < 7 -> 5.0
                else -> 10.0
            }
        } else {
            when {
                fraction <= 1 -> 1.0
                fraction <= 2 -> 2.0
                fraction <= 5 -> 5.0
                else -> 10.0
            }
        }

        return niceFraction * pow(10.0, exponent)
    }

    /**
     * Sets the minimum and maximum data points.
     *
     * @param minPoint Double The minimum data point.
     * @param maxPoint Double The maximum data point.
     */
    fun setMinMaxPoints(minPoint: Double, maxPoint: Double) {
        this.minPoint = minPoint
        this.maxPoint = maxPoint
        calculate()
    }

    /**
     * Sets maximum number of tick marks we're comfortable with.
     *
     * @param maxTicks Double The maximum number of tick marks.
     */
    fun setMaxTicks(maxTicks: Double) {
        this.maxTicks = maxTicks
        calculate()
    }
}