package basecode.com.ui.extension.view

import androidx.core.content.ContextCompat
import android.widget.TextView

fun TextView.setColorText(color: Int) {
    this.setTextColor(ContextCompat.getColor(context, color))
}