package basecode.com.ui.features.bookdetail

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
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
import basecode.com.ui.features.books.BooksRenderer
import basecode.com.ui.features.books.BooksViewHolderModel
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.readbook.BookViewActivity
import basecode.com.ui.features.readbook.LocalService
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.GlideUtil
import com.bluelinelabs.conductor.RouterTransaction
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.screen_book_detail.view.*
import org.koin.standalone.inject

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
                    activity?.let { activity ->
                        val intent = Intent(activity, BookViewActivity::class.java)
                        activity.startActivity(intent)
                    }
                } else {
                    presenter.getStatusLogin()
                }
            }
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

    internal val PROGRESS_ACTION = "com.skytree.android.intent.action.PROGRESS"

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
            val percentInt = percent.toInt()
            view.pbDownloadEBook.progress = percentInt
            view.tvProcessDownloadEBook.text = "$percentInt/100"
        }
    }

    private var receiver: SkyReceiver? = null

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        val filter = IntentFilter(PROGRESS_ACTION)
        receiver = SkyReceiver()
        activity.registerReceiver(receiver, filter)
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        receiver?.let {
            activity.unregisterReceiver(receiver)
        }
    }


    override fun onDestroyView(view: View) {
        presenter.detachView()
        activity?.let { activity ->
            activity.unbindService(mConnection)
        }
        super.onDestroyView(view)
    }
}