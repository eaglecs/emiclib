package basecode.com.ui.features.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.LogoutSuccessEventBus
import basecode.com.domain.model.network.response.InfoUserResponse
import basecode.com.presentation.features.user.UserContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.util.DoubleTouchPrevent
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_user_info.view.*
import org.koin.standalone.inject

class UserViewController : ViewController(bundle = null), UserContract.View {

    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private val presenter: UserContract.Presenter by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_user_info, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        presenter.getUserInfo()
        handleOnClickView(view)
    }

    private fun handleOnClickView(view: View) {
        view.ivBackBookDetail.setOnClickListener {
            if (doubleTouchPrevent.check("ivBackBookDetail")) {
                router.popController(this)
            }
        }
        view.ivLogout.setOnClickListener {
            if (doubleTouchPrevent.check("ivLogout")) {
                presenter.logout()
            }
        }
    }

    override fun getUserInfoSuccess(infoUserResponse: InfoUserResponse) {
        view?.let { view ->
            view.tvUserName.text = infoUserResponse.patronName
            view.tvCodeCard.text = infoUserResponse.patronCode
            val time = "${infoUserResponse.validDate} - ${infoUserResponse.expiredDate}"
            view.tvTime.text = time
        }
        hideLoading()
    }

    override fun getUserInfoFail() {
        activity?.let { activity ->
            Toasty.error(activity, activity.resources.getString(R.string.msg_error_get_user_info))
        }
        hideLoading()
    }

    override fun logoutFail() {
        activity?.let { activity ->
            Toasty.error(activity, activity.resources.getString(R.string.msg_error_logout))
        }
    }

    override fun logoutSuccess() {
        KBus.post(LogoutSuccessEventBus())
        router.popController(this)
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgLoading.show()
        }
    }

    override fun hideLoading() {
        view?.let { view ->
            view.vgLoading.hide()
        }
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        presenter.detachView()
    }
}