package basecode.com.presentation.features.setting

import basecode.com.domain.features.GetUserTokenUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.usecase.base.ResultListener

class SettingPresenter(private val getUserTokenUseCase: GetUserTokenUseCase) : SettingContract.Presenter() {
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
        super.onDetachView()
    }
}