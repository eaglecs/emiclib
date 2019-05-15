package basecode.com.presentation.features

import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class HomeContract {
    interface View : ViewMvp, ViewSupportLoading {
    }

    abstract class Presenter : PresenterMvp<View>() {

    }
}