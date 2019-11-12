package basecode.com.presentation.features.requestdocument

import basecode.com.domain.model.network.request.RequestDocumentRequest
import basecode.com.domain.model.network.response.UserModel
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class RequestDocumentContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun requestDocumentSuccess()
        fun requestDocumentFail()
        fun getUserInfoSuccess(data: UserModel)
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun requestDocument(requestDocumentRequest: RequestDocumentRequest)
        abstract fun getUserInfo()
    }
}