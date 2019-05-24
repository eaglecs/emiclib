package basecode.com.presentation.features.searchbook

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading
import basecode.com.presentation.features.books.BookVewModel

class SearchBookContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun searchBookSuccess(lstBookSearch: MutableList<BookVewModel>)
        fun searchBookFail()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun searchBookAdvance(docType: Int, searchText: String, title: Int, author: Int, language: Int)
        abstract fun searchBook(docType: Int, searchText: String)
    }
}