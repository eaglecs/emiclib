package basecode.com.ui.util

import android.content.Intent
import basecode.com.ui.base.controller.viewcontroller.ViewController

object ShareUtil {
    fun shareViaMedia(subject: String, body: String, title: String, controller: ViewController, requestCode: Int) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)
        controller.startActivityForResult(Intent.createChooser(intent, title), requestCode)
    }
}