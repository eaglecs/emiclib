package basecode.com.presentation.features.bookborrow

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.GetLoanHoldingCurrentUseCase
import basecode.com.domain.features.GetLoanHoldingHistoryUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.usecase.base.ResultListener
import basecode.com.presentation.features.books.BookVewModel

class BookBorrowPresenter(private val getLoanHoldingHistoryUseCase: GetLoanHoldingHistoryUseCase,
                          private val getLoanHoldingCurrentUseCase: GetLoanHoldingCurrentUseCase) : BookBorrowContract.Presenter() {
    override fun getListBook(isRefresh: Boolean, isBorrowing: Boolean) {
        view?.let { view ->
            view.showLoading()
            if (isBorrowing) {
                getLoanHoldingCurrentUseCase.cancel()
                getLoanHoldingCurrentUseCase.executeAsync(object : ResultListener<List<NewNewsResponse>, ErrorResponse> {
                    override fun success(data: List<NewNewsResponse>) {
                        val lstResult = mutableListOf<BookVewModel>()
                        data.forEach { newbook ->
                            val bookVewModel = BookVewModel(id = newbook.id.valueOrZero(), name = newbook.title.valueOrEmpty(),
                                    photo = newbook.coverPicture.valueOrEmpty(), author = newbook.author.valueOrEmpty(),
                                    publishedYear = newbook.publishedYear.valueOrEmpty())
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
                getLoanHoldingHistoryUseCase.executeAsync(object : ResultListener<List<NewNewsResponse>, ErrorResponse> {
                    override fun success(data: List<NewNewsResponse>) {
                        val lstResult = mutableListOf<BookVewModel>()
                        data.forEach { newbook ->
                            val bookVewModel = BookVewModel(id = newbook.id.valueOrZero(), name = newbook.title.valueOrEmpty(),
                                    photo = newbook.coverPicture.valueOrEmpty(), author = newbook.author.valueOrEmpty(),
                                    publishedYear = newbook.publishedYear.valueOrEmpty())
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