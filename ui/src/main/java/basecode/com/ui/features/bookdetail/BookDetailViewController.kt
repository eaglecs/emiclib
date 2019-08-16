package basecode.com.ui.features.bookdetail

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.*
import android.provider.Settings
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.LogoutSuccessEventBus
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.extention.valueOrFalse
import basecode.com.domain.model.bus.DownloadFailEventBus
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.domain.model.dbflow.EBookModel
import basecode.com.presentation.features.bookdetail.BookDetailContract
import basecode.com.presentation.features.books.BookVewModel
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.*
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.OnItemRvClickedListener
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.extension.view.gone
import basecode.com.ui.extension.view.visible
import basecode.com.ui.features.books.BooksRenderer
import basecode.com.ui.features.books.BooksViewHolderModel
import basecode.com.ui.features.dialog.DialogOneEventViewController
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.readbook.BookViewActivity
import basecode.com.ui.features.readbook.LocalService
import basecode.com.ui.features.readbook.SkyDatabase
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.GlideUtil
import basecode.com.ui.util.PermissionUtil
import basecode.com.ui.util.TemplateUtil
import com.bluelinelabs.conductor.RouterTransaction
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.skytree.epub.BookInformation
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.screen_book_detail.view.*
import org.koin.standalone.inject


class BookDetailViewController(bundle: Bundle) : ViewController(bundle), BookDetailContract.View,
        DialogOneEventViewController.ActionEvent {

    private val presenter: BookDetailContract.Presenter by inject()
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private var isEBook = false
    private var bookId: Long = 0
    private var photo = ""
    private var titleBook = ""
    private var author = ""
    internal var isBound = false
    private var receiver: SkyReceiver = SkyReceiver()
    private var isCheckLogin = false
    private var isLogin = false
    private val permissionsCode = 1000
    private var pathBook = ""

    internal var ls: LocalService? = null
    private lateinit var rvController: RecyclerViewController
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service is LocalService.LocalBinder) {
                ls = service.service
            }
        }

    }

    object BundleOptions {
        var Bundle.isEBook by BundleExtraBoolean("BooksViewController.isEBook")
        var Bundle.bookId by BundleExtraLong("BooksViewController.bookId")
        var Bundle.titleBook by BundleExtraString("BooksViewController.titleBook")
        var Bundle.author by BundleExtraString("BooksViewController.bookCode")
        var Bundle.photo by BundleExtraString("BooksViewController.photo")
        fun create(isEbook: Boolean, bookId: Long, photo: String) = Bundle().apply {
            this.isEBook = isEbook
            this.bookId = bookId
            this.author = author
            this.titleBook = titleBook
            this.photo = photo
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            isEBook = options.isEBook.valueOrFalse()
            bookId = options.bookId.valueOrZero()
            photo = options.photo.valueOrEmpty()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_book_detail, container, false)
    }

    override fun initPostCreateView(view: View) {
        doBindService()
        val filter = IntentFilter(PROGRESS_ACTION)
        activity?.registerReceiver(receiver, filter)
        presenter.attachView(this)
        initView(view)

        initEventBus(view)
        handleOnClick(view)
        presenter.getBookInfo(bookId)
        presenter.getListBookRelated(bookId)
    }

    private fun initEventBus(view: View) {
        KBus.subscribe<DownloadFailEventBus>(this) { reasonText ->
            view.vgLoadingDownloadBook?.gone()
            activity?.let { activity ->
                isLoadingFinished = true
                Toasty.error(activity, "${activity.resources.getString(R.string.msg_error_download_book)} ${reasonText.reasonText}").show()
            }
        }
        KBus.subscribe<LoginSuccessEventBus>(this) {
            isLogin = true
            if (it.type == LoginSuccessEventBus.Type.HandleBook) {
                handleBook()
            }
        }
        KBus.subscribe<LogoutSuccessEventBus>(this) {
            isLogin = false
        }
        KBus.subscribe<ProgressDownloadBook>(this) {
            if (it.percentValue == 100) {
                if(!isLoadingFinished){
                    isLoadingFinished = true
                    val eBookModel = EBookModel(id = bookId, title = titleBook, author = author, photo = photo)
                    presenter.saveBookDownload(eBookModel)
                }
            }
        }
    }

    override fun saveEBookSuccess() {
        view?.let { view ->
            view.vgLoadingDownloadBook.gone()
            val textAction = view.context.getString(R.string.text_read_book)
            val msg = view.context.getString(R.string.msg_download_book_success)
            val bundle = DialogOneEventViewController.BundleOptions.create(title = "", msg = msg, textEvent = textAction)
            router.pushController(RouterTransaction.with(DialogOneEventViewController(targetController = this, bundle = bundle))
                    .pushChangeHandler(FadeChangeHandler(false)))
            hideLoading()
        }
    }

    private fun doBindService() {
        activity?.let { activity ->
            val intent = Intent(activity, LocalService::class.java)
            activity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
            isBound = true
        }
    }

    private fun initView(view: View) {
        GlideUtil.loadImage(photo, view.ivBookDetail)
        if (isEBook) {
            view.tvHandleBook.text = view.context.getString(R.string.text_read_book)
        } else {
            view.tvHandleBook.text = view.context.getString(R.string.text_borrow)
        }
        val input = LinearRenderConfigFactory.Input(context = view.context, orientation = LinearRenderConfigFactory.Orientation.HORIZONTAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvBookRelated, renderConfig)
        activity?.let { activity ->
            rvController.addViewRenderer(BooksRenderer())
        }
        rvController.setOnItemRvClickedListener(object : OnItemRvClickedListener<ViewModel> {
            override fun onItemClicked(view: View, position: Int, dataItem: ViewModel) {
                if (dataItem is BooksViewHolderModel) {
                    targetController?.let { targetController ->
                        val bundle = BundleOptions.create(isEbook = true, bookId = dataItem.id, photo = dataItem.photo)
                        targetController.router.pushController(RouterTransaction.with(BookDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
                    }
                }
            }

        })
    }

    private fun handleOnClick(view: View) {
        view.ivBackBookDetail.setOnClickListener {
            if (doubleTouchPrevent.check("ivBackBookDetail")) {
                router.popController(this)
            }
        }
        view.tvHandleBook.setOnClickListener {
            if (doubleTouchPrevent.check("tvHandleBook")) {
                if (isCheckLogin) {
                    if (isLogin) {
                        handleBook()
                    } else {
                        val bundle = LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.HandleBook.value)
                        val loginViewController = LoginViewController(bundle)
                        router.pushController(RouterTransaction.with(loginViewController).pushChangeHandler(FadeChangeHandler(false)))
                    }
                } else {
                    presenter.getStatusLogin()
                }
            }
        }
    }


    override fun getBookInfoSuccess(path: String, title: String, author: String, publisher: String, publishYear: String, shortDescription: String) {
        pathBook = path
        view?.let { view ->
            view.tvBookName.text = title
            if (author.isNotEmpty()) {
                view.tvBookAuthor.text = author
            } else {
                view.vgBookAuthor.gone()
            }

            if (publisher.isNotEmpty()) {
                val textPublisher = TemplateUtil.createNewSpanny("")
                textPublisher.append("Nhà xuất bản: ", StyleSpan(Typeface.BOLD))
                textPublisher.append(publisher, StyleSpan(Typeface.NORMAL))
                view.tvPublisher.text = textPublisher
            } else {
                view.tvPublisher.gone()
            }

            if (publishYear.isNotEmpty()) {
                view.tvPublishYear.text = publishYear
            } else {
                view.vgPublishYear.gone()
            }

            view.tvDescription.text = shortDescription

        }
    }

    private fun readEBook() {
        if (pathBook.isEmpty()) {
            activity?.let {
                Toasty.warning(it, it.resources.getString(R.string.msg_warning_link_download_empty)).show()
            }
            return
        }
        activity?.let { activity ->
            ls?.let {
                if (it.isDownloaded(pathBook)) {
                    openEBook(activity)
                } else {
                    showLoading()
                    isLoadingFinished = false
                    updateProgress()
                    it.startDownload(pathBook, "", titleBook, author)
                }
            }

        }
    }

    private fun openEBook(activity: Activity) {

        val isWriteSetting = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(activity)
        } else {
            true
        }
        if (isWriteSetting) {
            val skyDatabase = SkyDatabase(activity)
            val fetchBookInformations = skyDatabase.fetchBookInformations(0, "")
            val setting = skyDatabase.fetchSetting()
            fetchBookInformations?.let { lstBook ->
                var bi: BookInformation? = null

                lstBook.forEach { book ->
                    if (book.url == pathBook) {
                        bi = book
                    }
                }

                bi?.let { bi ->
                    if (!bi.isDownloaded) return
                    val intent = Intent(activity, BookViewActivity::class.java)
                    intent.putExtra("BOOKCODE", bi.bookCode)
                    intent.putExtra("TITLE", bi.title)
                    intent.putExtra("AUTHOR", bi.creator)
                    intent.putExtra("BOOKNAME", bi.fileName)
                    if (bi.position < 0.0f) {
                        intent.putExtra("POSITION", (-1.0f).toDouble()) // 7.x -1 stands for start position for both LTR and RTL book.
                    } else {
                        intent.putExtra("POSITION", bi.position)
                    }
                    intent.putExtra("THEMEINDEX", setting.theme)
                    intent.putExtra("DOUBLEPAGED", setting.doublePaged)
                    intent.putExtra("transitionType", setting.transitionType)
                    intent.putExtra("GLOBALPAGINATION", setting.globalPagination)
                    intent.putExtra("RTL", bi.isRTL)
                    intent.putExtra("VERTICALWRITING", bi.isVerticalWriting)

                    intent.putExtra("SPREAD", bi.spread)
                    intent.putExtra("ORIENTATION", bi.orientation)

                    startActivity(intent)
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                startActivity(intent)
            }
        }
    }

    var percent = 0
    var isLoadingFinished = false

    private fun updateProgress() {
        view?.let { view ->
            view.vgLoadingDownloadBook.visible()
        }
        if (isLoadingFinished) {
            view?.vgLoadingDownloadBook?.gone()
            return
        }
        percent += if (percent < 60) {
            2
        } else if (percent < 99) {
            1
        } else {
            return
        }
        refreshPieView(percent)
        Handler().postDelayed({ updateProgress() }, 1000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionsCode) {
            if (permissions.isNotEmpty() && grantResults.isNotEmpty() && permissions.size == grantResults.size) {
                var isGrantAll = true
                loop@ for (i in 0 until grantResults.size) {
                    val grantResult = grantResults[i]
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        isGrantAll = false
                        break@loop
                    }
                }
                if (isGrantAll) {
                    readEBook()
                }
            }
        }

    }

    override fun getBookInfoFail(msgError: String) {
        activity?.let { activity ->
            Toasty.error(activity, msgError).show()
        }
    }

    override fun handleAfterCheckLogin(isLogin: Boolean) {
        isCheckLogin = true
        this.isLogin = isLogin
        if (isLogin) {
            handleBook()
        } else {
            val bundle = LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.HandleBook.value)
            val loginViewController = LoginViewController(bundle)
            router.pushController(RouterTransaction.with(loginViewController).pushChangeHandler(FadeChangeHandler(false)))
        }
    }

    private fun handleBook() {
        if (isEBook) {
            activity?.let {
                if (PermissionUtil.hasPermissions(it, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    readEBook()
                } else {
                    requestPermissions(PermissionUtil.readWriteExternalPermissions, permissionsCode)
                }
            }
        } else {
            presenter.reservationBook(bookId)
        }
    }

    override fun reservationBookSuccess() {
        activity?.let { activity ->
            Toasty.success(activity, activity.getString(R.string.msg_reservation_success)).show()
        }
    }

    override fun reservationBookFail(errorCode: Int) {
        activity?.let { activity ->

            val msgError = when (errorCode) {
                1 -> {
                    activity.getString(R.string.msg_reservation_fail_number_card)
                }
                2 -> {
                    activity.getString(R.string.msg_reservation_fail)
                }
                3 -> {
                    activity.getString(R.string.msg_reservation_fail_request_exist)
                }
                4 -> {
                    activity.getString(R.string.msg_reservation_fail_except)
                }
                5 -> {
                    activity.getString(R.string.msg_reservation_fail_full)
                }
                6 -> {
                    activity.getString(R.string.msg_reservation_fail_not_Invalid)
                }
                7 -> {
                    activity.getString(R.string.msg_reservation_fail_free)
                }
                else -> {
                    activity.getString(R.string.msg_reservation_fail_nomal)
                }
            }

            val title = activity.getString(R.string.TITLE_ERROR)
            val bundle = DialogOneEventViewController.BundleOptions.create(title = title, msg = msgError)
            router.pushController(RouterTransaction.with(DialogOneEventViewController(targetController = this, bundle = bundle))
                    .pushChangeHandler(FadeChangeHandler(false)))
        }
    }

    override fun getListBookRelatedSuccess(data: List<BookVewModel>) {
        val lstBook = mutableListOf<BooksViewHolderModel>()
        data.forEach { book ->
            val booksViewHolderModel = BooksViewHolderModel(id = book.id, title = book.name, photo = book.photo, author = book.author)
            lstBook.add(booksViewHolderModel)
        }
        rvController.setItems(lstBook)
        rvController.notifyDataChanged()
    }

    override fun showErrorGetListBookRelated() {
        activity?.let { activity ->
            //            Toasty.error(activity, activity.getString(R.string.msg_error_get_list_book_related)).show()
        }
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgLoadingBookDetail.show()
        }
    }

    override fun hideLoading() {
        view?.let { view ->
            view.vgLoadingBookDetail.hide()

        }
    }

    private val PROGRESS_ACTION = "com.skytree.android.intent.action.PROGRESS"
    private val downloadFail = "com.skytree.android.intent.action.FAIL"
    val handlerDownloadFail = object : Handler() {
        override fun handleMessage(msg: Message) {
            view?.vgLoadingDownloadBook?.gone()
            activity?.let { activity ->
                Toasty.error(activity, activity.resources.getString(R.string.msg_error_download_book)).show()
            }
        }
    }

    inner class SkyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == PROGRESS_ACTION) {
                val bookCode = intent.getIntExtra("BOOKCODE", -1)
                val percent = intent.getDoubleExtra("PERCENT", 0.0)
                val percentValue = (percent * 100).toInt()
                KBus.post(ProgressDownloadBook(bookCode, percentValue))
            } else if (intent.action == downloadFail) {
                val msg = Message()
                handlerDownloadFail.sendMessage(msg)
            }
        }
    }

    private fun refreshPieView(percent: Int) {
        view?.let { view ->
            view.pbDownloadEBook.progress = percent
            view.tvProcessDownloadEBook.text = "$percent/100"
        }
    }

    override fun handleBack(): Boolean {
        return false
    }

    override fun onResultAfterHandleDialog() {
        if(isEBook){
            handleBook()
        }
    }

    override fun onDestroyView(view: View) {
        KBus.unsubscribe(this)
        activity?.unbindService(mConnection)
        activity?.unregisterReceiver(receiver)
        super.onDestroyView(view)
    }
}

class ProgressDownloadBook(var bookCode: Int, var percentValue: Int)
