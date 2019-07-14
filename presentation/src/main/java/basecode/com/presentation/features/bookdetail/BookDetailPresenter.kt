package basecode.com.presentation.features.bookdetail

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.*
import basecode.com.domain.model.dbflow.EBookModel
import basecode.com.domain.model.network.response.BookResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.InfoBookResponse
import basecode.com.domain.usecase.base.ResultListener
import basecode.com.presentation.features.books.BookVewModel

class BookDetailPresenter(private val getListBookRelatedUseCase: GetListBookRelatedUseCase,
                          private val getUserTokenUseCase: GetUserTokenUseCase,
                          private val getInfoBookUseCase: GetInfoBookUseCase,
                          private val reservationBookUseCase: ReservationBookUseCase,
                          private val saveBookUseCase: SaveBookUseCase) : BookDetailContract.Presenter() {

    override fun saveBookDownload(eBookModel: EBookModel) {
        view?.let { view ->
            view.showLoading()
            saveBookUseCase.cancel()
            saveBookUseCase.executeAsync(object : ResultListener<Boolean, ErrorResponse>{
                override fun success(data: Boolean) {
                }

                override fun fail(error: ErrorResponse) {
                }

                override fun done() {
                    view.hideLoading()
                }

            }, eBookModel)
        }
    }
    override fun reservationBook(bookId: Long) {
        view?.let { view ->
            view.showLoading()
            reservationBookUseCase.cancel()
            reservationBookUseCase.executeAsync(object : ResultListener<Int, ErrorResponse> {
                override fun success(data: Int) {
                    if (data == 1) {
                        view.reservationBookSuccess()
                    } else {
                        view.reservationBookFail("")
                    }
                }

                override fun fail(error: ErrorResponse) {
                    view.reservationBookFail(error.msgError)
                }

                override fun done() {
                    view.hideLoading()
                }

            }, bookId)
        }
    }

    override fun getListBookRelated(bookId: Long) {
        view?.let { view ->
            view.showLoading()
            getListBookRelatedUseCase.cancel()
            getListBookRelatedUseCase.executeAsync(object : ResultListener<List<BookResponse>, ErrorResponse> {
                override fun success(data: List<BookResponse>) {
                    val lstResult = mutableListOf<BookVewModel>()
                    data.forEach { book ->
                        val bookVewModel = BookVewModel(id = book.id.valueOrZero(), photo = book.coverPicture.valueOrEmpty(), name = book.title.valueOrEmpty(),
                                author = book.author.valueOrEmpty(), publishedYear = book.publishedYear.valueOrEmpty())
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
                    val isLogin = data.isNotEmpty()
                    view.handleAfterCheckLogin(isLogin)
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

    override fun getBookInfo(bookId: Long) {
        view?.let { view ->
            view.showLoading()
            getInfoBookUseCase.cancel()
            getInfoBookUseCase.executeAsync(object : ResultListener<InfoBookResponse, ErrorResponse> {
                override fun success(data: InfoBookResponse) {
                    var path = ""
                    data.lstPath?.let { lstPath ->
                        if (lstPath.isNotEmpty()) {
                            path = lstPath.first().valueOrEmpty()
                        }
                    }
                    val title = data.title.valueOrEmpty()
                    val author = data.author.valueOrEmpty()
                    view.getBookInfoSuccess(path, title, author)
                }

                override fun fail(error: ErrorResponse) {
                    view.getBookInfoFail(error.msgError)
                }

                override fun done() {
                    view.hideLoading()
                }

            }, bookId)
        }
    }

    override fun onDetachView() {
        saveBookUseCase.cancel()
        getListBookRelatedUseCase.cancel()
        reservationBookUseCase.cancel()
        getUserTokenUseCase.cancel()
        getInfoBookUseCase.cancel()
        super.onDetachView()
    }
}