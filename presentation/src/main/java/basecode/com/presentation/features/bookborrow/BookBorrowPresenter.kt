package basecode.com.presentation.features.bookborrow

import basecode.com.domain.extention.formatTime
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.GetLoanHoldingCurrentUseCase
import basecode.com.domain.features.GetLoanHoldingHistoryUseCase
import basecode.com.domain.features.GetLoanHoldingRenewBookUseCase
import basecode.com.domain.model.network.response.BookBorrowResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.usecase.base.ResultListener
import basecode.com.domain.util.DateTimeFormat
import basecode.com.presentation.features.books.BookBorrowViewModel

class BookBorrowPresenter(private val getLoanHoldingHistoryUseCase: GetLoanHoldingHistoryUseCase,
                          private val loanRenewUseCase: GetLoanHoldingRenewBookUseCase,
                          private val getLoanHoldingCurrentUseCase: GetLoanHoldingCurrentUseCase) : BookBorrowContract.Presenter() {
    override fun renew(coppynumber: String) {
        view?.let { view ->
            view.showLoading()
            loanRenewUseCase.cancel()
            loanRenewUseCase.executeAsync(object : ResultListener<Any, ErrorResponse> {
                override fun success(data: Any) {
                    view.renewSuccess()
                }

                override fun fail(error: ErrorResponse) {
                    view.renewSuccess()
                }

                override fun done() {
                    view.hideLoading()
                }

            }, coppynumber)
        }
    }

    override fun getListBook(isRefresh: Boolean, isBorrowing: Boolean) {
        view?.let { view ->
            view.showLoading()
            if (isBorrowing) {
                getLoanHoldingCurrentUseCase.cancel()
                getLoanHoldingCurrentUseCase.executeAsync(object : ResultListener<List<BookBorrowResponse>, ErrorResponse> {
                    override fun success(data: List<BookBorrowResponse>) {
                        val lstResult = mutableListOf<BookBorrowViewModel>()
                        data.forEach { newbook ->
                            var checkOutDate = newbook.checkOutDate.valueOrEmpty()
                            if (checkOutDate.isNotEmpty()) {
                                checkOutDate = checkOutDate.formatTime(DateTimeFormat.YY_MM_DD_T_HH_MM_SS, DateTimeFormat.DDMMYYFORMAT)
                            }

                            var dueDate = newbook.dueDate.valueOrEmpty()
                            if (dueDate.isNotEmpty()) {
                                dueDate = dueDate.formatTime(DateTimeFormat.YY_MM_DD_T_HH_MM_SS, DateTimeFormat.DDMMYYFORMAT)
                            }
                            var afterRenewDate = newbook.afterRenewDate.valueOrEmpty()
                            if (afterRenewDate.isNotEmpty()) {
                                afterRenewDate = afterRenewDate.formatTime(DateTimeFormat.YY_MM_DD_T_HH_MM_SS, DateTimeFormat.DDMMYYFORMAT)
                            }
                            val bookVewModel = BookBorrowViewModel(title = newbook.title.valueOrEmpty(), coppyNumber = newbook.copyNumber.valueOrEmpty(),
                                    checkOutDate = checkOutDate, dueDate = dueDate, photo = newbook.imageCover.valueOrEmpty(), afterRenewDate = afterRenewDate)
                            lstResult.add(bookVewModel)
                        }
                        view.getListBookSuccess(lstResult)
                    }

                    override fun fail(error: ErrorResponse) {
                        view.getListBookFail()
                    }

                    override fun done() {
                        view.hideLoading()
                    }

                })
            } else {
                getLoanHoldingHistoryUseCase.cancel()
                getLoanHoldingHistoryUseCase.executeAsync(object : ResultListener<List<BookBorrowResponse>, ErrorResponse> {
                    override fun success(data: List<BookBorrowResponse>) {
                        val lstResult = mutableListOf<BookBorrowViewModel>()
                        data.forEach { newbook ->
                            var checkOutDate = newbook.checkOutDate.valueOrEmpty()
                            if (checkOutDate.isNotEmpty()) {
                                checkOutDate = checkOutDate.formatTime(DateTimeFormat.YY_MM_DD_T_HH_MM_SS, DateTimeFormat.DDMMYYFORMAT)
                            }

                            var dueDate = newbook.dueDate.valueOrEmpty()
                            if (dueDate.isNotEmpty()) {
                                dueDate = dueDate.formatTime(DateTimeFormat.YY_MM_DD_T_HH_MM_SS, DateTimeFormat.DDMMYYFORMAT)
                            }
                            val bookVewModel = BookBorrowViewModel(title = newbook.title.valueOrEmpty(), coppyNumber = newbook.copyNumber.valueOrEmpty(),
                                    checkOutDate = checkOutDate, dueDate = dueDate, photo = newbook.imageCover.valueOrEmpty(), afterRenewDate = "")
                            lstResult.add(bookVewModel)
                        }
                        view.getListBookSuccess(lstResult)
                    }

                    override fun fail(error: ErrorResponse) {
                        view.getListBookFail()
                    }

                    override fun done() {
                        view.hideLoading()
                    }

                })
            }

        }
    }

    override fun onDetachView() {
        getLoanHoldingCurrentUseCase.cancel()
        getLoanHoldingHistoryUseCase.cancel()
        loanRenewUseCase.cancel()
        super.onDetachView()
    }
}