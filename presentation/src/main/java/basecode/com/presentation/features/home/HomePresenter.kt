package basecode.com.presentation.features.home

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.*
import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.domain.model.network.response.*
import basecode.com.domain.usecase.base.ResultListener

class HomePresenter(
    private val getListNewBookUseCase: GetListNewBookUseCase,
    private val getListNewEbookUseCase: GetListNewEbookUseCase,
    private val getBooksRecommendUseCase: GetBooksRecommendUseCase,
    private val getLoginRequestUseCase: GetLoginRequestUseCase,
    private val loginUseCase: LoginUseCase,
    private val saveInfoLoginUseCase: SaveInfoLoginUseCase,
    private val getInfoUserUseCase: GetInfoUserUseCase,
    private val getUserTokenUseCase: GetUserTokenUseCase
) : HomeContract.Presenter() {
    override fun getListNewBook(isTDN: Boolean) {
        view?.let { view ->
            view.showLoading()
            getLoginRequestUseCase.cancel()
            getLoginRequestUseCase.executeAsync(object :
                ResultListener<LoginRequest, ErrorResponse> {
                override fun success(data: LoginRequest) {
                    requestLogin(data)
                }

                override fun fail(error: ErrorResponse) {
                }

                override fun done() {
                }

            })
            getUserTokenUseCase.cancel()
            getUserTokenUseCase.executeAsync(object : ResultListener<String, ErrorResponse> {
                override fun success(data: String) {
                    if (data.isNotEmpty()) {
                        getInfoUserUseCase.cancel()
                        getInfoUserUseCase.executeAsync(object :
                            ResultListener<UserModel, ErrorResponse> {
                            override fun success(userModel: UserModel) {
                                view.handleAfterCheckLogin(data.isNotEmpty(), userModel.linkAvatar)

                            }

                            override fun fail(error: ErrorResponse) {
                                view.handleAfterCheckLogin(false)
                            }

                            override fun done() {
                            }

                        })
                    } else {
                        view.handleAfterCheckLogin(false)
                    }
                }

                override fun fail(error: ErrorResponse) {
                    view.handleAfterCheckLogin(false)
                }

                override fun done() {
                    getInfoHome()
                }
            })

        }
    }

    private var totalRequest = 0
    private val lstNewBook: MutableList<NewBooksModel> = mutableListOf()
    private val lstNewEBook: MutableList<NewEBookModel> = mutableListOf()
    private val lstCollectionRecommend: MutableList<CollectionRecommendModel> = mutableListOf()

    private fun getInfoHome() {
        totalRequest = 0
        lstNewBook.clear()
        lstNewEBook.clear()
        lstCollectionRecommend.clear()
        getListNewBookUseCase.cancel()
        getListNewBookUseCase.executeAsync(object : ResultListener<List<BookResponse>, ErrorResponse>{
            override fun success(data: List<BookResponse>) {
                data.let { listNewBooks ->
                    listNewBooks.forEach { newBook ->
                        val newBooksModel = NewBooksModel(id = newBook.id.valueOrZero(), title = newBook.title.valueOrEmpty(),
                            coverPicture = newBook.coverPicture.valueOrEmpty(), author = newBook.author.valueOrEmpty())
                        lstNewBook.add(newBooksModel)
                    }
                }
            }

            override fun fail(error: ErrorResponse) {
            }

            override fun done() {
                handleShowData()
            }

        }, NewEBookRequest(numberItem = 10, pageIndex = 1, pageSize = 10))

        getListNewEbookUseCase.cancel()
        getListNewEbookUseCase.executeAsync(object : ResultListener<List<NewEBookResponse>, ErrorResponse>{
            override fun success(data: List<NewEBookResponse>) {
                data.let { listNewEBooks ->
                    listNewEBooks.forEach { eBook ->
                        val newEBooksModel = NewEBookModel(id = eBook.id.valueOrZero(), title = eBook.title.valueOrEmpty(),
                            coverPicture = eBook.coverPicture.valueOrEmpty(), author = eBook.author.valueOrEmpty())
                        lstNewEBook.add(newEBooksModel)
                    }
                }
            }

            override fun fail(error: ErrorResponse) {
            }

            override fun done() {
                handleShowData()
            }

        }, NewEBookRequest(numberItem = 10, pageIndex = 1, pageSize = 10))

        getBooksRecommendUseCase.cancel()
        getBooksRecommendUseCase.executeAsync(object : ResultListener<List<BookRecommendResponse>, ErrorResponse>{
            override fun success(data: List<BookRecommendResponse>) {
                data.let {
                    it.forEach { collection ->
                        val collectionRecommendModel = CollectionRecommendModel(id = collection.id.valueOrZero(), name = collection.title.valueOrEmpty(), coverPicture = collection.coverPicture.valueOrEmpty())
                        lstCollectionRecommend.add(collectionRecommendModel)
                    }
                }
            }

            override fun fail(error: ErrorResponse) {
            }

            override fun done() {
                handleShowData()
            }

        }, GetBooksRecommendUseCase.Input(pageIndex = 1, pageSize = 10))

//        getInfoHomeUseCase.cancel()
//        getInfoHomeUseCase.executeAsync(object :
//            ResultListener<InfoHomeResponse, ErrorResponse> {
//            override fun success(data: InfoHomeResponse) {
//                view.getListNewEBookSuccess(data)
//            }
//
//            override fun fail(error: ErrorResponse) {
//                view.showErrorGetListNewEbook()
//            }
//
//            override fun done() {
//                view.hideLoading()
//            }
//
//        }, isTDN)
    }

    private fun handleShowData() {
        totalRequest +=1
        if (totalRequest == 3){
            view?.getListNewEBookSuccess(InfoHomeResponse(lstNewBook = lstNewBook, lstNewEBook = lstNewEBook, lstCollectionRecommend = lstCollectionRecommend))
        }
    }

    fun requestLogin(loginRequest: LoginRequest) {
        loginUseCase.cancel()
        loginUseCase.executeAsync(object : ResultListener<LoginResponse, ErrorResponse> {
            override fun success(data: LoginResponse) {
                val accessToken = data.accessToken.valueOrEmpty()
                val tokenType = data.tokenType.valueOrEmpty()
                if (accessToken.isNotEmpty() && tokenType.isNotEmpty()) {
                    handleAfterLoginSuccess(accessToken, tokenType)
                }
            }

            override fun fail(error: ErrorResponse) {
            }

            override fun done() {}

        }, loginRequest)
    }

    private fun handleAfterLoginSuccess(accessToken: String, tokenType: String) {
        saveInfoLoginUseCase.cancel()
        saveInfoLoginUseCase.executeAsync(object : ResultListener<Boolean, ErrorResponse> {
            override fun success(data: Boolean) {
            }

            override fun fail(error: ErrorResponse) {
            }

            override fun done() {}

        }, SaveInfoLoginUseCase.Input(accessToken = accessToken, tokenType = tokenType))
    }

    override fun onDetachView() {
        loginUseCase.cancel()
        getInfoUserUseCase.cancel()
        saveInfoLoginUseCase.cancel()
        getLoginRequestUseCase.cancel()
        getUserTokenUseCase.cancel()
        getListNewBookUseCase.cancel()
        getListNewEbookUseCase.cancel()
        getBooksRecommendUseCase.cancel()
        super.onDetachView()
    }
}