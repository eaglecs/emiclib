package basecode.com.presentation.features.login

import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.domain.model.network.response.UserModel
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class LoginContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun resultLogin(isLogin: Boolean, userModel: UserModel? = null)
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun requestLogin(loginRequest: LoginRequest)
    }
}