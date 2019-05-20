package basecode.com.presentation.features.home

import basecode.com.domain.features.GetInfoHomeUseCase
import basecode.com.domain.features.GetUserTokenUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.InfoHomeResponse
import basecode.com.domain.usecase.base.ResultListener

class HomePresenter(private val getInfoHomeUseCase: GetInfoHomeUseCase,
                    private val getUserTokenUseCase: GetUserTokenUseCase) : HomeContract.Presenter() {
    override fun getListNewBook() {
        view?.let { view ->
            view.showLoading()
            getUserTokenUseCase.cancel()
            getUserTokenUseCase.executeAsync(object : ResultListener<String, ErrorResponse>{
                override fun success(data: String) {
                    view.handleAfterCheckLogin(data.isNotEmpty())
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

                    })
                }
            })
        }

    }

    override fun onDetachView() {
        getUserTokenUseCase.cancel()
        getInfoHomeUseCase.cancel()
        super.onDetachView()
    }
}