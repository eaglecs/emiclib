package basecode.com.presentation.features.requestdocument

import basecode.com.domain.features.GetInfoUserUseCase
import basecode.com.domain.features.GetUserTokenUseCase
import basecode.com.domain.features.RequestDocumentUseCase
import basecode.com.domain.model.network.request.RequestDocumentRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.UserModel
import basecode.com.domain.usecase.base.ResultListener

class RequestDocumentPresenter(
    private val requestDocumentUseCase: RequestDocumentUseCase,
    private val getInfoUserUseCase: GetInfoUserUseCase,
    private val getUserTokenUseCase: GetUserTokenUseCase
) : RequestDocumentContract.Presenter() {
    override fun getUserInfo() {
        view?.let { view ->
            view.showLoading()
            getUserTokenUseCase.cancel()
            getUserTokenUseCase.executeAsync(object : ResultListener<String, ErrorResponse>{
                override fun success(data: String) {
                    if (data.isNotEmpty()) {
                        getInfoUserUseCase.cancel()
                        getInfoUserUseCase.executeAsync(object : ResultListener<UserModel, ErrorResponse> {
                            override fun success(data: UserModel) {
                                view.getUserInfoSuccess(data)
                            }

                            override fun fail(error: ErrorResponse) {
                            }

                            override fun done() {
                                view.hideLoading()
                            }

                        })
                    }
                }

                override fun fail(error: ErrorResponse) {
                }

                override fun done() {

                }

            })
        }
    }

    override fun requestDocument(requestDocumentRequest: RequestDocumentRequest) {
        view?.let { view ->
            view.showLoading()
            requestDocumentUseCase.cancel()
            requestDocumentUseCase.executeAsync(object : ResultListener<Int, ErrorResponse> {
                override fun success(data: Int) {
                    if (data == 1) {
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
        getUserTokenUseCase.cancel()
        requestDocumentUseCase.cancel()
        getInfoUserUseCase.cancel()
        super.onDetachView()
    }
}