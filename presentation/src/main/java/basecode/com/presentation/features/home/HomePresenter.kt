package basecode.com.presentation.features.home

import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.*
import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.InfoHomeResponse
import basecode.com.domain.model.network.response.LoginResponse
import basecode.com.domain.model.network.response.UserModel
import basecode.com.domain.usecase.base.ResultListener

class HomePresenter(private val getInfoHomeUseCase: GetInfoHomeUseCase,
                    private val getLoginRequestUseCase: GetLoginRequestUseCase,
                    private val loginUseCase: LoginUseCase,
                    private val saveInfoLoginUseCase: SaveInfoLoginUseCase,
                    private val getInfoUserUseCase: GetInfoUserUseCase,
                    private val getUserTokenUseCase: GetUserTokenUseCase) : HomeContract.Presenter() {
    override fun getListNewBook(isTDN: Boolean) {
        view?.let { view ->
            view.showLoading()
            getLoginRequestUseCase.cancel()
            getLoginRequestUseCase.executeAsync(object : ResultListener<LoginRequest, ErrorResponse> {
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
                    getInfoUserUseCase.cancel()
                    getInfoUserUseCase.executeAsync(object : ResultListener<UserModel, ErrorResponse> {
                        override fun success(userModel: UserModel) {
                            view.handleAfterCheckLogin(data.isNotEmpty(), userModel.linkAvatar)

                        }

                        override fun fail(error: ErrorResponse) {
                            view.handleAfterCheckLogin(false)
                        }

                        override fun done() {
                            view.hideLoading()
                        }

                    })
                }

                override fun fail(error: ErrorResponse) {
                    view.handleAfterCheckLogin(false)
                }

                override fun done() {
                    getInfoHomeUseCase.cancel()
                    getInfoHomeUseCase.executeAsync(object : ResultListener<InfoHomeResponse, ErrorResponse> {
                        override fun success(data: InfoHomeResponse) {
                            view.getListNewEBookSuccess(data)
                        }

                        override fun fail(error: ErrorResponse) {
                            view.hideLoading()
                            view.showErrorGetListNewEbook()
                        }

                        override fun done() {}

                    }, isTDN)
                }
            })

        }
    }

    fun requestLogin(loginRequest: LoginRequest) {
        view?.let { view ->
            view.showLoading()
            loginUseCase.cancel()
            loginUseCase.executeAsync(object : ResultListener<LoginResponse, ErrorResponse> {
                override fun success(data: LoginResponse) {
                    val accessToken = data.accessToken.valueOrEmpty()
                    val tokenType = data.tokenType.valueOrEmpty()
                    if (accessToken.isNotEmpty() && tokenType.isNotEmpty()) {
                        handleAfterLoginSuccess(accessToken, tokenType, loginRequest)
                    }
                }

                override fun fail(error: ErrorResponse) {
                }

                override fun done() {}

            }, loginRequest)
        }
    }

    private fun handleAfterLoginSuccess(accessToken: String, tokenType: String, loginRequest: LoginRequest) {
        view?.let { view ->
            saveInfoLoginUseCase.cancel()
            saveInfoLoginUseCase.executeAsync(object : ResultListener<Boolean, ErrorResponse> {
                override fun success(data: Boolean) {
                }

                override fun fail(error: ErrorResponse) {
                }

                override fun done() {}

            }, SaveInfoLoginUseCase.Input(accessToken = accessToken, tokenType = tokenType))
        }
    }

    override fun onDetachView() {
        loginUseCase.cancel()
        getInfoUserUseCase.cancel()
        saveInfoLoginUseCase.cancel()
        getLoginRequestUseCase.cancel()
        getUserTokenUseCase.cancel()
        getInfoHomeUseCase.cancel()
        super.onDetachView()
    }
}