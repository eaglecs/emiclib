package basecode.com.ui.features.new

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.LogoutSuccessEventBus
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.presentation.features.new.MainContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.user.UserViewController
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.GlideUtil
import basecode.com.ui.util.ScanQRCode
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.layout_header_new_app.view.*
import kotlinx.android.synthetic.main.screen_main.view.*
import org.koin.standalone.inject

class MainViewController : ViewController(null), MainContract.View {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private val presenter by inject<MainContract.Presenter>()
    private var isLogin = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_main, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initEventBus(view)
        handleView(view)
        presenter.checkLogin()
    }

    private fun initEventBus(view: View) {
        KBus.subscribe<LoginSuccessEventBus>(this) {
            isLogin = true
            GlideUtil.loadImage(
                url = it.avatar,
                imageView = view.ivLogin,
                holderImage = R.drawable.user_default,
                errorImage = R.drawable.user_default
            )
        }
        KBus.subscribe<LogoutSuccessEventBus>(this) {
            isLogin = false
            view.ivLogin.setImageResource(R.drawable.ic_login)
        }
    }

    private fun handleView(view: View) {
        view.btnAccount.setOnClickListener {
            if (doubleTouchPrevent.check("btnAccount")) {
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

    override fun handleAfterCheckLogin(isLogin: Boolean, avatar: String) {
        this.isLogin = isLogin
        view?.let { view ->
            if (isLogin) {
                GlideUtil.loadImage(
                    url = avatar,
                    imageView = view.ivLogin,
                    holderImage = R.drawable.user_default,
                    errorImage = R.drawable.user_default
                )
            } else {
                view.ivLogin.setImageResource(R.drawable.ic_login)
            }
        }
    }

    override fun showLoading() {
        view?.apply {
            vgLoading.show()
        }
    }

    override fun hideLoading() {
        view?.apply {
            vgLoading.hide()
        }
    }

    override fun onDestroyView(view: View) {
        presenter.detachView()
        KBus.unsubscribe(this)
        super.onDestroyView(view)
    }
}