package basecode.com.presentation.features.fqa

import basecode.com.domain.features.GetListFQAUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.QAResponse
import basecode.com.domain.usecase.base.ResultListener

class FQAPresenter(private val getListFQAUseCase: GetListFQAUseCase): FQAContract.Presenter() {
    override fun getListFQA() {
        view?.let { view ->
            view.showLoading()
            getListFQAUseCase.cancel()
            getListFQAUseCase.executeAsync(object : ResultListener<List<QAResponse>, ErrorResponse>{
                override fun success(data: List<QAResponse>) {
                    view.getListFQASuccess(data)
                }

                override fun fail(error: ErrorResponse) {
                    view.getListFQAFail()
                }

                override fun done() {
                    view.hideLoading()
                }

            })
        }
    }

    override fun onDetachView() {
        getListFQAUseCase.cancel()
        super.onDetachView()
    }
}