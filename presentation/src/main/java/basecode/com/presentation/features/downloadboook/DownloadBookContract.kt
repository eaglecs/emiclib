package basecode.com.presentation.features.downloadboook

import basecode.com.domain.model.dbflow.EBookModel
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class DownloadBookContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun getListBookDownloadSuccess(lstBook: List<EBookModel>)
        fun getListBookDownloadFail()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getListBookDownload()
    }
}