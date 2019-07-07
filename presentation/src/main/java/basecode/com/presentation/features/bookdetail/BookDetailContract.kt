package basecode.com.presentation.features.bookdetail

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading
import basecode.com.presentation.features.books.BookVewModel

class BookDetailContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListBookRelatedSuccess(data: List<BookVewModel>)
        fun showErrorGetListBookRelated()
        fun handleAfterCheckLogin(isLogin: Boolean)
        fun getBookInfoSuccess(path: String)
        fun getBookInfoFail(msgError: String)
        fun reservationBookSuccess()
        fun reservationBookFail(msgError: String)
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListBookRelated(bookId: Int)
        abstract fun getStatusLogin()
        abstract fun getBookInfo(bookId: Int)
        abstract fun reservationBook(bookId: Int)
    }
}