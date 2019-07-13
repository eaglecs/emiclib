package basecode.com.presentation.features.user

import basecode.com.domain.model.network.response.ReserveResponse
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class ReserveQueueRequestContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListBookSuccess(data: List<ReserveResponse>)
        fun getListBookFail()
    }
    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListBookReserveQueue()
        abstract fun getListBookReserveRequest()
    }
}