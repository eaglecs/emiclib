package basecode.com.presentation.features.books

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class BooksContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListNewBookSuccess(data: List<BookVewModel>, refresh: Boolean)
        fun showErrorGetListNewBook()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListNewBook(isRefresh: Boolean)
        abstract fun getListNewEBook(isRefresh: Boolean)
        abstract fun isShowLoadMore(isEBook: Boolean): Boolean
    }
}