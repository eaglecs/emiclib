package basecode.com.presentation.features.user

import basecode.com.domain.features.GetListReserveQueueUseCase
import basecode.com.domain.features.GetListReserveRequestUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.ReserveResponse
import basecode.com.domain.usecase.base.ResultListener

class ReserveQueueRequestPresenter(private val getListReserveQueueUseCase: GetListReserveQueueUseCase,
                                   private val getListReserveRequestUseCase: GetListReserveRequestUseCase) : ReserveQueueRequestContract.Presenter() {
    override fun getListBookReserveQueue() {
        view?.let { view ->
            view.showLoading()
            getListReserveQueueUseCase.cancel()
            getListReserveQueueUseCase.executeAsync(object : ResultListener<List<ReserveResponse>, ErrorResponse> {
                override fun success(data: List<ReserveResponse>) {
                    view.getListBookSuccess(data)
                }

                override fun fail(error: ErrorResponse) {
                    view.getListBookFail()
                }

                override fun done() {
                    view.hideLoading()
                }

            })
        }
    }

    override fun getListBookReserveRequest() {
        view?.let { view ->
            view.showLoading()
            getListReserveRequestUseCase.cancel()
            getListReserveRequestUseCase.executeAsync(object : ResultListener<List<ReserveResponse>, ErrorResponse> {
                override fun success(data: List<ReserveResponse>) {
                    view.getListBookSuccess(data)
                }

                override fun fail(error: ErrorResponse) {
                    view.getListBookFail()
                }

                override fun done() {
                    view.hideLoading()
                }

            })
        }
    }

    override fun onDetachView() {
        getListReserveQueueUseCase.cancel()
        getListReserveRequestUseCase.cancel()
        super.onDetachView()
    }
}