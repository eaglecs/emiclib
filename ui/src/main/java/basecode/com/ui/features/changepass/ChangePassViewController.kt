package basecode.com.ui.features.changepass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.presentation.features.changepass.ChangePassContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.extension.view.hideKeyboard
import basecode.com.ui.util.DoubleTouchPrevent
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_change_pass.view.*
import org.koin.standalone.inject

class ChangePassViewController : ViewController(null), ChangePassContract.View {
    private val presenter: ChangePassContract.Presenter by inject()
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_change_pass, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        handleView(view)

    }

    private fun handleView(view: View) {
        view.ivBack.setOnClickListener {
            if (doubleTouchPrevent.check("ivBack")) {
                router.popController(this)
            }
        }

        view.btnChangePass.setOnClickListener {
            if (doubleTouchPrevent.check("btnChangePass")) {
                val newPass = view.edtNewPass.text.toString()
                val confirmPass = view.edtConfirmPass.text.toString()
                if (newPass == confirmPass) {
                    val oldPass = view.edtOldPass.text.toString()
                    presenter.changePass(oldPass, newPass)
                } else {
                    Toasty.warning(view.context, view.context.resources.getString(R.string.msg_warning_confirm_pass_fail)).show()
                }
            }
        }
    }

    override fun changePassSuccess() {
        view?.let { view ->
            Toasty.success(view.context, view.context.resources.getString(R.string.msg_change_pass_success)).show()
        }
        router.popController(this)
    }

    override fun changePassFail() {
        view?.let { view ->
            Toasty.error(view.context, view.context.resources.getString(R.string.msg_change_pass_fail)).show()
        }
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgLoading.show()
        }
    }

    override fun hideLoading() {
        view?.let { view ->
            view.hideKeyboard()
            view.vgLoading.hide()
        }
    }

    override fun onDestroyView(view: View) {
        view.hideKeyboard()
        presenter.detachView()
        super.onDestroyView(view)
    }
}