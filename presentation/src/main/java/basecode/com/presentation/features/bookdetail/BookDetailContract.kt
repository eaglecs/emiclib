package basecode.com.presentation.features.bookdetail

import basecode.com.domain.model.dbflow.EBookModel
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading
import basecode.com.presentation.features.books.BookVewModel

class BookDetailContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListBookRelatedSuccess(data: List<BookVewModel>)
        fun showErrorGetListBookRelated()
        fun handleAfterCheckLogin(isLogin: Boolean)
        fun getBookInfoSuccess(path: String, title: String, author: String, publisher: String, publishYear: String, shortDescription: String)
        fun getBookInfoFail(msgError: String)
        fun reservationBookSuccess()
        fun reservationBookFail(errorCode: Int)
        fun saveEBookSuccess()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListBookRelated(bookId: Long)
        abstract fun getStatusLogin()
        abstract fun getBookInfo(bookId: Long)
        abstract fun reservationBook(bookId: Long)
        abstract fun saveBookDownload(eBookModel: EBookModel)
    }
}