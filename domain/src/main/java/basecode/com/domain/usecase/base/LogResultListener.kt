package basecode.com.domain.usecase.base



class LogResultListener : RawResultListener {
    override fun fail(throwable: Throwable) {
//        LogDebug.d(throwable, throwable.message.orEmpty())
    }
}