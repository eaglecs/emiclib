package basecode.com.ui.features.requestdocument

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.model.network.request.RequestDocumentRequest
import basecode.com.presentation.features.requestdocument.RequestDocumentContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.extension.view.hideKeyboard
import basecode.com.ui.extension.view.shake
import basecode.com.ui.util.DoubleTouchPrevent
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_request_document.view.*
import org.koin.standalone.inject

class RequestDocumentViewController : ViewController(null), RequestDocumentContract.View {
    private val presenter: RequestDocumentContract.Presenter by inject()
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_request_document, container, false)
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

        view.btnRequestDocument.setOnClickListener {
            if (doubleTouchPrevent.check("btnChangePass")) {
                val name = view.edtNameDocument.text.toString()
                if(name.isEmpty()){
                    view.edtNameDocument.shake()
                    return@setOnClickListener
                }

                val patronCode = view.edtPatronCode.text.toString()
                val email = view.edtEmail.text.toString()
                val phone = view.edtPhone.text.toString()
                val facebook = view.edtFacebook.text.toString()
                val supplier = view.edtSupplier.text.toString()
                val groupName = view.edtGroupName.text.toString()
                val title = view.edtTitle.text.toString()
                if(title.isEmpty()){
                    view.edtTitle.shake()
                    return@setOnClickListener
                }
                val author = view.edtAuthor.text.toString()
                val publisher = view.edtPublisher.text.toString()
                val publishYear = view.edtPublishYear.text.toString()
                val information = view.edtInformation.text.toString()

                val requestDocumentRequest = RequestDocumentRequest(fullName = name, supplier = supplier, publishYear = publishYear,
                        publisher = publisher, phone = phone, patronCode = patronCode, information = information, groupName = groupName,
                        facebook = facebook, email = email, author = author, title = title)
                presenter.requestDocument(requestDocumentRequest)
            }
        }
    }

    override fun requestDocumentSuccess() {
        view?.let { view ->
            Toasty.success(view.context, view.context.resources.getString(R.string.msg_request_document_success)).show()
        }
        router.popController(this)
    }

    override fun requestDocumentFail() {
        view?.let { view ->
            Toasty.error(view.context, view.context.resources.getString(R.string.msg_request_document_fail)).show()
        }
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgLoading.show()
        }
    }

    override fun hideLoading() {
        view?.let { view ->
            view.hideKeyboard()
            view.vgLoading.hide()
        }
    }

    override fun onDestroyView(view: View) {
        view.hideKeyboard()
        presenter.detachView()
        super.onDestroyView(view)
    }
}