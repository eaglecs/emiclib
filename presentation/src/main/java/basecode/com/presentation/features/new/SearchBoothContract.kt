package basecode.com.presentation.features.new

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading
import basecode.com.presentation.features.new.model.BoothViewModel

class SearchBoothContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun showBooths(lstBooth: List<BoothViewModel>)
        fun showErrorGetBooths()

    }
    abstract class Presenter : PresenterMvp<View>() {
        abstract fun getBooths()
    }
}