package basecode.com.presentation.features.feedback

import basecode.com.domain.features.FeedbackUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.usecase.base.ResultListener

class FeedbackPresenter(private val feedbackUseCase: FeedbackUseCase): FeedbackContract.Presenter() {
    override fun feedback(input: FeedbackUseCase.Input) {
        view?.let { view ->
            view.showLoading()
            feedbackUseCase.cancel()
            feedbackUseCase.executeAsync(object : ResultListener<Int, ErrorResponse>{
                override fun success(data: Int) {
                    if(data == 1){
                        view.feedbackSuccess()
                    } else {
                        view.feedbackFail()
                    }
                }

                override fun fail(error: ErrorResponse) {
                    view.feedbackFail()
                }

                override fun done() {
                    view.hideLoading()
                }

            }, input)
        }
    }

    override fun onDetachView() {
        feedbackUseCase.cancel()
        super.onDetachView()
    }
}