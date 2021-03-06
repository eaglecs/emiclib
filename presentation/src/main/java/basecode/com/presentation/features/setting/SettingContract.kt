package basecode.com.presentation.features.setting

import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class SettingContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun resultCheckLogin(isLogin: Boolean)
        fun getUserInfoSuccess(data: LoginRequest)
        fun setStatusNewMessage(isHasNewMessage: Boolean)
    }
    abstract class Presenter : PresenterMvp<View>() {
        abstract fun checkLogin(onActionDone: (() -> Unit)? = null)
        abstract fun getUserInfo()
        abstract fun checkNewMessage()
    }
}