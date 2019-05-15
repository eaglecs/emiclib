package basecode.com.domain.extention.date

import basecode.com.domain.extention.toTimeMilliSecond
import java.text.SimpleDateFormat
import java.util.*

fun Date.calDiffTimeMiliToNow(date: String, format: String): Long {
    val timeMilliSecondDate = date.toTimeMilliSecond(format)
    val instance = Calendar.getInstance()
    var timeMilliSecondDateCurrent: Long = 0
    instance?.let {
        timeMilliSecondDateCurrent = it.timeInMillis
    }
    return timeMilliSecondDateCurrent - timeMilliSecondDate
}


fun Date.format(format: String): String {
    return SimpleDateFormat(format, Locale.US).format(this)
}

fun Long.toDate(): Date {
    return Date(this)
}