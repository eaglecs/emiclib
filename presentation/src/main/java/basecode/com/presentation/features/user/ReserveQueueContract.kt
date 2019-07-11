package basecode.com.presentation.features.user

import basecode.com.domain.model.network.response.ReserveResponse
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class ReserveQueueContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListBookReserveQueueSuccess(data: List<ReserveResponse>)
        fun getListBookReserveQueueFail()
    }
    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListBookReserveQueue()
    }
}