package basecode.com.ui.features.new

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.user.UserViewController
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.ScanQRCode
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.layout_header_new_app.view.*
import kotlinx.android.synthetic.main.screen_main.view.*
import org.koin.standalone.inject

class MainViewController : ViewController(null) {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private var isLogin = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_main, container, false)
    }

    override fun initPostCreateView(view: View) {
        handleView(view)
    }

    private fun handleView(view: View) {
        view.btnAccount.setOnClickListener {
            if (doubleTouchPrevent.check("btnAccount")) {

            }
        }
        view.btnReturnBook.setOnClickListener {
            if (doubleTouchPrevent.check("btnReturnBook")) {

            }
        }
        view.btnBorrowBook.setOnClickListener {
            if (doubleTouchPrevent.check("btnBorrowBook")) {

            }
        }
        view.btnSearchBooth.setOnClickListener {
            if (doubleTouchPrevent.check("btnSearchBooth")) {

            }
        }
        view.ivLogin.setOnClickListener {
            if (doubleTouchPrevent.check("ivLogin")) {
                if (isLogin) {
                    router.pushController(
                        RouterTransaction.with(
                            UserViewController()
                        ).pushChangeHandler(FadeChangeHandler(false))
                    )
                } else {
                    val bundle =
                        LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.Normal.value)
                    val loginViewController = LoginViewController(bundle)
                    router.pushController(
                        RouterTransaction.with(loginViewController)
                            .pushChangeHandler(FadeChangeHandler(false))
                    )
                }
            }
        }

        view.ivScanQRCode.setOnClickListener {
            if (doubleTouchPrevent.check("ivScanQRCode")) {
                ScanQRCode.openScreenQRCode(activity, this)
            }
        }
    }
}