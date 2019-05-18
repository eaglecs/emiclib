package basecode.com.ui.base.loading

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import basecode.com.ui.R

class ContentLoadingProgressBar : FrameLayout {
    companion object {
        const val MIN_SHOW_TIME = 500L // ms
        const val MIN_DELAY = 500L // ms
    }

    var mStartTime: Long = -1L
    private val mDelayedHide: Runnable = Runnable {
        visibility = View.GONE
        mStartTime = -1L
    }
    private val mDelayedShow: Runnable = Runnable {
        mStartTime = SystemClock.uptimeMillis()
        visibility = View.VISIBLE
    }
    private var mIsShown: Boolean = false
    var onViewDisplayEvent: (() -> Unit)? = null
    var onViewHideEvent: (() -> Unit)? = null
    var mIsAttachedToWindow: Boolean = false

    constructor(context: Context?) : super(context) {
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mIsShown = visibility == View.VISIBLE
        initView(context)
    }

    private fun initView(context: Context?) {
        inflate(context, R.layout.layout_progress_bar_of_app, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mIsAttachedToWindow = true
        if ((mIsShown && visibility != View.VISIBLE)) {
            postDelayed(mDelayedShow, MIN_DELAY)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mIsAttachedToWindow = false
        removeCallbacks(mDelayedHide)
        removeCallbacks(mDelayedShow)
        if (!mIsShown && mStartTime != -1L) {
            visibility = View.GONE
        }
        mStartTime = -1L
    }

    fun hide() {
        if (mIsShown) {
            mIsShown = false
            if (mIsAttachedToWindow) {
                removeCallbacks(mDelayedShow)
            }
            val diff = SystemClock.uptimeMillis() - mStartTime
            if (mStartTime == -1L || diff >= MIN_SHOW_TIME) {
                // The progress spinner has been shown lng enough
                // OR was not shown yet. If it wasn't shown yet,
                // it will just never be shown.
                visibility = View.GONE
                mStartTime = -1L
            } else {
                // The progress spinner is shown, but not lng enough,
                // so put a delayed message in to hide it when its been
                // shown lng enough.
                postDelayed(mDelayedHide, MIN_SHOW_TIME - diff)
            }
        }
    }

    fun show() {
        if (!mIsShown) {
            mIsShown = true
            if (mIsAttachedToWindow) {
                removeCallbacks(mDelayedHide)
                if (mStartTime == -1L) {
                    postDelayed(mDelayedShow, MIN_DELAY)
                }
            }
        }
    }

    override fun onVisibilityChanged(changedView: View?, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (changedView === this) {
            if (visibility == View.VISIBLE) {
                onViewDisplayEvent?.invoke()
            } else {
                onViewHideEvent?.invoke()
            }
        }
    }
}