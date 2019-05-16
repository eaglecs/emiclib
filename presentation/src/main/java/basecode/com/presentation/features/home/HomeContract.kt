package basecode.com.presentation.features.home

import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class HomeContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListNewEBookSuccess()
        fun showErrorGetListNewEbook()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListNewBook(newEBookRequest: NewEBookRequest)
    }
}