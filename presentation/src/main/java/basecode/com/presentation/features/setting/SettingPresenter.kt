package basecode.com.presentation.features.setting

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.features.GetListMessageUseCase
import basecode.com.domain.features.GetLoginRequestUseCase
import basecode.com.domain.features.GetUserTokenUseCase
import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.MessageResponse
import basecode.com.domain.usecase.base.ResultListener

class SettingPresenter(private val getUserTokenUseCase: GetUserTokenUseCase,
                       private val getListMessageUseCase: GetListMessageUseCase,
                       private val getLoginRequestUseCase: GetLoginRequestUseCase) : SettingContract.Presenter() {
    override fun getUserInfo() {
        view?.let { view ->
            view.showLoading()
            getLoginRequestUseCase.cancel()
            getLoginRequestUseCase.executeAsync(object : ResultListener<LoginRequest, ErrorResponse> {
                override fun success(data: LoginRequest) {
                    view.getUserInfoSuccess(data)
                }

                override fun fail(error: ErrorResponse) {
                }

                override fun done() {
                    view.hideLoading()
                }

            })

        }
    }

    override fun checkNewMessage() {
        view?.let { view ->
            view.showLoading()
            getListMessageUseCase.cancel()
            getListMessageUseCase.executeAsync(object : ResultListener<List<MessageResponse>, ErrorResponse> {
                override fun success(data: List<MessageResponse>) {
                    var isHasNewMessage = false
                    loop@ for (message in data) {
                        if (message.status.valueOrZero() != 1) {
                            isHasNewMessage = true
                            break@loop
                        }
                    }
                    view.setStatusNewMessage(isHasNewMessage)
                }

                override fun fail(error: ErrorResponse) {
                }

                override fun done() {
                    view.hideLoading()
                }

            })
        }
    }

    override fun checkLogin() {
        view?.let { view ->
            view.showLoading()
            getUserTokenUseCase.cancel()
            getUserTokenUseCase.executeAsync(object : ResultListener<String, ErrorResponse> {
                override fun success(data: String) {
                    view.resultCheckLogin(data.isNotEmpty())
                }

                override fun fail(error: ErrorResponse) {
                    view.resultCheckLogin(false)
                }

                override fun done() {
                    view.hideLoading()
                }
            })
        }
    }

    override fun onDetachView() {
        getUserTokenUseCase.cancel()
        getLoginRequestUseCase.cancel()
        getListMessageUseCase.cancel()
        super.onDetachView()
    }
}