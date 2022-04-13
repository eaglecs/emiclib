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
import basecode.com.presentation.features.borrowreturn.model.BookBorrowNewViewModel
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
import basecode.com.ui.features.new.mapper.BookBorrowNewViewHolderModelMapper
import basecode.com.ui.features.new.renderer.BorrowReturnBookRenderer
import basecode.com.ui.features.new.viewholder.BookBorrowNewViewHolderModel
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
    private lateinit var rvControllerBookReturn: RecyclerViewController
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
        presenter.getPatronOnLoanCopies()
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

        val inputBookReturn = LinearRenderConfigFactory.Input(
            context = view.context,
            orientation = LinearRenderConfigFactory.Orientation.VERTICAL
        )
        val renderConfigBookReturn = LinearRenderConfigFactory(inputBookReturn).create()
        rvControllerBookReturn =
            RecyclerViewController(view.rvBooksBookReturn, renderConfigBookReturn)
        rvControllerBookReturn.addViewRenderer(BorrowReturnBookRenderer(isScreenBorrow = isBorrow))



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

        if (isBorrow) {
            view.tvTitle.text = "Danh sách ấn phẩm vừa ghi mượn:"
        } else {
            view.tvTitle.text = "Bạn đọc đang mượn các ấn phẩm:"
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
                ScanQRCode.openScreenQRCode(activity, this, type = ScanQRCode.TypeScan.BookCode)
            }
        }

        view.btnEnter.setOnClickListener {
            if (doubleTouchPrevent.check("btnEnter")) {
                val copyNumber = view.edtSearch.text.toString()
                if (copyNumber.isEmpty()) {
                    activity?.apply {
                        Toasty.error(this, "Bạn chưa nhập mã xếp giá!").show()
                    }
                    return@setOnClickListener
                }
                if (isBorrow) {
                    presenter.borrowBook(copyNumber)
                } else {
                    presenter.returnBook(copyNumber)
                }
                view.edtSearch.setText("")
            }
        }
    }

    override fun showErrorBorrowBook(errorCode: Int) {

        val msgError = when (errorCode) {
            1 -> "Mã xếp giá không tồn tại."
            2 -> "Ấn phẩm chưa sẵn sàng phục vụ (bị khoá hoặc chưa đưa ra lưu thông)."
            3 -> "Ấn phẩm đang được cho mượn."
            4 -> "Ấn phẩm đang được bạn đọc khác đặt chỗ."
            5 -> "Ấn phẩm này thuộc kho mà cán bộ thư viện không có quyền quản lý."
            6 -> "Bạn đọc không được mượn ấn phẩm tại những kho mà nhóm mình không được mượn."
            7 -> "Thẻ đang bị khóa."
            10 -> "Bạn đọc đã quá hạn ngạch."
            12 -> "Thẻ bạn đọc đã hết hạn."
            else -> "Mượn tài liệu về nhà thất bại."
        }
        activity?.let { activity ->
            Toasty.error(activity, msgError).show()
        }
        hideLoading()

    }

    override fun showErrorReturnBook(errorCode: Int) {
        val msgError = when (errorCode) {
            1 -> "Không tồn tại ÐKCB trong danh sách ấn phẩm cho mượn."
            2 -> "Ấn phẩm chưa sẵn sàng phục vụ (bị khoá hoặc chưa đưa ra lưu thông)."
            5 -> "Ấn phẩm này thuộc kho mà cán bộ thư viện không có quyền quản lý."
            6 -> "Bạn đọc không được mượn trả ấn phẩm thuộc những kho mà cán bộ thư viện quản lý."
            else -> "Trả tài liệu thất bại."
        }
        activity?.let { activity ->
            Toasty.error(activity, msgError).show()
        }
        hideLoading()
    }

    override fun returnBookSuccess() {
        activity?.let { activity ->
            Toasty.success(activity, "Trả sách thành công!!!").show()
        }
    }

    override fun borrowBookSuccess(copyNumber: String) {
        activity?.let { activity ->
            Toasty.success(activity, "Mượn sách thành công!!!").show()
        }
        val lstBookBorrow = mutableListOf<BookBorrowNewViewHolderModel>()
        rvController.getItems().forEach { viewModel ->
            if (viewModel is BookBorrowNewViewHolderModel && viewModel.code != copyNumber) {
                lstBookBorrow.add(viewModel)
            }
        }
        rvController.setItems(lstBookBorrow)
        rvController.notifyDataChanged()
        hideLoading()
    }

    override fun showBookBorrow(lstBook: List<BookBorrowNewViewModel>) {
        val lstResult = BookBorrowNewViewHolderModelMapper().mapList(lstBook)
        rvController.setItems(lstResult)
        rvController.notifyDataChanged()
        hideLoading()
    }

    override fun addBookBorrow(book: BookBorrowNewViewModel) {
        val newBookBorrow = BookBorrowNewViewHolderModelMapper().map(book)
        rvController.addItem(0, newBookBorrow)
        rvController.notifyDataChanged()
        hideLoading()
    }

    override fun deleteBookBorrow(book: BookBorrowNewViewModel) {
        view?.let { view ->
            val returnBook = BookBorrowNewViewHolderModelMapper().map(book)
            rvControllerBookReturn.setItems(mutableListOf(returnBook))
            rvControllerBookReturn.notifyDataChanged()
            view.vgAboutBookReturn.visible()
        }
        val lstBookBorrow = mutableListOf<BookBorrowNewViewHolderModel>()
        rvController.getItems().forEach { viewModel ->
            if (viewModel is BookBorrowNewViewHolderModel && viewModel.code != book.code) {
                lstBookBorrow.add(viewModel)
            }
        }
        rvController.setItems(lstBookBorrow)
        rvController.notifyDataChanged()
        hideLoading()
    }

    override fun showErrorGetBooksBorrow() {
        activity?.let { activity ->
            Toasty.error(
                activity,
                "Lấy danh sách bạn đọc còn đang mượn tài liệu sách tại booth thất bại!"
            ).show()
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