package basecode.com.ui.util

import android.app.Activity
import android.content.Intent
import basecode.com.ui.QrCodeScanActivity
import basecode.com.ui.base.controller.viewcontroller.ViewController

object ScanQRCode {
    fun openScreenQRCode(activity: Activity?, viewController: ViewController) {
        val intent = Intent(activity, QrCodeScanActivity::class.java)
        viewController.startActivity(intent)
    }
}