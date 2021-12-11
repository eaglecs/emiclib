package basecode.com.presentation.features.searchbook

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.FindAdvanceBookUseCase
import basecode.com.domain.features.FindBookUseCase
import basecode.com.domain.model.network.response.BookResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.usecase.base.ResultListener
import basecode.com.presentation.features.books.BookViewModel

class SearchBookPresenter(
    private val searchBookUseCase: FindBookUseCase,
    private val findAdvanceBookUseCase: FindAdvanceBookUseCase
) : SearchBookContract.Presenter() {
    private var isAllowLoadMore = true
    private var pageIndex: Int = 1
    private val pageSize: Int = 20

    override fun searchBookAdvance(
        isRefresh: Boolean,
        docType: Int,
        searchText: String,
        title: String,
        author: String,
        language: String
    ) {
        view?.let { view ->
            if (isRefresh) {
                pageIndex = 1
                view.showLoading()
                isAllowLoadMore = true
            }
            val input = FindAdvanceBookUseCase.Input(
                docType = docType,
                author = author,
                searchText = searchText,
                title = title,
                language = language,
                pageIndex = pageIndex,
                pageSize = pageSize
            )
            findAdvanceBookUseCase.cancel()
            findAdvanceBookUseCase.executeAsync(object :
                ResultListener<List<BookResponse>, ErrorResponse> {
                override fun success(data: List<BookResponse>) {
                    isAllowLoadMore = data.size == pageSize
                    if (isAllowLoadMore) {
                        pageIndex += 1
                    }
                    val lstBookSearch = mutableListOf<BookViewModel>()
                    data.forEach { book ->
                        val bookVewModel = BookViewModel(
                            id = book.id.valueOrZero(),
                            author = book.author.valueOrEmpty(),
                            name = book.title.valueOrEmpty(),
                            photo = book.coverPicture.valueOrEmpty(),
                            publishedYear = book.publishedYear.valueOrEmpty(),
                            publisher = book.publisher.valueOrEmpty()
                        )
                        lstBookSearch.add(bookVewModel)
                    }
                    view.searchBookSuccess(lstBookSearch, isRefresh)
                }

                override fun fail(error: ErrorResponse) {
                    view.hideLoading()
                    view.searchBookFail()
                }

                override fun done() {}

            }, input)

        }
    }

    override fun searchBook(isRefresh: Boolean, docType: Int, searchText: String) {
        view?.let { view ->
            if (isRefresh) {
                pageIndex = 1
                view.showLoading()
                isAllowLoadMore = true
            }
            val input = FindBookUseCase.Input(
                docType = docType,
                searchText = searchText,
                pageIndex = pageIndex,
                pageSize = pageSize
            )
            searchBookUseCase.cancel()
            searchBookUseCase.executeAsync(object :
                ResultListener<List<BookResponse>, ErrorResponse> {
                override fun success(data: List<BookResponse>) {
                    isAllowLoadMore = data.size == pageSize
                    if (isAllowLoadMore) {
                        pageIndex += 1
                    }
                    val lstBookSearch = mutableListOf<BookViewModel>()
                    data.forEach { book ->
                        val bookVewModel = BookViewModel(
                            id = book.id.valueOrZero(),
                            author = book.author.valueOrEmpty(),
                            name = book.title.valueOrEmpty(),
                            photo = book.coverPicture.valueOrEmpty(),
                            publishedYear = book.publishedYear.valueOrEmpty(),
                            publisher = book.publisher.valueOrEmpty()
                        )
                        lstBookSearch.add(bookVewModel)
                    }
                    view.searchBookSuccess(isRefresh = isRefresh, lstBookSearch = lstBookSearch)
                }

                override fun fail(error: ErrorResponse) {
                    view.hideLoading()
                    view.searchBookFail()
                }

                override fun done() {}

            }, input)

        }
    }

    override fun isShowLoadMore(): Boolean = isAllowLoadMore

    override fun onDetachView() {
        searchBookUseCase.cancel()
        findAdvanceBookUseCase.cancel()
        super.onDetachView()
    }
}