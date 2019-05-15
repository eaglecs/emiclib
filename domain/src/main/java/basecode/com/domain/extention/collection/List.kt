package com.basecode.domain.extention.collection

fun <T> List<T>?.getValueOrDefault(): List<T> {
    return this ?: mutableListOf()
}