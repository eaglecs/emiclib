package com.basecode.domain.extention.collection

fun <T> MutableList<T>?.getValueOrDefault(): MutableList<T> {
    return this ?: mutableListOf()
}