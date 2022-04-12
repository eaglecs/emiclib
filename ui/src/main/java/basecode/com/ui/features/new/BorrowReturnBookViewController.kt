package basecode.com.ui.features.new

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.LogoutSuccessEventBus
import basecode.com.domain.eventbus.model.ResultScanQRCodeBookCodeEventBus
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.extention.valueOrFalse
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.presentation.features.borrowreturn.BorrowReturnBookContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraBoolean
import basecode.com.ui.base.extra.BundleExtraString
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.extension.view.gone
import basecode.com.ui.extension.view.hideKeyboard
import basecode.com.ui.extension.view.visible
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.new.renderer.BorrowReturnBookRenderer
import basecode.com.ui.features.user.UserViewController
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.GlideUtil
import basecode.com.ui.util.ScanQRCode
import com.bluelinelabs.conductor.RouterTransaction
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_header_new_app.view.*
import kotlinx.android.synthetic.main.screen_borrow_books.view.*
import org.koin.standalone.inject

class BorrowReturnBookViewController(bundle: Bundle) : ViewController(bundle),
    BorrowReturnBookContract.View {
    private val presenter by inject<BorrowReturnBookContract.Presenter>()
    private val doubleTouchPrevent by inject<DoubleTouchPrevent>()
    private lateinit var rvController: RecyclerViewController
    private var isBorrow = false
    private var isLogin = false
    private var avatar: String = ""

    object BundleOptions {
        var Bundle.isLogin by BundleExtraBoolean("isLogin")
        var Bundle.avatar by BundleExtraString("avatar")
        var Bundle.isBorrow by BundleExtraBoolean("isBorrow")
        fun create(isBorrow: Boolean, isLogin: Boolean, avatar: String) = Bundle().apply {
            this.isBorrow = isBorrow
            this.isLogin = isLogin
            this.avatar = avatar
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            isBorrow = options.isBorrow.valueOrFalse()
            isLogin = options.isLogin.valueOrFalse()
            avatar = options.avatar.valueOrEmpty()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_borrow_books, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        initEventBus(view)
        handleView(view)
        presenter.borrowBook("VN 651/2017")
        presenter.getPatronOnLoanCopies()
        presenter.returnBook("VN 651/2017")
    }

    private fun initEventBus(view: View) {
        KBus.subscribe<LoginSuccessEventBus>(this) {
            isLogin = true
            this.avatar = it.avatar
            GlideUtil.loadImage(
                url = it.avatar,
                imageView = view.ivLogin,
                holderImage = R.drawable.user_default,
                errorImage = R.drawable.user_default
            )
        }
        KBus.subscribe<LogoutSuccessEventBus>(this) {
            isLogin = false
            avatar = ""
            view.ivLogin.setImageResource(R.drawable.ic_login)
        }

        KBus.subscribe<ResultScanQRCodeBookCodeEventBus>(this) {
            view.edtSearch.setText(it.bookCode)
        }
    }

    private fun initView(view: View) {
        val input = LinearRenderConfigFactory.Input(
            context = view.context,
            orientation = LinearRenderConfigFactory.Orientation.VERTICAL
        )
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvBooks, renderConfig)
        rvController.addViewRenderer(BorrowReturnBookRenderer(isScreenBorrow = isBorrow))

        if (isLogin) {
            GlideUtil.loadImage(
                url = avatar,
                imageView = view.ivLogin,
                holderImage = R.drawable.user_default,
                errorImage = R.drawable.user_default
            )
        } else {
            view.ivLogin.setImageResource(R.drawable.ic_login)
        }
    }


    private fun handleView(view: View) {
        view.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view.edtSearch.clearFocus()
                view.edtSearch.hideKeyboard()
                true
            }
            false
        }
        view.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(textSearchDestination: Editable?) {
                val textSearch = textSearchDestination.toString().trim()
                if (textSearch.isNotEmpty()) {
                    view.btnClearTextSearch.visible()
                } else {
                    view.btnClearTextSearch.gone()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        view.btnClearTextSearch.setOnClickListener {
            if (doubleTouchPrevent.check("btnClearTextSearchPlace")) {
                view.edtSearch.setText("")
            }
        }

        view.ivLogin.setOnClickListener {
            if (doubleTouchPrevent.check("ivLogin")) {
                if (isLogin) {
                    router.pushController(
                        RouterTransaction.with(
                            UserViewController()
                        ).pushChangeHandler(FadeChangeHandler(false))
                    )
                } else {
                    val bundle =
                        LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.Normal.value)
                    val loginViewController = LoginViewController(bundle)
                    router.pushController(
                        RouterTransaction.with(loginViewController)
                            .pushChangeHandler(FadeChangeHandler(false))
                    )
                }
            }
        }

        view.ivScanQRCode.setOnClickListener {
            if (doubleTouchPrevent.check("ivScanQRCode")) {
                ScanQRCode.openScreenQRCode(activity, this)
            }
        }

        view.ivScanBookCode.setOnClickListener {
            if (doubleTouchPrevent.check("ivScanBookCode")) {
                ScanQRCode.openScreenQRCode(activity, this)
            }
        }

        view.btnEnter.setOnClickListener {
            if (doubleTouchPrevent.check("btnEnter")) {

            }
        }
    }

    override fun showErrorBorrowBook(errorCode: Int) {

    }

    override fun showErrorReturnBook(errorCode: Int) {
    }

    override fun returnBookSuccess() {
        activity?.let { activity ->
            Toasty.success(activity, "Trả sách thành công!!!").show()
        }
    }

    override fun borrowBookSuccess() {
        activity?.let { activity ->
            Toasty.success(activity, "Mượn sách thành công!!!").show()
        }
    }

    override fun showLoading() {
        view?.apply {
            vgLoading.show()
        }
    }

    override fun hideLoading() {
        view?.apply {
            vgLoading.hide()
        }
    }

    override fun onDestroyView(view: View) {
        presenter.detachView()
        super.onDestroyView(view)
    }
}