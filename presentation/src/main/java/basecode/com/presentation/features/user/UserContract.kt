package basecode.com.presentation.features.user

import basecode.com.domain.model.network.response.InfoUserResponse
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class UserContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getUserInfoSuccess(infoUserResponse: InfoUserResponse)
        fun getUserInfoFail()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getUserInfo()
    }
}