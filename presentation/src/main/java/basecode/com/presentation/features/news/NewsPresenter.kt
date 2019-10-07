package basecode.com.presentation.features.news

import basecode.com.domain.features.GetListNewsUseCase
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.usecase.base.ResultListener

class NewsPresenter(private val getListNewsUseCase: GetListNewsUseCase) : NewsContract.Presenter() {
    private val pageSize = 10
    private var pageIndexCurrent = 1
    private var isLoadMoreNewNews = true

    override fun getListNewNews(isRefresh: Boolean) {
        view?.let { view ->
            if (isRefresh) {
                view.showLoading()
                pageIndexCurrent = 1
                isLoadMoreNewNews = true
            }
            getListNewsUseCase.cancel()
            getListNewsUseCase.executeAsync(object : ResultListener<List<NewNewsResponse>, ErrorResponse> {
                override fun success(data: List<NewNewsResponse>) {
                    if (data.size < pageSize) {
                        isLoadMoreNewNews = false
                    } else {
                        pageIndexCurrent += 1
                    }
                    view.getListNewNewsSuccess(data, isRefresh)
                }

                override fun fail(error: ErrorResponse) {
                    view.hideLoading()
                    view.showErrorGetListNewNews()
                }

                override fun done() {}

            }, pageIndexCurrent)
        }
    }

    override fun isShowLoadMore(): Boolean = isLoadMoreNewNews
    override fun onDetachView() {
        getListNewsUseCase.cancel()
        super.onDetachView()
    }
}