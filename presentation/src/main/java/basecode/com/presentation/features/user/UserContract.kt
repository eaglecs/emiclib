package basecode.com.presentation.features.user

import basecode.com.domain.model.network.response.UserModel
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class UserContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getUserInfoSuccess(userModel: UserModel)
        fun getUserInfoFail()
        fun logoutSuccess()
        fun logoutFail()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getUserInfo()
        abstract fun logout()
    }
}