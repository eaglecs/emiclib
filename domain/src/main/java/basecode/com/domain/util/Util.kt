package basecode.com.domain.util

import java.util.*

object Util {

    fun getStringFromList(lstText: List<String>): String {
        val stringBuilder = StringBuilder()
        lstText.forEach { text ->
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append(',')
            }
            stringBuilder.append(text)
        }
        return stringBuilder.toString()
    }

    fun newUniqueID(): String = UUID.randomUUID().toString()

    fun decimalFormatAlwaysZero(value: Double, numberAfterDot: Int, locale: Locale, groupDecimalMode: DecimalUtils.GroupDecimalMode): String {
        return DecimalUtils.decimalFormat(value, numberAfterDot, locale, groupDecimalMode, DecimalUtils.AfterDotMode.ALWAYS_ZERO)
    }

    fun decimalFormatIfExist(value: Double, numberAfterDot: Int, locale: Locale, groupDecimalMode: DecimalUtils.GroupDecimalMode): String{
        return DecimalUtils.decimalFormat(value, numberAfterDot, locale, groupDecimalMode, DecimalUtils.AfterDotMode.IF_EXIST)
    }
}