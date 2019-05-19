package basecode.com.ui.features.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.util.DoubleTouchPrevent
import kotlinx.android.synthetic.main.screen_login.view.*
import org.koin.standalone.inject

class LoginViewController : ViewController(bundle = null) {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_login, container, false)
    }

    override fun initPostCreateView(view: View) {
        initView(view)
        onHandleView(view)
    }

    private fun initView(view: View) {

    }

    private fun onHandleView(view: View) {
        view.ivCloseLogin.setOnClickListener {
            if (doubleTouchPrevent.check("ivCloseLogin")) {
                router.popController(this)
            }
        }
        view.tvRegister.setOnClickListener {
            if (doubleTouchPrevent.check("tvRegister")) {

            }
        }
        view.btnLogin.setOnClickListener {
            if (doubleTouchPrevent.check("btnLogin")) {

            }
        }
        view.tvForgotPass.setOnClickListener {
            if (doubleTouchPrevent.check("tvForgotPass")) {

            }
        }


    }
}