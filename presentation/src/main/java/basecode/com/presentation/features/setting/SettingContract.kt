package basecode.com.presentation.features.setting

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class SettingContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun resultCheckLogin(isLogin: Boolean)
    }
    abstract class Presenter : PresenterMvp<View>() {
        abstract fun checkLogin()
    }
}