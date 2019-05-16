package basecode.com.presentation.features.home

import basecode.com.domain.features.GetListNewEbookItemsUseCase
import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.NewEBookResponse
import basecode.com.domain.usecase.base.ResultListener

class HomePresenter(private val getListNewEbookItemsUseCase: GetListNewEbookItemsUseCase) : HomeContract.Presenter() {
    override fun getListNewBook(newEBookRequest: NewEBookRequest) {
        view?.let { view ->
            view.showLoading()
            getListNewEbookItemsUseCase.cancel()
            getListNewEbookItemsUseCase.executeAsync(object : ResultListener<NewEBookResponse, ErrorResponse> {
                override fun success(data: NewEBookResponse) {
                    view.getListNewEBookSuccess()
                }

                override fun fail(error: ErrorResponse) {
                    view.showErrorGetListNewEbook()
                }

                override fun done() {
                    view.hideLoading()
                }

            }, newEBookRequest)
        }

    }

    override fun onDetachView() {
        getListNewEbookItemsUseCase.cancel()
        super.onDetachView()
    }
}