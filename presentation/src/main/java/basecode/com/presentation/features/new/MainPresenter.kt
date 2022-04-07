package basecode.com.presentation.features.new

import basecode.com.domain.features.GetInfoUserUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.UserModel
import basecode.com.domain.usecase.base.ResultListener

class MainPresenter(private val getInfoUserUseCase: GetInfoUserUseCase): MainContract.Presenter() {
    override fun checkLogin() {
        view?.let { view ->
            view.showLoading()
            getInfoUserUseCase.cancel()
            getInfoUserUseCase.executeAsync(object : ResultListener<UserModel, ErrorResponse> {
                override fun success(data: UserModel) {
                    if (data.patronCode.isNotEmpty()) {
                        view.handleAfterCheckLogin(isLogin = true, avatar = data.linkAvatar)
                    } else {
                        view.handleAfterCheckLogin(false, "")
                    }
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
}