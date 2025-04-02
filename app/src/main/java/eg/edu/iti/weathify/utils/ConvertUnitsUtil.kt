package eg.edu.iti.weathify.utils

object ConvertUnitsUtil {
    //wind speed converts
    fun toKmH(ms: Double) = ms * 3.6
    fun toMH(ms: Double) = ms * 2.23693629

    //temp units converts
    fun toF(c: Double): Double = (c * 1.8) + 32
    fun toK(c: Double) = c + 273.15
}