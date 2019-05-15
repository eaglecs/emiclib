package basecode.com.ui.extension.view

import android.graphics.Paint
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import basecode.com.ui.base.listview.view.ViewSize
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

inline fun <T : View> T.afterMeasured(crossinline f: T.(ViewSize) -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                val viewSize = ViewSize(measuredWidth, measuredHeight)
                f(viewSize)
            }
        }
    })
}


inline fun <T : View> T.afterMeasuredSize(crossinline f: T.(ViewSize) -> Unit) {
    this.afterMeasured { viewSize ->
        f(viewSize)
    }
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.isGone(): Boolean {
    return this.visibility == View.GONE
}

fun View.isInvisible(): Boolean {
    return this.visibility == View.INVISIBLE
}

fun View.shake() {
    YoYo.with(Techniques.Shake)
            .duration(700)
            .repeat(1)
            .playOn(this)
}

fun View.showZoomIn(duration: Long) {
    YoYo.with(Techniques.ZoomIn)
            .onStart {
                this.visible()
            }
            .duration(duration)
            .playOn(this)
}

fun View.hideZoomOut(duration: Long) {
    YoYo.with(Techniques.ZoomOut)
            .duration(duration)
            .onEnd {
                this.gone()
            }.onCancel {
                this.gone()
            }
            .playOn(this)
}

fun View.showFadeInDown(duration: Long) {
    YoYo.with(Techniques.FadeInDown)
            .onStart {
                this.visible()
            }
            .duration(duration)
            .playOn(this)
}

fun View.hideFadeInUp(duration: Long) {
    YoYo.with(Techniques.FadeInUp)
            .onEnd {
                this.gone()
            }
            .onCancel {
                this.gone()
            }
            .duration(duration)
            .playOn(this)
}


fun View.hideFadeInDown(duration: Long) {
    YoYo.with(Techniques.FadeInDown)
            .onEnd {
                this.gone()
            }
            .onCancel {
                this.gone()
            }
            .duration(duration)
            .playOn(this)
}

fun View.showFadeInUp(duration: Long) {
    YoYo.with(Techniques.FadeInUp)
            .onStart {
                this.visible()
            }
            .duration(duration)
            .playOn(this)
}

fun TextView.strikeThrough() {
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextView.notStrikeThrough() {
    this.paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}
