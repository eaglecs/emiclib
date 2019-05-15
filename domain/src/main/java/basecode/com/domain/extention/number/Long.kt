package basecode.com.domain.extention.number

import java.util.*

fun Long?.valueOrDefault(default: Long): Long {
    return this ?: default
}

fun Long?.valueOrZero(): Long {
    return this.valueOrDefault(0)
}

fun Long.formatTimeHHMM(): String {
    val second: Long = this / 1000
    var minute: Long = second / 60
    val hour: Long = minute / 60
    minute %= 60
    return String.format(Locale.ENGLISH, "%02d:%02d", hour, minute)
}
