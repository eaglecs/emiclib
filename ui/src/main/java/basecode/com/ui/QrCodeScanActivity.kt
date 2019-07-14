package basecode.com.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.ResultScanQRCodeEventBus
import basecode.com.domain.extention.valueOrEmpty
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QrCodeScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private val requestCodePermissionsCamera = 9999

    private lateinit var mScannerView: ZXingScannerView
    private var isRequestedPermission: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            KBus.post(ResultScanQRCodeEventBus(contentQRCode))
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == requestCodePermissionsCamera) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                onCheckPermissionSuccess()
            }
        }
    }
}

