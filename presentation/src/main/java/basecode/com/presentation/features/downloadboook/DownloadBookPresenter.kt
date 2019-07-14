package basecode.com.presentation.features.downloadboook

import basecode.com.domain.features.GetAllBookUseCase
import basecode.com.domain.model.dbflow.EBookModel
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.usecase.base.ResultListener

class DownloadBookPresenter(private val getAllBookUseCase: GetAllBookUseCase) : DownloadBookContract.Presenter() {
    override fun getListBookDownload() {
        view?.let { view ->
            view.showLoading()
            getAllBookUseCase.cancel()
            getAllBookUseCase.executeAsync(object : ResultListener<List<EBookModel>, ErrorResponse> {
                override fun success(data: List<EBookModel>) {
                    view.getListBookDownloadSuccess(data)
                }

                override fun fail(error: ErrorResponse) {
                    view.getListBookDownloadFail()
                }

                override fun done() {
                    view.hideLoading()
                }

            })
        }
    }

    override fun onDetachView() {
        getAllBookUseCase.cancel()
        super.onDetachView()
    }
}