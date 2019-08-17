package basecode.com.presentation.features.searchbook

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading
import basecode.com.presentation.features.books.BookViewModel

class SearchBookContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun searchBookSuccess(lstBookSearch: MutableList<BookViewModel>)
        fun searchBookFail()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun searchBookAdvance(docType: Int, searchText: String, title: String, author: String, language: String)
        abstract fun searchBook(docType: Int, searchText: String)
    }
}