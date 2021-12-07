package basecode.com.ui.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.presentation.features.login.LoginContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraInt
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.extension.view.hideKeyboard
import basecode.com.ui.extension.view.shake
import basecode.com.ui.util.DoubleTouchPrevent
import com.jackandphantom.blurimage.BlurImage
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.screen_login.view.*
import org.koin.standalone.inject
import android.content.Intent
import android.net.Uri
import basecode.com.domain.model.network.response.UserModel
import basecode.com.ui.BuildConfig
import basecode.com.ui.extension.view.gone


class LoginViewController(bundle: Bundle) : ViewController(bundle = bundle), LoginContract.View {

    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private val presenter: LoginContract.Presenter by inject()
    private var type = LoginSuccessEventBus.Type.Normal

    object BundleOptions {
        var Bundle.type by BundleExtraInt("LoginViewController.type")
        fun create(type: Int) = Bundle().apply {
            this.type = type
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            type = when (options.type.valueOrZero()) {
                1 -> {
                    LoginSuccessEventBus.Type.HandleBook
                }
                2 -> {
                    LoginSuccessEventBus.Type.UserInfo
                }
                3 -> {
                    LoginSuccessEventBus.Type.BorrowBook
                }
                4 -> {
                    LoginSuccessEventBus.Type.Notify
                }
                5 -> {
                    LoginSuccessEventBus.Type.RenewBook
                }
                6 -> {
                    LoginSuccessEventBus.Type.DownloadBook
                }
                7 -> {
                    LoginSuccessEventBus.Type.ChangePass
                }
                8 -> {
                    LoginSuccessEventBus.Type.RequestDocument
                }
                9 -> {
                    LoginSuccessEventBus.Type.BookRoom
                }
                else -> {
                    LoginSuccessEventBus.Type.Normal
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_login, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        onHandleView(view)
    }

    private fun initView(view: View) {
        if (BuildConfig.USE_DATA_OTHER_APP) {
            view.tvForgotPass.gone()
            if (BuildConfig.USE_DATA_GSL) {
                view.ivLogoLogin.setImageResource(R.drawable.ic_logo_gsl)
            } else {
                view.ivLogoLogin.setImageResource(R.drawable.ic_logo_tdn)
            }
        } else {
            view.ivLogoLogin.setImageResource(R.drawable.ic_logo_new)
        }
    }

    private fun onHandleView(view: View) {
        view.ivBack.setOnClickListener {
            if (doubleTouchPrevent.check("ivBack")) {
                router.popController(this)
            }
        }

        view.tvCloseLogin.setOnClickListener {
            if (doubleTouchPrevent.check("ivCloseLogin")) {
                router.popController(this)
            }
        }
        view.btnLogin.setOnClickListener {
            if (doubleTouchPrevent.check("btnLogin")) {
                view.edtPassword.hideKeyboard()
                loginLib(view)
            }
        }
        view.tvForgotPass.setOnClickListener {
            if (doubleTouchPrevent.check("tvForgotPass")) {
                openWeb()
            }
        }

        view.edtPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                view.edtPassword.hideKeyboard()
                loginLib(view)
                true
            }
            false
        }
    }

    private fun openWeb() {
        val url = "https://lib.eiu.edu.vn/OForgotPassword.aspx"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun loginLib(view: View) {
        val userName = view.edtUserName.text.toString()
        val password = view.edtPassword.text.toString()

        if (userName.isEmpty()) {
            view.edtUserName.shake()
        }

        if (password.isEmpty()) {
            view.edtPassword.shake()
        }

        if (userName.isNotEmpty() && password.isNotEmpty()) {
            presenter.requestLogin(LoginRequest(username = userName, password = password))
        }
    }

    override fun resultLogin(isLogin: Boolean, userModel: UserModel?) {
        if (isLogin) {
            userModel?.let {
                KBus.post(LoginSuccessEventBus(type, it.linkAvatar))
            }
            router.popController(this)
        } else {
            activity?.let { activity ->
                Toasty.error(activity, activity.getString(R.string.msg_error_login)).show()
            }
        }

        hideLoading()
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgLoadingLogin.show()
        }
    }

    override fun hideLoading() {
        view?.let { view ->
            view.vgLoadingLogin.hide()
        }
    }

    override fun onDestroyView(view: View) {
        view.hideKeyboard()
        view.context.hideKeyboard(view)
        presenter.detachView()
        super.onDestroyView(view)
    }
}