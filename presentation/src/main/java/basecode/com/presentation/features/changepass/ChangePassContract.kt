package basecode.com.presentation.features.changepass

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class ChangePassContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun changePassSuccess()
        fun changePassFail()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun changePass(oldPass: String, newPass: String)
    }
}