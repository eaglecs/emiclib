package basecode.com.ui.util

import basecode.com.ui.BuildConfig
import timber.log.Timber

object LogDebug {
    fun d(throwable: Throwable, msg: String) {
        if (BuildConfig.DEBUG) {
            Timber.d(throwable, msg)
        }
    }
    fun d(msg: String) {
        if (BuildConfig.DEBUG) {
            Timber.d(msg)
        }
    }
    fun d(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Timber.d(throwable)
        }
    }
}