package basecode.com.domain.util

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {

    fun getCurrentDateTimeStr(): String = getCurrentDateTimeStr(DateTimeFormat.SAVE_TIME_FORMAT)

    fun getCurrentDateTimeStr(timeFormat: DateTimeFormat): String = formatLongTime(Date().time, timeFormat)

    fun formatLongTime(date: Long, timeFormat: DateTimeFormat) = formatLongTime(date, timeFormat, Locale.ENGLISH)

    fun formatLongTime(date: Long, timeFormat: DateTimeFormat, locale: Locale) = SimpleDateFormat(timeFormat.value, locale).format(date)

    fun getCurrentTimeLong(): Long = Calendar.getInstance().timeInMillis

    fun formatTimeStrFromLong(diffTime: Long): String {
        val second = diffTime / 1000
        var minute = second / 60
        val hour = minute / 60
        minute %= 60
        return String.format(Locale.ENGLISH, "%02d:%02d", hour, minute)
    }

    fun convertDateTimeFormat(dateStr: String, outputFormat: DateTimeFormat) = convertDateTimeFormat(dateStr, DateTimeFormat.SAVE_TIME_FORMAT, outputFormat)

    fun convertDateTimeFormat(dateStr: String, currentFormat: DateTimeFormat, outputFormat: DateTimeFormat): String {
        var dateStrOuput = ""
        if (dateStr.isNotEmpty()) {
            dateStrOuput = formatLongTime(parseDateTime(dateStr, currentFormat).time, outputFormat)
        }
        return dateStrOuput
    }

    fun parseDateTime(dateTime: String, dateTimeFormat: DateTimeFormat): Date {
        var result = Date()
        try {
            val simpleDateFormat = SimpleDateFormat(dateTimeFormat.value, Locale.US)
            result = simpleDateFormat.parse(dateTime)
        } catch (e: Exception) {

        }
        return result
    }

    fun resetTime(date: Date): Date{
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

}