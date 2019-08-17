package basecode.com.presentation.features.bookborrow

import basecode.com.domain.extention.formatTime
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.GetLoanHoldingCurrentUseCase
import basecode.com.domain.features.GetLoanHoldingHistoryUseCase
import basecode.com.domain.model.network.response.BookBorrowResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.usecase.base.ResultListener
import basecode.com.domain.util.DateTimeFormat
import basecode.com.presentation.features.books.BookBorrowViewModel
import basecode.com.presentation.features.books.BookViewModel

class BookBorrowPresenter(private val getLoanHoldingHistoryUseCase: GetLoanHoldingHistoryUseCase,
                          private val getLoanHoldingCurrentUseCase: GetLoanHoldingCurrentUseCase) : BookBorrowContract.Presenter() {
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
                            val bookVewModel = BookBorrowViewModel(title = newbook.title.valueOrEmpty(), photo = newbook.imageCover.valueOrEmpty(),
                                    checkOutDate = checkOutDate, dueDate = dueDate, coppyNumber = newbook.copyNumber.valueOrEmpty())
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
                            val bookVewModel = BookBorrowViewModel(title = newbook.title.valueOrEmpty(), photo = newbook.imageCover.valueOrEmpty(),
                                    checkOutDate = checkOutDate, dueDate = dueDate, coppyNumber = newbook.copyNumber.valueOrEmpty())
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
        super.onDetachView()
    }
}