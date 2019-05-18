package basecode.com.presentation.features.books

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.GetListNewBookUseCase
import basecode.com.domain.features.GetListNewEbookUseCase
import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.BookResponse
import basecode.com.domain.model.network.response.NewEBookResponse
import basecode.com.domain.usecase.base.ResultListener

class BooksPresenter(private val getListNewEbookUseCase: GetListNewEbookUseCase,
                     private val getListNewBookUseCase: GetListNewBookUseCase) : BooksContract.Presenter() {
    private val numItemLoadMore = 10
    private val pageSize = 10
    private var pageIndexCurrent = 1
    private var isLoadMoreNewBook = true
    private var isLoadMoreNewEBook = true

    override fun isShowLoadMore(isEBook: Boolean): Boolean {
        return if (isEBook) {
            isLoadMoreNewEBook
        } else {
            isLoadMoreNewBook
        }
    }
    override fun getListNewBook(isRefresh: Boolean) {
        view?.let { view ->
            view.showLoading()
            if (isRefresh) {
                pageIndexCurrent = 1
                isLoadMoreNewBook = true
            }

            getListNewBookUseCase.cancel()
            getListNewBookUseCase.executeAsync(object : ResultListener<List<BookResponse>, ErrorResponse> {
                override fun success(data: List<BookResponse>) {
                    if (data.size < pageSize) {
                        isLoadMoreNewBook = false
                    } else {
                        pageIndexCurrent += 1
                    }
                    val lstResult = mutableListOf<BookVewModel>()
                    data.forEach { newbook ->
                        val bookVewModel = BookVewModel(id = newbook.id.valueOrZero(), name = newbook.title.valueOrEmpty(), photo = newbook.coverPicture.valueOrEmpty(),
                                author = newbook.author.valueOrEmpty())
                        lstResult.add(bookVewModel)
                    }
                    view.getListNewBookSuccess(lstResult, isRefresh)
                }

                override fun fail(error: ErrorResponse) {
                    view.hideLoading()
                    view.showErrorGetListNewBook()
                }

                override fun done() {}
            }, NewEBookRequest(numberItem = numItemLoadMore, pageIndex = pageIndexCurrent, pageSize = pageSize))

        }

    }

    override fun getListNewEBook(isRefresh: Boolean) {
        view?.let { view ->
            view.showLoading()
            if (isRefresh) {
                pageIndexCurrent = 1
                isLoadMoreNewEBook = true
            }

            getListNewEbookUseCase.cancel()
            getListNewEbookUseCase.executeAsync(object : ResultListener<List<NewEBookResponse>, ErrorResponse> {
                override fun success(data: List<NewEBookResponse>) {
                    if (data.size < pageSize) {
                        isLoadMoreNewEBook = false
                    } else {
                        pageIndexCurrent += 1
                    }
                    val lstResult = mutableListOf<BookVewModel>()
                    data.forEach { newbook ->
                        val bookVewModel = BookVewModel(id = newbook.id.valueOrZero(), name = newbook.title.valueOrEmpty(),
                                photo = newbook.coverPicture.valueOrEmpty(), author = newbook.author.valueOrEmpty())
                        lstResult.add(bookVewModel)
                    }
                    view.getListNewBookSuccess(lstResult, isRefresh)
                }

                override fun fail(error: ErrorResponse) {
                    view.hideLoading()
                    view.showErrorGetListNewBook()
                }

                override fun done() {}
            }, NewEBookRequest(numberItem = numItemLoadMore, pageIndex = pageIndexCurrent, pageSize = pageSize))

        }
    }

    override fun onDetachView() {
        getListNewEbookUseCase.cancel()
        getListNewBookUseCase.cancel()
        super.onDetachView()
    }
}