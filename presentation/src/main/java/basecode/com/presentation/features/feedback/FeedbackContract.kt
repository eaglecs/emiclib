package basecode.com.presentation.features.feedback

import basecode.com.domain.features.FeedbackUseCase
import basecode.com.presentation.base.mvp.PresenterMvp
import basecode.com.presentation.base.mvp.ViewMvp
import basecode.com.presentation.base.mvp.view.ViewSupportLoading

class FeedbackContract {
    interface View : ViewMvp, ViewSupportLoading {
        fun feedbackSuccess()
        fun feedbackFail()
    }

    abstract class Presenter : PresenterMvp<View>() {
        abstract fun feedback(input: FeedbackUseCase.Input)
    }
}