package basecode.com.presentation.features.user

import basecode.com.domain.features.GetListReserveRequestUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.ReserveResponse
import basecode.com.domain.usecase.base.ResultListener

class ReserveRequestPresenter(private val getListReserveRequestUseCase: GetListReserveRequestUseCase): ReserveRequestContract.Presenter() {
    override fun getListBookReserveRequest() {
        view?.let { view ->
            view.showLoading()
            getListReserveRequestUseCase.cancel()
            getListReserveRequestUseCase.executeAsync(object : ResultListener<List<ReserveResponse>, ErrorResponse> {
                override fun success(data: List<ReserveResponse>) {
                    view.getListBookReserveRequestSuccess(data)
                }

                override fun fail(error: ErrorResponse) {
                    view.getListBookReserveRequestFail()
                }

                override fun done() {
                    view.hideLoading()
                }

            })
        }
    }

    override fun onDetachView() {
        getListReserveRequestUseCase.cancel()
        super.onDetachView()
    }
}