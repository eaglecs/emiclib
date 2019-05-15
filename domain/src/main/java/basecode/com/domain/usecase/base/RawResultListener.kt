package basecode.com.domain.usecase.base

interface RawResultListener {
    fun fail(throwable: Throwable)
}