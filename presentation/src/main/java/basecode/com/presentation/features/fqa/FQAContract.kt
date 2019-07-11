package basecode.com.presentation.features.fqa

import basecode.com.domain.model.network.response.QAResponse
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class FQAContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListFQASuccess(lstFQA: List<QAResponse>)
        fun getListFQAFail()
    }
    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListFQA()
    }
}