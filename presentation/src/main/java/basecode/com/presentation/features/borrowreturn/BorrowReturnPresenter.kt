package basecode.com.presentation.features.borrowreturn

import basecode.com.domain.extention.number.valueOrDefault
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.*
import basecode.com.domain.model.network.response.BooksDetailResponse
import basecode.com.domain.model.network.response.BorrowReturnBookResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.GetPatronOnLoanCopiesResponse
import basecode.com.domain.usecase.base.ResultListener
import basecode.com.presentation.features.borrowreturn.mapper.BookBorrowViewModelMapper
import basecode.com.presentation.features.borrowreturn.model.BookBorrowNewViewModel

class BorrowReturnPresenter(
    private val returnBookUseCase: ReturnBookUseCase,
    private val getCheckInCurrentLoanInforUseCase: GetCheckInCurrentLoanInforUseCase,
    private val getPatronOnLoanCopiesUseCase: GetPatronOnLoanCopiesUseCase,
    private val getCheckOutCurrentLoanUseCase: GetCheckOutCurrentLoanUseCase,
    private val borrowBookUseCase: BorrowBookUseCase
) : BorrowReturnBookContract.Presenter() {
    override fun borrowBook(copyNumber: String) {
        view?.let { view ->
            view.showLoading()
            borrowBookUseCase.cancel()
            borrowBookUseCase.executeAsync(object :
                ResultListener<BorrowReturnBookResponse, ErrorResponse> {
                override fun success(data: BorrowReturnBookResponse) {
                    if (data.isNotEmpty()) {
                        val errorCode = data.first().intError.valueOrDefault(-1)
                        if (errorCode == 0) {
                            view.borrowBookSuccess()
                            val transactionIds = data.first().strTransIDs.valueOrEmpty()
                            if (transactionIds.isNotEmpty()) {
                                getCheckOutCurrentLoan(
                                    view = view,
                                    transactionIds = transactionIds
                                )
                            } else {
                                view.hideLoading()
                            }
                        } else {
                            view.showErrorBorrowBook(errorCode = errorCode)
                            view.hideLoading()
                        }

                    }
                }

                override fun fail(error: ErrorResponse) {
                    view.showErrorBorrowBook(errorCode = -1)
                    view.hideLoading()
                }

                override fun done() {

                }

            }, copyNumber)
        }
    }

    private fun getCheckOutCurrentLoan(
        transactionIds: String,
        view: BorrowReturnBookContract.View
    ) {
        getCheckOutCurrentLoanUseCase.cancel()
        getCheckOutCurrentLoanUseCase.executeAsync(object :
            ResultListener<BooksDetailResponse, ErrorResponse> {
            override fun success(data: BooksDetailResponse) {
                if (data.isNotEmpty()) {
                    val book = data.first()
                    view.addBookBorrow(
                        book = BookBorrowNewViewModel(
                            code = book.copyNumber.valueOrEmpty(),
                            bookName = book.title.valueOrEmpty(),
                            borrowDate = book.checkOutDate.valueOrEmpty(),
                            returnDate = book.checkInDate.valueOrEmpty()
                        )
                    )
                }
            }

            override fun fail(error: ErrorResponse) {
            }

            override fun done() {
                view.hideLoading()
            }

        }, transactionIds)
    }

    override fun returnBook(copyNumber: String) {
        view?.let { view ->
            view.showLoading()
            returnBookUseCase.cancel()
            returnBookUseCase.executeAsync(object :
                ResultListener<BorrowReturnBookResponse, ErrorResponse> {
                override fun success(data: BorrowReturnBookResponse) {
                    val errorCode = data.first().intError.valueOrDefault(-1)
                    if (errorCode == 0) {
                        view.returnBookSuccess()
                        val transactionIds = data.first().strTransIDs.valueOrEmpty()
                        if (transactionIds.isNotEmpty()) {
                            getCheckInCurrentLoan(
                                view = view,
                                transactionIds = transactionIds
                            )
                        } else {
                            view.hideLoading()
                        }
                    } else {
                        view.showErrorReturnBook(errorCode = errorCode)
                        view.hideLoading()
                    }
                }

                override fun fail(error: ErrorResponse) {
                    view.showErrorReturnBook(errorCode = -1)
                    view.hideLoading()
                }

                override fun done() {

                }

            }, copyNumber)
        }
    }

    private fun getCheckInCurrentLoan(transactionIds: String, view: BorrowReturnBookContract.View) {
        getCheckInCurrentLoanInforUseCase.cancel()
        getCheckInCurrentLoanInforUseCase.executeAsync(object :
            ResultListener<BooksDetailResponse, ErrorResponse> {
            override fun success(data: BooksDetailResponse) {
                if (data.isNotEmpty()) {
                    val book = data.first()
                    view.deleteBookBorrow(
                        book = BookBorrowNewViewModel(
                            code = book.copyNumber.valueOrEmpty(),
                            bookName = book.title.valueOrEmpty(),
                            borrowDate = book.checkOutDate.valueOrEmpty(),
                            returnDate = book.checkInDate.valueOrEmpty()
                        )
                    )
                }
            }

            override fun fail(error: ErrorResponse) {

            }

            override fun done() {
                view.hideLoading()
            }

        }, transactionIds)
    }

    override fun getPatronOnLoanCopies() {
        view?.let { view ->
            view.showLoading()
            getPatronOnLoanCopiesUseCase.cancel()
            getPatronOnLoanCopiesUseCase.executeAsync(object :
                ResultListener<GetPatronOnLoanCopiesResponse, ErrorResponse> {
                override fun success(data: GetPatronOnLoanCopiesResponse) {
                    val lstBook = BookBorrowViewModelMapper().map(data)
                    view.showBookBorrow(lstBook = lstBook)
                }

                override fun fail(error: ErrorResponse) {
                    view.showErrorGetBooksBorrow()
                    view.hideLoading()
                }

                override fun done() {

                }

            })
        }
    }

    override fun onDetachView() {
        returnBookUseCase.cancel()
        getCheckInCurrentLoanInforUseCase.cancel()
        getPatronOnLoanCopiesUseCase.cancel()
        getCheckOutCurrentLoanUseCase.cancel()
        borrowBookUseCase.cancel()
        super.onDetachView()
    }
}