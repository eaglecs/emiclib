package basecode.com.presentation.features.renew

import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class RenewContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListLoanRenewSuccess(lstBook: List<NewNewsResponse>)
        fun getListLoanRenewFail(msgError: String)
        fun renewSuccess()
        fun renewfail()
    }
    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListLoanRenew()
        abstract fun renew(coppynumber: String)
    }
}