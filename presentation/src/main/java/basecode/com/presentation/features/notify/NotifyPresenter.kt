package basecode.com.presentation.features.notify

import basecode.com.domain.features.GetListMessageUseCase
import basecode.com.domain.features.ReadMessageUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.MessageResponse
import basecode.com.domain.usecase.base.ResultListener

class NotifyPresenter(private val getListMessageUseCase: GetListMessageUseCase,
                      private val readMessageUseCase: ReadMessageUseCase) : NotifyContract.Presenter() {
    override fun getListNotify() {
        view?.let { view ->
            view.showLoading()
            getListMessageUseCase.cancel()
            getListMessageUseCase.executeAsync(object : ResultListener<List<MessageResponse>, ErrorResponse> {
                override fun success(data: List<MessageResponse>) {
                    view.getListNotifySuccess(data)
                }

                override fun fail(error: ErrorResponse) {
                    view.getListNotifyFail()
                }

                override fun done() {
                    view.hideLoading()
                }

            })
        }
    }

    override fun readMessage(messageId: Long) {
        view?.let { view ->
            readMessageUseCase.cancel()
            readMessageUseCase.executeAsync(object : ResultListener<Any, ErrorResponse> {
                override fun success(data: Any) {
                    view.readMessageSuccess(messageId)
                }

                override fun fail(error: ErrorResponse) {
                    view.readMessageFail()
                }

                override fun done() {
                    view.readMessageFail()
                }

            }, messageId)
        }
    }

    override fun onDetachView() {
        readMessageUseCase.cancel()
        getListMessageUseCase.cancel()
        super.onDetachView()
    }
}