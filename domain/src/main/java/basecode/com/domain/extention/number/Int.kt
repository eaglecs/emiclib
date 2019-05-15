package basecode.com.domain.extention.number

import basecode.com.domain.util.DecimalUtils
import java.util.*

fun Int?.valueOrDefault(default: Int): Int {
    return this ?: default
}

fun Int?.valueOrZero(): Int {
    return this.valueOrDefault(0)
}

fun Int.formatNumber(): String {
    return DecimalUtils.decimalFormat(this.toDouble(), 0, Locale.ENGLISH, DecimalUtils.GroupDecimalMode.COMMA, DecimalUtils.AfterDotMode.IF_EXIST)
}
