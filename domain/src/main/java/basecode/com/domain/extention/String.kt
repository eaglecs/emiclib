package basecode.com.domain.extention

import basecode.com.domain.util.AccentRemover
import java.text.SimpleDateFormat
import java.util.*

fun String.calDiffTimeMiliToNow(format: String): Long {
    return try {
        val timeMilliSecondDateCurrent: Long = Calendar.getInstance(Locale.US).timeInMillis
        val timeMilliSecondDate = this.toDate(format).time
        timeMilliSecondDateCurrent - timeMilliSecondDate
    } catch (e: Exception) {
        0
    }
}

fun String.calDiffTimeMiliToDate(date: String, format: String): Long {
    try {
        Date()
        val timeMilliSecondDate = this.toDate(format).time
        val timeMilliSecondDateToDate: Long = date.toTimeMilliSecond(format)
        return timeMilliSecondDateToDate - timeMilliSecondDate
    } catch (e: Exception) {
    }
    return 0
}

fun String.toTimeMilliSecond(format: String): Long = this.toDate(format).time

fun String.removeAccentAndSpace(): String = AccentRemover.removeAccentAndSpace(this)

fun String?.valueOrDefault(default: String): String {
    return this ?: default
}

fun String?.valueOrEmpty(): String {
    return this.valueOrDefault("")
}

fun String.toDate(format: String): Date = SimpleDateFormat(format, Locale.US).parse(this)

fun String.parseLong(valueDefault: Long): Long {
    var result = valueDefault
    if (this.trim().isNotEmpty()) {
        try {
            result = this.replace(",","+").toLong()
        } catch (e: Exception) {
        }
    }
    return result
}

fun String.parseInt(valueDefault: Int): Int {
    var result = valueDefault
    if (this.trim().isNotEmpty()) {
        try {
            result = this.replace(",","+").toInt()
        } catch (e: Exception) {
        }
    }
    return result
}

fun String.parseDouble(valueDefault: Double): Double {
    var result = valueDefault
    if (this.trim().isNotEmpty()) {
        try {
            result = this.replace(",","+").toDouble()
        } catch (e: Exception) {
        }
    }
    return result
}

fun String.parseFloat(valueDefault: Float): Float {
    var result = valueDefault
    if (this.trim().isNotEmpty()) {
        try {
            result = this.replace(",","+").toFloat()
        } catch (e: Exception) {
        }
    }
    return result
}

fun String.checkParseDouble(): Boolean {
    if (this.trim().isNotEmpty()) {
        try {
            this.parseDouble(0.0)
        } catch (e: Exception) {
            return false
        }
    } else {
        return false
    }
    return true
}

fun String.parseBool(): Boolean = "true".equals(this, ignoreCase = true)

fun String.upperCase(): String {
    return this.toUpperCase()
}

fun String.lowerCase(): String {
    return this.toLowerCase()
}

fun String.getListStrFromStr(splitStr: String): List<String> {
    return this.trim().split(splitStr)
}

fun String.replace(vararg symbols: String): String {
    symbols.forEach { symbol ->
        this.replace(symbol, "")
    }
    return this
}

