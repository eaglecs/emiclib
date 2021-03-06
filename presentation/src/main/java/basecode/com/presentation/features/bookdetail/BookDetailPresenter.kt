package basecode.com.presentation.features.bookdetail

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.*
import basecode.com.domain.model.dbflow.EBookModel
import basecode.com.domain.model.network.response.BookResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.InfoBookResponse
import basecode.com.domain.model.network.response.UserModel
import basecode.com.domain.usecase.base.ResultListener
import basecode.com.presentation.features.books.BookViewModel

class BookDetailPresenter(
    private val getListBookRelatedUseCase: GetListBookRelatedUseCase,
    private val getInfoUserUseCase: GetInfoUserUseCase,
    private val getInfoBookUseCase: GetInfoBookUseCase,
    private val reservationBookUseCase: ReservationBookUseCase,
    private val reserverBookUseCase: ReserverBookUseCase,
    private val saveBookUseCase: SaveBookUseCase
) : BookDetailContract.Presenter() {

    override fun saveBookDownload(eBookModel: EBookModel) {
        view?.let { view ->
            view.showLoading()
            saveBookUseCase.cancel()
            saveBookUseCase.executeAsync(object : ResultListener<Boolean, ErrorResponse> {
                override fun success(data: Boolean) {
                    view.saveEBookSuccess()
                }

                override fun fail(error: ErrorResponse) {
                }

                override fun done() {
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
                    if (data == 0) {
                        view.reservationBookSuccess()
                    } else {
                        view.reservationBookFail(data)
                    }
                }

                override fun fail(error: ErrorResponse) {
                    view.reservationBookFail(8)
                }

                override fun done() {
                    view.hideLoading()
                }

            }, bookId)
        }
    }

    override fun reserverBook(bookId: Long) {
        view?.let { view ->
            view.showLoading()
            reserverBookUseCase.cancel()
            reserverBookUseCase.executeAsync(object : ResultListener<Int, ErrorResponse> {
                override fun success(data: Int) {
                    if (data == 0) {
                        view.reserverBookSuccess()
                    } else {
                        view.reserverBookFail(data)
                    }
                }

                override fun fail(error: ErrorResponse) {
                    view.reserverBookFail(8)
                }

                override fun done() {
                    view.hideLoading()
                }

            }, bookId)
        }
    }

    override fun getListBookRelated(bookId: Long, docType: Int, boothId: Long) {
        view?.let { view ->
            view.showLoading()
            getListBookRelatedUseCase.cancel()
            getListBookRelatedUseCase.executeAsync(object :
                ResultListener<List<BookResponse>, ErrorResponse> {
                override fun success(data: List<BookResponse>) {
                    val lstResult = mutableListOf<BookViewModel>()
                    data.forEach { book ->
                        val bookVewModel = BookViewModel(
                            id = book.id.valueOrZero(),
                            photo = book.coverPicture.valueOrEmpty(),
                            name = book.title.valueOrEmpty(),
                            author = book.author.valueOrEmpty(),
                            publishedYear = book.publishedYear.valueOrEmpty(),
                            publisher = book.publisher.valueOrEmpty()
                        )
                        lstResult.add(bookVewModel)
                    }
                    view.getListBookRelatedSuccess(data = lstResult)
                }

                override fun fail(error: ErrorResponse) {
                    view.getListBookRelatedSuccess(data = mutableListOf())
//                    view.hideLoading()
//                    view.showErrorGetListBookRelated()
                }

                override fun done() {}

            }, GetListBookRelatedUseCase.Input(bookId = bookId, docType = docType))
        }
    }

    override fun getStatusLogin() {
        view?.let { view ->
            getInfoUserUseCase.cancel()
            getInfoUserUseCase.executeAsync(object : ResultListener<UserModel, ErrorResponse> {
                override fun success(data: UserModel) {
                    val isLogin = data.patronCode.isNotEmpty()
                    view.handleAfterCheckLogin(isLogin = isLogin, avatar = data.linkAvatar)
                }

                override fun fail(error: ErrorResponse) {
                    view.handleAfterCheckLogin(false, "")
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
            getInfoBookUseCase.executeAsync(object :
                ResultListener<InfoBookResponse, ErrorResponse> {
                override fun success(data: InfoBookResponse) {
                    val lstPathResult = mutableListOf<String>()
                    var copyNumberResult = ""
                    data.lstPath?.let { lstPath ->
                        lstPath.forEach { path ->
                            val pathBook = path.valueOrEmpty()
                            lstPathResult.add(pathBook)
                        }
                    }
                    var numFreeBookStr = ""
                    var numFreeBook = 0
                    data.lstCopyNumber?.let { copyNumbers ->
                        copyNumbers.forEach { copyNumber ->
                            copyNumber?.let {
                                copyNumberResult = if (copyNumberResult.isNotEmpty()) {
                                    "$copyNumberResult, $it"
                                } else {
                                    "$it"
                                }
                            }
                        }
                        data.lstFreeCopyNumber?.let { lstFreeCopyNumber ->
                            numFreeBook = lstFreeCopyNumber.size
                            numFreeBookStr = "$numFreeBook/${copyNumbers.size}"
                        }
                    }
                    val title = data.title.valueOrEmpty()
                    val author = data.author.valueOrEmpty()
                    val publisher = data.publisher.valueOrEmpty()
                    val publishYear = data.publishYear.valueOrEmpty()
                    val shortDescription = data.shortDescription.valueOrEmpty()
                    val linkShare = data.linkShare.valueOrEmpty()
                    val coverPicture = data.coverPicture.valueOrEmpty()
                    val info = data.info.valueOrEmpty()
                    view.getBookInfoSuccess(
                        lstPathResult = lstPathResult,
                        title = title,
                        author = author,
                        publisher = publisher,
                        publishYear = publishYear,
                        shortDescription = shortDescription,
                        copyNumberResult = copyNumberResult,
                        linkShare = linkShare,
                        infoBook = info,
                        numFreeBookStr = numFreeBookStr,
                        numFreeBook = numFreeBook,
                        coverPicture = coverPicture
                    )
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
        getInfoUserUseCase.cancel()
        getInfoBookUseCase.cancel()
        reserverBookUseCase.cancel()
        super.onDetachView()
    }
}