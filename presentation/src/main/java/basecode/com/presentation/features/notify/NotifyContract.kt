package basecode.com.presentation.features.notify

import basecode.com.domain.model.network.response.MessageResponse
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class NotifyContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListNotifySuccess(data: List<MessageResponse>)
        fun getListNotifyFail()
        fun readMessageSuccess(messageId: Long)
        fun readMessageFail()

    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListNotify()
        abstract fun readMessage(messageId: Long)
    }
}