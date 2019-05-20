package basecode.com.presentation.features.bookdetail

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.GetListBookRelatedUseCase
import basecode.com.domain.features.GetUserTokenUseCase
import basecode.com.domain.model.network.response.BookResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.usecase.base.ResultListener
import basecode.com.presentation.features.books.BookVewModel

class BookDetailPresenter(private val getListBookRelatedUseCase: GetListBookRelatedUseCase,
                          private val getUserTokenUseCase: GetUserTokenUseCase) : BookDetailContract.Presenter() {
    override fun getListBookRelated(bookId: Int) {
        view?.let { view ->
            view.showLoading()
            getListBookRelatedUseCase.cancel()
            getListBookRelatedUseCase.executeAsync(object : ResultListener<List<BookResponse>, ErrorResponse> {
                override fun success(data: List<BookResponse>) {
                    val lstResult = mutableListOf<BookVewModel>()
                    data.forEach { book ->
                        val bookVewModel = BookVewModel(id = book.id.valueOrZero(), photo = book.coverPicture.valueOrEmpty(), name = book.title.valueOrEmpty(), author = book.author.valueOrEmpty())
                        lstResult.add(bookVewModel)
                    }
                    view.getListBookRelatedSuccess(data = lstResult)
                }

                override fun fail(error: ErrorResponse) {
                    view.hideLoading()
                    view.showErrorGetListBookRelated()
                }

                override fun done() {}

            }, bookId)
        }
    }

    override fun getStatusLogin() {
        view?.let { view ->
            getUserTokenUseCase.cancel()
            getUserTokenUseCase.executeAsync(object : ResultListener<String, ErrorResponse> {
                override fun success(data: String) {
                    view.handleAfterCheckLogin(true)
                }

                override fun fail(error: ErrorResponse) {
                    view.handleAfterCheckLogin(false)
                }

                override fun done() {
                    view.hideLoading()
                }

            })
        }
    }

    override fun onDetachView() {
        getListBookRelatedUseCase.cancel()
        getUserTokenUseCase.cancel()
        super.onDetachView()
    }
}