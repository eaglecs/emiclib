package basecode.com.ui.features.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.presentation.features.login.LoginContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.extension.view.hideKeyboard
import basecode.com.ui.extension.view.shake
import basecode.com.ui.util.DoubleTouchPrevent
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.screen_login.view.*
import org.koin.standalone.inject

class LoginViewController : ViewController(bundle = null), LoginContract.View {

    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private val presenter: LoginContract.Presenter by inject()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_login, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        onHandleView(view)
    }

    private fun initView(view: View) {

    }

    private fun onHandleView(view: View) {
        view.tvCloseLogin.setOnClickListener {
            if (doubleTouchPrevent.check("ivCloseLogin")) {
                router.popController(this)
            }
        }
        view.btnLogin.setOnClickListener {
            if (doubleTouchPrevent.check("btnLogin")) {
                loginLib(view)
            }
        }
        view.tvForgotPass.setOnClickListener {
            if (doubleTouchPrevent.check("tvForgotPass")) {

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

    override fun resultLogin(isLogin: Boolean) {
        if (isLogin) {
            KBus.post(LoginSuccessEventBus())
            router.popController(this)
        } else {
            activity?.let { activity ->
                Toasty.error(activity, activity.getString(R.string.msg_error_login)).show()
            }
        }
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
        presenter.detachView()
        super.onDestroyView(view)
    }
}