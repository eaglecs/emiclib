package basecode.com.presentation.features.books

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.GetItemInCollectionRecomandUseCase
import basecode.com.domain.features.GetListNewBookUseCase
import basecode.com.domain.features.GetListNewEbookUseCase
import basecode.com.domain.model.network.BookType
import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.BookResponse
import basecode.com.domain.model.network.response.NewEBookResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.usecase.base.ResultListener

class BooksPresenter(private val getListNewEbookUseCase: GetListNewEbookUseCase,
                     private val getListNewBookUseCase: GetListNewBookUseCase,
                     private val getItemInCollectionRecomandUseCase: GetItemInCollectionRecomandUseCase) : BooksContract.Presenter() {
    private val numItemLoadMore = 10
    private val pageSize = 10
    private var pageIndexCurrent = 1
    private var isLoadMoreNewBook = true
    private var isLoadMoreNewEBook = true
    private var isLoadMoreBookCollection = true

    override fun isShowLoadMore(bookType: Int): Boolean {
        return when (bookType) {
            BookType.BOOK.value -> {
                isLoadMoreNewBook
            }
            BookType.E_BOOK.value -> {
                isLoadMoreNewEBook
            }
            else -> {
                isLoadMoreBookCollection
            }
        }
    }

    override fun getItemInCollectionRecomand(isRefresh: Boolean, collectionId: Int) {
        view?.let { view ->
            view.showLoading()
            if (isRefresh) {
                pageIndexCurrent = 1
                isLoadMoreBookCollection = true
            }
            val input = GetItemInCollectionRecomandUseCase.Input(collectionId = collectionId,
                    pageIndex = pageIndexCurrent, pageSize = pageSize)
            getItemInCollectionRecomandUseCase.cancel()

            getItemInCollectionRecomandUseCase.executeAsync(object : ResultListener<List<NewNewsResponse>, ErrorResponse> {
                override fun success(data: List<NewNewsResponse>) {
                    if (data.size < pageSize) {
                        isLoadMoreBookCollection = false
                    } else {
                        pageIndexCurrent += 1
                    }
                    val lstResult = mutableListOf<BookViewModel>()
                    data.forEach { newbook ->
                        val bookVewModel = BookViewModel(id = newbook.id.valueOrZero(), name = newbook.title.valueOrEmpty(),
                                photo = newbook.coverPicture.valueOrEmpty(), author = newbook.author.valueOrEmpty(),
                                publishedYear = newbook.publishedYear.valueOrEmpty(), publisher = newbook.publisher.valueOrEmpty())
                        lstResult.add(bookVewModel)
                    }
                    view.getListNewBookSuccess(lstResult, isRefresh)
                }

                override fun fail(error: ErrorResponse) {
                    view.hideLoading()
                    view.showErrorGetListNewBook()
                }

                override fun done() {
                    view.hideLoading()
                }

            }, input)

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
                    val lstResult = mutableListOf<BookViewModel>()
                    data.forEach { newbook ->
                        val bookVewModel = BookViewModel(id = newbook.id.valueOrZero(), name = newbook.title.valueOrEmpty(), photo = newbook.coverPicture.valueOrEmpty(),
                                author = newbook.author.valueOrEmpty(), publishedYear = newbook.publishedYear.valueOrEmpty(), publisher = newbook.publisher.valueOrEmpty())
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
                    val lstResult = mutableListOf<BookViewModel>()
                    data.forEach { newbook ->
                        val bookVewModel = BookViewModel(id = newbook.id.valueOrZero(), name = newbook.title.valueOrEmpty(),
                                photo = newbook.coverPicture.valueOrEmpty(), author = newbook.author.valueOrEmpty(),
                                publishedYear = newbook.publishedYear.valueOrEmpty(), publisher = newbook.publisher.valueOrEmpty())
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
        getItemInCollectionRecomandUseCase.cancel()
        super.onDetachView()
    }
}