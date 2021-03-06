package basecode.com.presentation.features.login

import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.GetInfoUserUseCase
import basecode.com.domain.features.LoginUseCase
import basecode.com.domain.features.SaveInfoLoginUseCase
import basecode.com.domain.features.SaveLoginRequestUseCase
import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.LoginResponse
import basecode.com.domain.model.network.response.UserModel
import basecode.com.domain.usecase.base.ResultListener

class LoginPresenter(private val loginUseCase: LoginUseCase, private val saveInfoLoginUseCase: SaveInfoLoginUseCase,
                     private val saveLoginRequestUseCase: SaveLoginRequestUseCase, private val getInfoUserUseCase: GetInfoUserUseCase) : LoginContract.Presenter() {
    override fun requestLogin(loginRequest: LoginRequest) {
        view?.let { view ->
            view.showLoading()
            loginUseCase.cancel()
            loginUseCase.executeAsync(object : ResultListener<LoginResponse, ErrorResponse> {
                override fun success(data: LoginResponse) {
                    val accessToken = data.accessToken.valueOrEmpty()
                    val tokenType = data.tokenType.valueOrEmpty()
                    handleAfterLoginSuccess(accessToken, tokenType, loginRequest)
                }

                override fun fail(error: ErrorResponse) {
                    view.hideLoading()
                    view.resultLogin(false)
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
                    saveLoginRequestUseCase.cancel()
                    saveLoginRequestUseCase.executeAsync(object : ResultListener<Boolean, ErrorResponse>{
                        override fun success(data: Boolean) {
                            getInfoUserUseCase.cancel()
                            getInfoUserUseCase.executeAsync(object : ResultListener<UserModel, ErrorResponse> {
                                override fun success(userModel: UserModel) {
                                    if (accessToken.isNotEmpty() && tokenType.isNotEmpty()) {
                                        view.resultLogin(true, userModel)
                                    } else {
                                        view.resultLogin(false, userModel)
                                    }

                                }

                                override fun fail(error: ErrorResponse) {
                                    view.resultLogin(false)
                                }

                                override fun done() {
                                    view.hideLoading ()
                                }

                            })
                        }

                        override fun fail(error: ErrorResponse) {
                            view.resultLogin(false)
                        }

                        override fun done() {
                        }

                    }, loginRequest)
                }

                override fun fail(error: ErrorResponse) {
                    view.hideLoading()
                    view.resultLogin(false)
                }

                override fun done() {}

            }, SaveInfoLoginUseCase.Input(accessToken = accessToken, tokenType = tokenType))
        }
    }

    override fun onDetachView() {
        loginUseCase.cancel()
        getInfoUserUseCase.cancel()
        saveLoginRequestUseCase.cancel()
        saveInfoLoginUseCase.cancel()
        super.onDetachView()
    }
}