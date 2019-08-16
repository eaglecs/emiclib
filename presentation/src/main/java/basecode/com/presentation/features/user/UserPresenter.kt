package basecode.com.presentation.features.user

import basecode.com.domain.features.GetInfoUserUseCase
import basecode.com.domain.features.GetListReserveQueueUseCase
import basecode.com.domain.features.GetListReserveRequestUseCase
import basecode.com.domain.features.SaveInfoLoginUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.InfoUserResponse
import basecode.com.domain.model.network.response.ReserveResponse
import basecode.com.domain.model.network.response.UserModel
import basecode.com.domain.usecase.base.ResultListener

class UserPresenter(private val getInfoUserUseCase: GetInfoUserUseCase,
                    private val saveInfoLoginUseCase: SaveInfoLoginUseCase

) : UserContract.Presenter() {

    override fun getUserInfo() {
        view?.let { view ->
            view.showLoading()
            getInfoUserUseCase.cancel()
            getInfoUserUseCase.executeAsync(object : ResultListener<UserModel, ErrorResponse> {
                override fun success(data: UserModel) {
                    view.getUserInfoSuccess(data)
                }

                override fun fail(error: ErrorResponse) {
                    view.getUserInfoFail()
                }

                override fun done() {
                    view.hideLoading()
                }

            })
        }
    }

    override fun logout() {
        view?.let { view ->
            view.showLoading()
            saveInfoLoginUseCase.executeAsync(object : ResultListener<Boolean, ErrorResponse> {
                override fun success(data: Boolean) {
                    view.logoutSuccess()
                }

                override fun fail(error: ErrorResponse) {
                    view.logoutFail()
                }

                override fun done() {
                    view.hideLoading()
                }

            }, SaveInfoLoginUseCase.Input(accessToken = "", tokenType = ""))
        }
    }

    override fun onDetachView() {
        getInfoUserUseCase.cancel()
        saveInfoLoginUseCase.cancel()
        super.onDetachView()
    }
}