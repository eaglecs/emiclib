package basecode.com.presentation.features.news

import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class NewsContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListNewNewsSuccess(data: List<NewNewsResponse>, refresh: Boolean)
        fun showErrorGetListNewNews()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListNewNews(isRefresh: Boolean)
        abstract fun isShowLoadMore(): Boolean
    }
}