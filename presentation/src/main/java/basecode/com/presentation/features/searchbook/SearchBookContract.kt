package basecode.com.presentation.features.searchbook

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading
import basecode.com.presentation.features.books.BookViewModel

class SearchBookContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun searchBookSuccess(lstBookSearch: MutableList<BookViewModel>, isRefresh: Boolean)
        fun searchBookFail()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun searchBookAdvance(
            isRefresh: Boolean,
            docType: Int,
            searchText: String,
            title: String,
            author: String,
            language: String,
            boothId: Long
        )
        abstract fun searchBook(isRefresh: Boolean, docType: Int, searchText: String, boothId: Long)
        abstract fun isShowLoadMore(): Boolean
    }
}