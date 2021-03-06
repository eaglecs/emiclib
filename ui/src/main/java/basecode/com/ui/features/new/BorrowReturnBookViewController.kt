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
import basecode.com.domain.model.bus.ComeHomeScreenEventBus
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
        KBus.subscribe<LoginSuccessEventBus>(this){
            if (it.type == LoginSuccessEventBus.Type.BorrowReturnBook){
                handleBook(view)
            }
        }
        KBus.subscribe<ComeHomeScreenEventBus>(this){
            router.popController(this)
        }
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
            borrowOrReturnBook(view)
        }
    }

    private fun initView(view: View) {
        if (isBorrow){
            view.edtSearch.hint = "Nh???p m?? x???p gi?? b???n mu???n m?????n"
        } else {
            view.edtSearch.hint = "Nh???p m?? x???p gi?? b???n mu???n tr???"
        }
        val input = LinearRenderConfigFactory.Input(
            context = view.context,
            orientation = LinearRenderConfigFactory.Orientation.VERTICAL
        )
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvBooks, renderConfig)
        rvController.addViewRenderer(BorrowReturnBookRenderer{ bookCode ->
            presenter.returnBook(bookCode)
        })

        val inputBookReturn = LinearRenderConfigFactory.Input(
            context = view.context,
            orientation = LinearRenderConfigFactory.Orientation.VERTICAL
        )
        val renderConfigBookReturn = LinearRenderConfigFactory(inputBookReturn).create()
        rvControllerBookReturn =
            RecyclerViewController(view.rvBooksBookReturn, renderConfigBookReturn)
        rvControllerBookReturn.addViewRenderer(BorrowReturnBookRenderer())



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
            view.tvTitle.text = "Danh s??ch ???n ph???m v???a ghi m?????n:"
        } else {
            view.tvTitle.text = "B???n ?????c ??ang m?????n c??c ???n ph???m:"
        }
    }


    private fun handleView(view: View) {
        view.vgComeHome.setOnClickListener {
            if (doubleTouchPrevent.check("vgComeHome")) {
                KBus.post(ComeHomeScreenEventBus())
            }
        }
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
                borrowOrReturnBook(view)
            }
        }
    }

    private fun borrowOrReturnBook(view: View) {
        val copyNumber = view.edtSearch.text.toString()
        if (copyNumber.isEmpty()) {
            activity?.apply {
                Toasty.error(this, "B???n ch??a nh???p m?? x???p gi??!").show()
            }
            return
        }
        if (isLogin) {
            handleBook(view)
        } else {
            val bundle =
                LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.BorrowReturnBook.value)
            val loginViewController = LoginViewController(bundle)
            router.pushController(
                RouterTransaction.with(loginViewController)
                    .pushChangeHandler(FadeChangeHandler(false))
            )
        }
    }

    private fun handleBook(view: View) {
        val copyNumber = view.edtSearch.text.toString()
        if (isBorrow) {
            presenter.borrowBook(copyNumber)
        } else {
            presenter.returnBook(copyNumber)
        }
        view.edtSearch.setText("")
        view.edtSearch.clearFocus()
        view.edtSearch.hideKeyboard()
    }

    override fun showErrorBorrowBook(errorCode: Int) {

        val msgError = when (errorCode) {
            1 -> "M?? x???p gi?? kh??ng t???n t???i."
            2 -> "???n ph???m ch??a s???n s??ng ph???c v??? (b??? kho?? ho???c ch??a ????a ra l??u th??ng)."
            3 -> "???n ph???m ??ang ???????c cho m?????n."
            4 -> "???n ph???m ??ang ???????c b???n ?????c kh??c ?????t ch???."
            5 -> "???n ph???m n??y thu???c kho m?? c??n b??? th?? vi???n kh??ng c?? quy???n qu???n l??."
            6 -> "B???n ?????c kh??ng ???????c m?????n ???n ph???m t???i nh???ng kho m?? nh??m m??nh kh??ng ???????c m?????n."
            7 -> "The?? ??ang bi?? kho??a."
            10 -> "B???n ?????c ???? qu?? h???n ng???ch."
            12 -> "Th??? b???n ?????c ???? h???t h???n."
            15 -> "B???n ?????c vui l??ng m?????n s??ch t???i c??c qu???y Booth.(B???n ?????c ??? qu?? xa so v???i c??c tr???m booth)"
            else -> "M?????n t??i li???u v??? nh?? th???t b???i."
        }
        activity?.let { activity ->
            Toasty.error(activity, msgError).show()
        }
        hideLoading()

    }

    override fun showErrorReturnBook(errorCode: Int) {
        val msgError = when (errorCode) {
            1 -> "Kh??ng t???n t???i ??KCB trong danh s??ch ???n ph???m cho m?????n."
            3 -> "T??i li???u n??y ??ang ???????c m?????n b???i ng?????i kh??c."
            2 -> "???n ph???m ch??a s???n s??ng ph???c v??? (b??? kho?? ho???c ch??a ????a ra l??u th??ng)."
            5 -> "???n ph???m n??y thu???c kho m?? c??n b??? th?? vi???n kh??ng c?? quy???n qu???n l??."
            6 -> "B???n ?????c kh??ng ???????c m?????n tr??? ???n ph???m thu???c nh???ng kho m?? c??n b??? th?? vi???n qu???n l??."
            15 -> "B???n ?????c vui l??ng tr??? s??ch t???i c??c qu???y Booth.(B???n ?????c ??? qu?? xa so v???i c??c tr???m booth)"
            else -> "Tr??? t??i li???u th???t b???i."
        }
        activity?.let { activity ->
            Toasty.error(activity, msgError).show()
        }
        hideLoading()
    }

    override fun returnBookSuccess() {
        activity?.let { activity ->
            Toasty.success(activity, "Tr??? s??ch th??nh c??ng!!!").show()
        }
    }

    override fun borrowBookSuccess() {
        activity?.let { activity ->
            Toasty.success(activity, "M?????n s??ch th??nh c??ng!!!").show()
        }
    }

    override fun showBookBorrow(lstBook: List<BookBorrowNewViewModel>) {
        val lstResult = BookBorrowNewViewHolderModelMapper(isBooksBorrow = true).mapList(lstBook)
        rvController.setItems(lstResult)
        rvController.notifyDataChanged()
        hideLoading()
    }

    override fun addBookBorrow(book: BookBorrowNewViewModel) {
        val newBookBorrow = BookBorrowNewViewHolderModelMapper(false).map(book)
        newBookBorrow.isOfBooksBorrow = true
        rvController.addItem(0, newBookBorrow)
        rvController.notifyDataChanged()
        hideLoading()
    }

    override fun deleteBookBorrow(book: BookBorrowNewViewModel) {
        view?.let { view ->
            val returnBook = BookBorrowNewViewHolderModelMapper(false).map(book)
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
                "L???y danh s??ch b???n ?????c c??n ??ang m?????n t??i li???u s??ch t???i booth th???t b???i!"
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