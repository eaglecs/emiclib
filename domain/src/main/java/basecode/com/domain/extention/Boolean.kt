package basecode.com.domain.extention

fun Boolean?.valueOrDefault(default: Boolean): Boolean {
    return this ?: default
}

fun Boolean?.valueOrFalse(): Boolean {
    return this.valueOrDefault(false)
}