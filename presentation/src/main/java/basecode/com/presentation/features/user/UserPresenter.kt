package basecode.com.presentation.features.user

import basecode.com.domain.features.GetInfoUserUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.InfoUserResponse
import basecode.com.domain.usecase.base.ResultListener

class UserPresenter(private val getInfoUserUseCase: GetInfoUserUseCase) : UserContract.Presenter() {
    override fun getUserInfo() {
        view?.let { view ->
            view.showLoading()
            getInfoUserUseCase.cancel()
            getInfoUserUseCase.executeAsync(object : ResultListener<InfoUserResponse, ErrorResponse> {
                override fun success(data: InfoUserResponse) {
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

    override fun onDetachView() {
        getInfoUserUseCase.cancel()
        super.onDetachView()
    }
}