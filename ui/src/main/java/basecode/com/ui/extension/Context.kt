package basecode.com.ui.extension

import android.content.Context
import android.support.annotation.PluralsRes
import basecode.com.ui.R
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat

fun Context.showAlert(msg: String, title: String, cancelable: Boolean = true, onActionPositive: (() -> Unit)?, onActionNegative: (() -> Unit)? = null, onActionNeutral: (() -> Unit)?) {
    this.alert(Appcompat, msg, title)
    {
        positiveButton(buttonText = getString(R.string.ACTION_RETRY), onClicked = {
            onActionPositive?.invoke()
        })
        negativeButton(buttonText = getString(R.string.ACTION_CANCEL), onClicked = {
            onActionNegative?.invoke()
        })
        neutralPressed(buttonText = "", onClicked = {
            onActionNeutral?.invoke()
        })
    }.show().setCancelable(cancelable)
}

fun Context.showAlert(msg: String, title: String, cancelable: Boolean = true, onActionPositive: (() -> Unit)?, onActionNegative: (() -> Unit)? = null) {
    this.alert(Appcompat, msg, title)
    {
        positiveButton(buttonText = getString(R.string.ACTION_YES), onClicked = {
            onActionPositive?.invoke()
        })
        negativeButton(buttonText = getString(R.string.ACTION_NO), onClicked = {
            onActionNegative?.invoke()
        })
    }.show().setCancelable(cancelable)
}

fun Context.showAlert(msg: String, title: String, cancelable: Boolean = true, onActionPositive: (() -> Unit)?) {
    this.alert(Appcompat, msg, title)
    {
        positiveButton(buttonText = getString(R.string.ACTION_OK), onClicked = {
            onActionPositive?.invoke()
        })

    }.show().setCancelable(cancelable)
}

fun Context.showAlert(msg: String, title: String, positiveStr: String, negative: String, cancelable: Boolean = true, onActionPositive: (() -> Unit)?, onActionNegative: (() -> Unit)?) {
    this.alert(Appcompat, msg, title)
    {
        positiveButton(buttonText = positiveStr, onClicked = {
            onActionPositive?.invoke()
        })
        negativeButton(buttonText = negative, onClicked = {
            onActionNegative?.invoke()
        })
    }.show().setCancelable(cancelable)
}

fun Context.showAlert(msg: String, title: String, positiveStr: String, negative: String, neutral: String, cancelable: Boolean = true, onActionPositive: (() -> Unit)?, onActionNegative: (() -> Unit)?, onActionNeutral: (() -> Unit)?) {
    this.alert(Appcompat, msg, title)
    {
        positiveButton(buttonText = positiveStr, onClicked = {
            onActionPositive?.invoke()
        })
        negativeButton(buttonText = negative, onClicked = {
            onActionNegative?.invoke()
        })
        neutralPressed(buttonText = neutral, onClicked = {
            onActionNeutral?.invoke()
        })
    }.show().setCancelable(cancelable)
}

fun Context.getQuantityString(@PluralsRes idStr: Int, quantity: Int): String {
    return this.resources.getQuantityString(idStr, quantity)
}

val Context.isTablet: Boolean
    get() = this.resources.getBoolean(R.bool.is_tablet)