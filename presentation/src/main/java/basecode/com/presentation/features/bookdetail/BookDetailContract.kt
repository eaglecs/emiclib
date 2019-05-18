package basecode.com.presentation.features.bookdetail

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading
import basecode.com.presentation.features.books.BookVewModel

class BookDetailContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListBookRelatedSuccess(data: List<BookVewModel>)
        fun showErrorGetListBookRelated()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListBookRelated(bookId: Int)
    }
}