package basecode.com.ui.features.bookdetail

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.os.*
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.extention.valueOrFalse
import basecode.com.presentation.features.bookdetail.BookDetailContract
import basecode.com.presentation.features.books.BookVewModel
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraBoolean
import basecode.com.ui.base.extra.BundleExtraInt
import basecode.com.ui.base.extra.BundleExtraString
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.OnItemRvClickedListener
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.extension.view.gone
import basecode.com.ui.extension.view.visible
import basecode.com.ui.features.books.BooksRenderer
import basecode.com.ui.features.books.BooksViewHolderModel
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.readbook.BookViewActivity
import basecode.com.ui.features.readbook.LocalService
import basecode.com.ui.features.readbook.SkyDatabase
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.GlideUtil
import basecode.com.ui.util.PermissionUtil
import com.bluelinelabs.conductor.RouterTransaction
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.screen_book_detail.view.*
import org.koin.standalone.inject
import android.provider.Settings.System.canWrite
import android.support.annotation.RequiresApi


class BookDetailViewController(bundle: Bundle) : ViewController(bundle), BookDetailContract.View {

    private val presenter: BookDetailContract.Presenter by inject()
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private var isEBook = false
    private var bookId: Int = 0
    private var photo = ""
    private var titleBook = ""
    private var author = ""
    internal var isBound = false

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
        var Bundle.bookId by BundleExtraInt("BooksViewController.bookId")
        var Bundle.titleBook by BundleExtraString("BooksViewController.titleBook")
        var Bundle.author by BundleExtraString("BooksViewController.bookCode")
        var Bundle.photo by BundleExtraString("BooksViewController.photo")
        fun create(isEbook: Boolean, bookId: Int, titleBook: String, author: String, photo: String) = Bundle().apply {
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
            author = options.author.valueOrEmpty()
            titleBook = options.titleBook.valueOrEmpty()
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
        handleOnClick(view)
        presenter.getListBookRelated(bookId)
    }

    private fun doBindService() {
        activity?.let { activity ->
            val intent = Intent(activity, LocalService::class.java)
            activity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
            isBound = true
        }
    }

    private fun initView(view: View) {
        view.tvBookName.text = titleBook
        view.tvBookAuthor.text = author
        activity?.let { activity ->
            GlideUtil.loadImage(photo, view.ivBookDetail, activity)
        }
        if (isEBook) {
            view.tvHandleBook.text = view.context.getString(R.string.text_read_book)
        } else {
            view.tvHandleBook.text = view.context.getString(R.string.text_borrow)
        }
        val input = LinearRenderConfigFactory.Input(context = view.context, orientation = LinearRenderConfigFactory.Orientation.HORIZONTAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvBookRelated, renderConfig)
        activity?.let { activity ->
            rvController.addViewRenderer(BooksRenderer(activity))
        }
        rvController.setOnItemRvClickedListener(object : OnItemRvClickedListener<ViewModel> {
            override fun onItemClicked(view: View, position: Int, dataItem: ViewModel) {
                if (dataItem is BooksViewHolderModel) {
                    targetController?.let { targetController ->
                        val bundle = BookDetailViewController.BundleOptions.create(isEbook = true, bookId = dataItem.id, photo = dataItem.photo, titleBook = dataItem.title,
                                author = dataItem.author)
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
                if (isEBook) {
                    presenter.getBookInfo(bookId)
                } else {
                    presenter.getStatusLogin()
                }
            }
        }
    }

    private val permissionsCode = 1000
    private var pathBook = ""
    @RequiresApi(Build.VERSION_CODES.M)
    override fun getBookInfoSuccess(path: String) {
        pathBook = path
        if (PermissionUtil.hasPermissions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val settingsCanWrite = Settings.System.canWrite(activity)
            if (settingsCanWrite) {
                readBook()
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), permissionsCode)
        }
    }

    private fun readBook() {
        activity?.let { activity ->
            ls?.let {
                if (it.isDownloaded("http://scs.skyepub.net/samples/Doctor.epub")) {
                    val skyDatabase = SkyDatabase(activity)
                    val fetchBookInformations = skyDatabase.fetchBookInformations(0, "")
                    val setting = skyDatabase.fetchSetting()
                    fetchBookInformations?.let { lstBook ->
                        if (lstBook.isNotEmpty()) {
                            val bi = lstBook.first()
                            bi?.let {
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
                    }
                } else {
                    it.startDownload("http://scs.skyepub.net/samples/Doctor.epub", "", titleBook, author)
                }
            }

        }
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
                    readBook()
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
        if (isLogin) {
            if (isEBook) {
                ls?.startDownload("http://scs.skyepub.net/samples/Alice.epub", "", "Alice's Adventures", "Lewis Carroll")
            } else {

            }
        } else {
            router.pushController(RouterTransaction.with(LoginViewController()).pushChangeHandler(FadeChangeHandler(false)))
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
            Toasty.error(activity, activity.getString(R.string.msg_error_get_list_book_related)).show()
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

    inner class SkyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == PROGRESS_ACTION) {
                val bookCode = intent.getIntExtra("BOOKCODE", -1)
                val percent = intent.getDoubleExtra("PERCENT", 0.0)
                //	        	debug("Receiver BookCode:"+bookCode+" "+percent);
                val msg = Message()
                val b = Bundle()
                b.putInt("BOOKCODE", bookCode)
                b.putDouble("PERCENT", percent)
                msg.data = b
                val handler = object : Handler() {
                    override fun handleMessage(msg: Message) {
                        val bookCode = msg.data.getInt("BOOKCODE")
                        val percent = msg.data.getDouble("PERCENT")
                        refreshPieView(bookCode, percent)
                    }
                }
                handler.sendMessage(msg)
            }
        }
    }

    private fun refreshPieView(bookCode: Int, percent: Double) {
        view?.let { view ->
            if (percent < 100) {
                view.pbDownloadEBook.visible()
                view.tvProcessDownloadEBook.visible()
            } else {
                view.pbDownloadEBook.gone()
                view.tvProcessDownloadEBook.gone()
            }
            val percentInt = percent.toInt()
            view.pbDownloadEBook.progress = percentInt
            view.tvProcessDownloadEBook.text = "$percentInt/100"
        }
    }

    private var receiver: SkyReceiver = SkyReceiver()


    override fun onDestroyView(view: View) {
        activity?.unbindService(mConnection)
        activity?.unregisterReceiver(receiver)
        super.onDestroyView(view)
    }
}