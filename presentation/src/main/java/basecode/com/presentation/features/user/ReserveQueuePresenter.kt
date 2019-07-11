package basecode.com.presentation.features.user

import basecode.com.domain.features.GetListReserveQueueUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.ReserveResponse
import basecode.com.domain.usecase.base.ResultListener

class ReserveQueuePresenter(private val getListReserveQueueUseCase: GetListReserveQueueUseCase): ReserveQueueContract.Presenter() {
    override fun getListBookReserveQueue() {
        view?.let { view ->
            view.showLoading()
            getListReserveQueueUseCase.cancel()
            getListReserveQueueUseCase.executeAsync(object : ResultListener<List<ReserveResponse>, ErrorResponse> {
                override fun success(data: List<ReserveResponse>) {
                    view.getListBookReserveQueueSuccess(data)
                }

                override fun fail(error: ErrorResponse) {
                    view.getListBookReserveQueueFail()
                }

                override fun done() {
                    view.hideLoading()
                }

            })
        }
    }

    override fun onDetachView() {
        getListReserveQueueUseCase.cancel()
        super.onDetachView()
    }
}