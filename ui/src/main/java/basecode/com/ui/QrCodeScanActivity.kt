package basecode.com.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.ResultScanQRCodeBookCodeEventBus
import basecode.com.domain.eventbus.model.ResultScanQRCodeEventBus
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrDefault
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.network.response.ScanQRCodeResponse
import basecode.com.ui.util.ScanQRCode
import com.google.gson.Gson
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.lang.Exception

class QrCodeScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private val requestCodePermissionsCamera = 9999

    private lateinit var mScannerView: ZXingScannerView
    private var isRequestedPermission: Boolean = false
    private var type = ScanQRCode.TypeScan.Book.value
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type =
            intent.getStringExtra("Type").valueOrDefault(ScanQRCode.TypeScan.Book.value)
        mScannerView = ZXingScannerView(this)
        //mScannerView.setAspectTolerance(0.5f)
        setContentView(mScannerView)

        isRequestedPermission = false
        checkPermissionCamera {
            onCheckPermissionSuccess()
        }
    }

    private fun onCheckPermissionSuccess() {
        isRequestedPermission = true
    }

    private fun checkPermissionCamera(successEvent: () -> Unit) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            successEvent.invoke()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), requestCodePermissionsCamera)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isRequestedPermission) {
            mScannerView.setResultHandler(this)
            mScannerView.startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isRequestedPermission) {
            mScannerView.stopCamera()
        }
    }

    override fun handleResult(rawResult: Result?) {
        rawResult?.let { result ->
            val contentQRCode = result.text.valueOrEmpty()
            if (type == ScanQRCode.TypeScan.Book.value){
                try {
                    val gson = Gson()
                    val infoBook = gson.fromJson(contentQRCode, ScanQRCodeResponse::class.java)
                    val bookId = infoBook.id.valueOrZero()
                    if (bookId> 0){
                        KBus.post(ResultScanQRCodeEventBus(bookId = bookId, docType = infoBook.docType.valueOrZero()))
                    }
                    finish()
                } catch (e: Exception){
                    finish()
                }
            } else {
                KBus.post(ResultScanQRCodeBookCodeEventBus(bookCode = contentQRCode))
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == requestCodePermissionsCamera) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                onCheckPermissionSuccess()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

