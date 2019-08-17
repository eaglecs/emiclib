package basecode.com.presentation.features.books

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class BooksContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListNewBookSuccess(data: List<BookViewModel>, refresh: Boolean)
        fun showErrorGetListNewBook()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListNewBook(isRefresh: Boolean)
        abstract fun getItemInCollectionRecomand(isRefresh: Boolean, collectionId: Int)
        abstract fun getListNewEBook(isRefresh: Boolean)
        abstract fun isShowLoadMore(bookType: Int): Boolean
    }
}