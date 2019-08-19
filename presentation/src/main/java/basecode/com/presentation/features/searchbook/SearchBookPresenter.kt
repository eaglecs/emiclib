package basecode.com.presentation.features.searchbook

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.FindAdvanceBookUseCase
import basecode.com.domain.features.FindBookUseCase
import basecode.com.domain.model.network.response.BookResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.usecase.base.ResultListener
import basecode.com.presentation.features.books.BookViewModel

class SearchBookPresenter(private val searchBookUseCase: FindBookUseCase, private val findAdvanceBookUseCase: FindAdvanceBookUseCase) : SearchBookContract.Presenter() {
    override fun searchBookAdvance(docType: Int, searchText: String, title: String, author: String, language: String) {
        view?.let { view ->
            view.showLoading()
            val input = FindAdvanceBookUseCase.Input(docType = docType, author = author, searchText = searchText, title = title, language = language)
            findAdvanceBookUseCase.cancel()
            findAdvanceBookUseCase.executeAsync(object : ResultListener<List<BookResponse>, ErrorResponse> {
                override fun success(data: List<BookResponse>) {
                    val lstBookSearch = mutableListOf<BookViewModel>()
                    data.forEach { book ->
                        val bookVewModel = BookViewModel(id = book.id.valueOrZero(), author = book.author.valueOrEmpty(), name = book.title.valueOrEmpty(),
                                photo = book.coverPicture.valueOrEmpty(), publishedYear = book.publishedYear.valueOrEmpty(), publisher = book.publisher.valueOrEmpty())
                        lstBookSearch.add(bookVewModel)
                    }
                    view.searchBookSuccess(lstBookSearch)
                }

                override fun fail(error: ErrorResponse) {
                    view.hideLoading()
                    view.searchBookFail()
                }

                override fun done() {}

            }, input)

        }
    }

    override fun searchBook(docType: Int, searchText: String) {
        val input = FindBookUseCase.Input(docType = docType, searchText = searchText)
        view?.let { view ->
            view.showLoading()
            searchBookUseCase.cancel()
            searchBookUseCase.executeAsync(object : ResultListener<List<BookResponse>, ErrorResponse> {
                override fun success(data: List<BookResponse>) {
                    val lstBookSearch = mutableListOf<BookViewModel>()
                    data.forEach { book ->
                        val bookVewModel = BookViewModel(id = book.id.valueOrZero(), author = book.author.valueOrEmpty(), name = book.title.valueOrEmpty(),
                                photo = book.coverPicture.valueOrEmpty(), publishedYear = book.publishedYear.valueOrEmpty(), publisher = book.publisher.valueOrEmpty())
                        lstBookSearch.add(bookVewModel)
                    }
                    view.searchBookSuccess(lstBookSearch)
                }

                override fun fail(error: ErrorResponse) {
                    view.hideLoading()
                    view.searchBookFail()
                }

                override fun done() {}

            }, input)

        }
    }

    override fun onDetachView() {
        searchBookUseCase.cancel()
        findAdvanceBookUseCase.cancel()
        super.onDetachView()
    }
}