package basecode.com.presentation.features.home

import basecode.com.domain.features.GetInfoHomeUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.InfoHomeResponse
import basecode.com.domain.usecase.base.ResultListener

class HomePresenter(private val getInfoHomeUseCase: GetInfoHomeUseCase) : HomeContract.Presenter() {
    override fun getListNewBook() {
        view?.let { view ->
            view.showLoading()
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

    }

    override fun onDetachView() {
        getInfoHomeUseCase.cancel()
        super.onDetachView()
    }
}