package basecode.com.presentation.features.new

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

interface MainContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun handleAfterCheckLogin(isLogin: Boolean, avatar: String)

    }
    abstract class Presenter : PresenterMvp<View>() {
        abstract fun checkLogin()
    }
}