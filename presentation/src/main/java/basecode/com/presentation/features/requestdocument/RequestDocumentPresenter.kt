package basecode.com.presentation.features.requestdocument

import basecode.com.domain.features.RequestDocumentUseCase
import basecode.com.domain.model.network.request.RequestDocumentRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.usecase.base.ResultListener

class RequestDocumentPresenter(private val requestDocumentUseCase: RequestDocumentUseCase) : RequestDocumentContract.Presenter() {
    override fun requestDocument(requestDocumentRequest: RequestDocumentRequest) {
        view?.let { view ->
            view.showLoading()
            requestDocumentUseCase.cancel()
            requestDocumentUseCase.executeAsync(object : ResultListener<Int, ErrorResponse> {
                override fun success(data: Int) {
                    if(data == 1){
                        view.requestDocumentSuccess()
                    } else {
                        view.requestDocumentFail()
                    }
                }

                override fun fail(error: ErrorResponse) {
                    view.requestDocumentFail()
                }

                override fun done() {
                    view.hideLoading()
                }

            }, requestDocumentRequest)
        }
    }

    override fun onDetachView() {
        requestDocumentUseCase.cancel()
        super.onDetachView()
    }
}