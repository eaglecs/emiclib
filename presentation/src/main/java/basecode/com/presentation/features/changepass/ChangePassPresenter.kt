package basecode.com.presentation.features.changepass

import basecode.com.domain.features.ChangePassUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.usecase.base.ResultListener

class ChangePassPresenter(private val changePassUseCase: ChangePassUseCase) : ChangePassContract.Presenter() {
    override fun changePass(oldPass: String, newPass: String) {
        view?.let { view ->
            view.showLoading()
            changePassUseCase.cancel()
            changePassUseCase.executeAsync(object : ResultListener<Int, ErrorResponse> {
                override fun success(data: Int) {
                    view.changePassSuccess()
                }

                override fun fail(error: ErrorResponse) {
                    view.changePassFail()
                }

                override fun done() {
                    view.hideLoading()
                }

            }, ChangePassUseCase.Input(oldPassword = oldPass, newPassword = newPass))
        }
    }

    override fun onDetachView() {
        changePassUseCase.cancel()
        super.onDetachView()
    }
}