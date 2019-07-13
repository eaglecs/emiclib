package basecode.com.ui.features.feedback

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.features.FeedbackUseCase
import basecode.com.presentation.features.feedback.FeedbackContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.util.DoubleTouchPrevent
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_feedback.view.*
import org.koin.standalone.inject

class FeedbackViewController : ViewController(null), FeedbackContract.View {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()

    private val presenter: FeedbackContract.Presenter by inject()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_feedback, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        handleView(view)
    }

    private fun handleView(view: View) {
        view.ivBack.setOnClickListener {
            if (doubleTouchPrevent.check("ivBack")) {
                router.popController(this)
            }
        }

        view.ivSendFeedBack.setOnClickListener {
            if (doubleTouchPrevent.check("ivSendFeedBack")) {
                val email = view.edtEmail.text.toString()
                val content = view.edtContent.text.toString()
                if (email.isEmpty()) {
                    Toasty.warning(view.context, view.context.getString(R.string.msg_warning_email_empty)).show()
                } else {
                    if (content.isEmpty()) {
                        Toasty.warning(view.context, view.context.getString(R.string.msg_warning_content_empty)).show()
                    } else {
                        val title = view.edtTitle.text.toString()
                        val nameUser = view.edtNameUser.text.toString()
                        val phoneNumber = view.edtPhoneNumber.text.toString()
                        val input = FeedbackUseCase.Input(content = content, email = email, userFullName = nameUser,
                                mobilePhone = phoneNumber, title = title)
                        presenter.feedback(input)
                    }
                }

            }
        }
    }

    override fun feedbackSuccess() {
        activity?.let { activity ->
            Toasty.success(activity, activity.resources.getString(R.string.msg_send_feedback_success)).show()
        }
        router.popController(this)
    }

    override fun feedbackFail() {
        activity?.let { activity ->
            Toasty.error(activity, activity.resources.getString(R.string.msg_error_feedback)).show()
        }
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgLoading.show()
        }
    }

    override fun hideLoading() {
        view?.let { view ->
            view.vgLoading.hide()
        }
    }

    override fun onDestroyView(view: View) {
        presenter.detachView()
        super.onDestroyView(view)
    }
}