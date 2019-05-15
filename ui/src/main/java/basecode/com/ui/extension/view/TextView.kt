package basecode.com.ui.extension.view

import android.support.v4.content.ContextCompat
import android.widget.TextView

fun TextView.setColorText(color: Int) {
    this.setTextColor(ContextCompat.getColor(context, color))
}