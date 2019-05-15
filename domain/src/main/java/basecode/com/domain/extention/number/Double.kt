package basecode.com.domain.extention.number

import basecode.com.domain.util.DecimalUtils
import java.util.*

fun Double?.valueOrDefault(default: Double): Double {
    return this ?: default
}

fun Double?.valueOrZero(): Double {
    return this.valueOrDefault(0.0)
}

fun Double.formatNumber(): String {
    return DecimalUtils.decimalFormat(this, 2, Locale.ENGLISH, DecimalUtils.GroupDecimalMode.COMMA, DecimalUtils.AfterDotMode.IF_EXIST)
}

fun Double.roundUp(numberDigit: Int): Double {
    return DecimalUtils.roundNumber(this, numberDigit, DecimalUtils.RoundMode.UP)
}

fun Double.roundDown(numberDigit: Int): Double {
    return DecimalUtils.roundNumber(this, numberDigit, DecimalUtils.RoundMode.DOWN)
}

fun Double.roundNatural(numberDigit: Int): Double {
    return DecimalUtils.roundNumber(this, numberDigit, DecimalUtils.RoundMode.NATURAL)
}
fun Double.roundNumber(): Double {
    return DecimalUtils.roundNumber(this, 0, DecimalUtils.RoundMode.NATURAL)
}