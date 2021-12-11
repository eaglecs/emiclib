package basecode.com.ui.features.bookdetail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import basecode.com.ui.R
import basecode.com.ui.util.DoubleTouchPrevent
import kotlinx.android.synthetic.main.view_image_overlay.view.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

@SuppressLint("ViewConstructor")
class ImageOverlayView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val onActionClose: () -> Unit
) : LinearLayout(context, attrs, defStyleAttr), KoinComponent {

    private val doubleTouchPrevent by inject<DoubleTouchPrevent>()

    init {
        View.inflate(context, R.layout.view_image_overlay, this)
        setBackgroundColor(Color.TRANSPARENT)
        ivClose.setOnClickListener {
            if (doubleTouchPrevent.check("ivClose")) {
                onActionClose.invoke()
            }
        }
    }
}