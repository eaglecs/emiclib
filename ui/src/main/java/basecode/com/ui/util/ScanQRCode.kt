package basecode.com.ui.util

import android.app.Activity
import android.content.Intent
import basecode.com.ui.QrCodeScanActivity
import basecode.com.ui.base.controller.viewcontroller.ViewController

object ScanQRCode {
    fun openScreenQRCode(activity: Activity?, viewController: ViewController, type: TypeScan = TypeScan.Book) {
        val intent = Intent(activity, QrCodeScanActivity::class.java)
        intent.putExtra("Type", type.value)
        viewController.startActivity(intent)
    }

    enum class TypeScan(val value: String){
        Book("Book"), BookCode("BookCode")
    }
}