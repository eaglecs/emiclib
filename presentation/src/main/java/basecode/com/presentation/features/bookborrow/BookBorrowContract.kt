package basecode.com.presentation.features.bookborrow

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading
import basecode.com.presentation.features.books.BookBorrowViewModel
import basecode.com.presentation.features.books.BookViewModel

class BookBorrowContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListBookSuccess(lstBook: List<BookBorrowViewModel>)
        fun getListBookFail()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListBook(isRefresh: Boolean, isBorrowing: Boolean)
    }
}