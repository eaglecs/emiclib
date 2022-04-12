package basecode.com.presentation.features.borrowreturn

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

interface BorrowReturnBookContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun showErrorBorrowBook(errorCode: Int)
        fun showErrorReturnBook(errorCode: Int)
        fun returnBookSuccess()
        fun borrowBookSuccess()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun returnBook(copyNumber: String)
        abstract fun borrowBook(copyNumber: String)
        abstract fun getPatronOnLoanCopies()
    }
}