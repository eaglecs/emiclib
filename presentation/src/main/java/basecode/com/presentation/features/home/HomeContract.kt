package basecode.com.presentation.features.home

import basecode.com.domain.model.network.response.InfoHomeResponse
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class HomeContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListNewEBookSuccess(data: InfoHomeResponse)
        fun showErrorGetListNewEbook()
        fun handleAfterCheckLogin(isLogin: Boolean)
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListNewBook()
    }
}