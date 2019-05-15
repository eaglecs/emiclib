package basecode.com.domain.usecase.base

import java.net.UnknownHostException


class CrashlyticsResultListener : RawResultListener {
    override fun fail(throwable: Throwable) {
        if (throwable !is UnknownHostException) {
//         TODO add  Crashlytics.logException(throwable)
        }
    }
}