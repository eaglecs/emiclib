package basecode.com.ui.features.bookdetail

import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.*
import android.provider.Settings
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.LogoutSuccessEventBus
import basecode.com.domain.extention.number.valueOrDefault
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.bus.DownloadFailEventBus
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.domain.model.dbflow.EBookModel
import basecode.com.domain.util.ConstAPI
import basecode.com.presentation.features.bookdetail.BookDetailContract
import basecode.com.presentation.features.books.BookViewModel
import basecode.com.ui.R
import basecode.com.ui.ReadBookActivity
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.screenchangehandler.HorizontalChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraInt
import basecode.com.ui.base.extra.BundleExtraLong
import basecode.com.ui.base.extra.BundleExtraString
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.extension.view.gone
import basecode.com.ui.extension.view.visible
import basecode.com.ui.features.bookdetail.renderer.AudioRenderer
import basecode.com.ui.features.bookdetail.renderer.HeaderAudioRenderer
import basecode.com.ui.features.bookdetail.renderer.BooksRelatedRenderer
import basecode.com.ui.features.bookdetail.renderer.ImageBookRenderer
import basecode.com.ui.features.bookdetail.viewmodel.AudioViewHolderModel
import basecode.com.ui.features.bookdetail.viewmodel.HeaderAudioViewHolderModel
import basecode.com.ui.features.bookdetail.viewmodel.BooksRelatedViewHolderModel
import basecode.com.ui.features.bookdetail.viewmodel.ImageBookViewHolderModel
import basecode.com.ui.features.books.BookRelatedRenderer
import basecode.com.ui.features.books.BooksViewHolderModel
import basecode.com.ui.features.dialog.DialogOneEventViewController
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.readbook.BookViewActivity
import basecode.com.ui.features.readbook.LocalService
import basecode.com.ui.features.readbook.SkyDatabase
import basecode.com.ui.util.*
import com.bluelinelabs.conductor.RouterTransaction
import com.example.jean.jcplayer.general.JcStatus
import com.example.jean.jcplayer.model.JcAudio
import com.example.jean.jcplayer.service.JcPlayerManagerListener
import com.example.jean.jcplayer.view.JcPlayerView
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.skytree.epub.BookInformation
import com.stfalcon.imageviewer.StfalconImageViewer
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_book_info.view.*
import kotlinx.android.synthetic.main.layout_image_info.view.*
import kotlinx.android.synthetic.main.layout_video_info.view.*
import kotlinx.android.synthetic.main.screen_book_detail.view.*
import org.koin.standalone.inject


class BookDetailViewController(bundle: Bundle) : ViewController(bundle), BookDetailContract.View,
    DialogOneEventViewController.ActionEvent {
    private val presenter: BookDetailContract.Presenter by inject()
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private var bookType = BookType.BOOK_NORMAL
    private var bookId: Long = 0
    private var photo = ""
    private var titleBook = ""
    private var linkShare = ""
    private var author = ""
    private var isBound = false
    private var receiver: SkyReceiver = SkyReceiver()
    private var isCheckLogin = true
    private var isLogin = true
    private val permissionsCode = 1000
    private var pathBook = ""
    private lateinit var player: JcPlayerView
    private var numFreeBook = 0
    private val lstPathAudio = mutableListOf<String>()
    private var docType = ConstAPI.DocType.Book.value

    internal var ls: LocalService? = null
    private lateinit var rvController: RecyclerViewController
    private lateinit var rvImageController: RecyclerViewController
    private lateinit var viewer: StfalconImageViewer<String>
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
        var Bundle.bookType by BundleExtraInt("BooksViewController.bookType")
        var Bundle.bookId by BundleExtraLong("BooksViewController.bookId")
        var Bundle.titleBook by BundleExtraString("BooksViewController.titleBook")
        var Bundle.author by BundleExtraString("BooksViewController.bookCode")
        var Bundle.photo by BundleExtraString("BooksViewController.photo")
        var Bundle.docType by BundleExtraInt("BooksViewController.docType")
        fun create(
            bookType: Int = BookType.BOOK_NORMAL.value,
            bookId: Long,
            photo: String,
            docType: Int = ConstAPI.DocType.Book.value
        ) =
            Bundle().apply {
                this.bookType = bookType
                this.bookId = bookId
                this.author = author
                this.titleBook = titleBook
                this.photo = photo
                this.docType = docType
            }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            bookType = when (options.bookType.valueOrZero()) {
                BookType.EBOOK.value -> {
                    BookType.EBOOK
                }
                BookType.SPEAK_BOOK.value -> {
                    BookType.SPEAK_BOOK
                }
                else -> {
                    BookType.BOOK_NORMAL
                }
            }
            bookId = options.bookId.valueOrZero()
            photo = options.photo.valueOrEmpty()
            docType = options.docType.valueOrDefault(ConstAPI.DocType.Book.value)
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
        presenter.getListBookRelated(bookId = bookId, docType = docType)
    }

    private fun initEventBus(view: View) {
        KBus.subscribe<DownloadFailEventBus>(this) { reasonText ->
            view.vgLoadingDownloadBook?.gone()
            activity?.let { activity ->
                isLoadingFinished = true
                Toasty.error(
                    activity,
                    "${activity.resources.getString(R.string.msg_error_download_book)} ${reasonText.reasonText}"
                ).show()
                hideLoading()
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
                if (!isLoadingFinished) {
                    isLoadingFinished = true
                    val eBookModel =
                        EBookModel(id = bookId, title = titleBook, author = author, photo = photo)
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
            val bundle = DialogOneEventViewController.BundleOptions.create(
                title = "",
                msg = msg,
                textEvent = textAction
            )
            router.pushController(
                RouterTransaction.with(
                    DialogOneEventViewController(
                        targetController = this,
                        bundle = bundle
                    )
                )
                    .pushChangeHandler(FadeChangeHandler(false))
            )
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
        player = view.playBookAudio
        GlideUtil.loadImage(photo, view.ivBookDetail)
        when (docType) {
            ConstAPI.DocType.Image.value -> {
                view.vgInfoImage.visible()
                view.vgInfoVideo.gone()
                view.vgInfoBook.gone()
            }
            ConstAPI.DocType.Video.value -> {
                view.vgInfoVideo.visible()
                view.vgInfoBook.gone()
                view.vgInfoImage.gone()
            }
            else -> {
                view.vgInfoVideo.gone()
                view.vgInfoImage.gone()
                view.vgInfoBook.visible()
            }
        }
        when (bookType) {
//            BookType.BOOK_NORMAL -> {
//                view.tvHandleBook.text = view.context.getString(R.string.text_borrow)
//            }
            BookType.EBOOK -> {
                view.tvHandleBook.text = view.context.getString(R.string.text_read_book)
            }
            BookType.SPEAK_BOOK -> {
                view.tvHandleBook.gone()
            }
            else -> {
                view.tvHandleBook.gone()
            }
        }

        val inputImage = LinearRenderConfigFactory.Input(
            context = view.context,
            orientation = LinearRenderConfigFactory.Orientation.HORIZONTAL
        )
        val renderConfigImage = LinearRenderConfigFactory(inputImage).create()
        rvImageController = RecyclerViewController(view.rvImage, renderConfigImage)
        rvImageController.addViewRenderer(ImageBookRenderer{ image, viewPhoto ->
            activity?.let { activity ->
                val imageViews = mutableListOf(viewPhoto)
                val images = mutableListOf(image)
                viewer = StfalconImageViewer.Builder(activity, images, ::loadImage)
                    .withStartPosition(0)
                    .withOverlayView(ImageOverlayView(context = activity, onActionClose = {
                        viewer.close()
                    }))
                    .withHiddenStatusBar(false)
                    .withBackgroundColorResource(R.color.default_color_black_alpha_80)
                    .withTransitionFrom(viewPhoto)
                    .withImageChangeListener { viewer.updateTransitionImage(imageViews.getOrNull(it)) }
                    .show()

            }


//            val bundle = ShowFullImageViewController.BundleOptions.create(image = image)
//            router.pushController(RouterTransaction.with(ShowFullImageViewController(bundle))
//                .popChangeHandler(HorizontalChangeHandler(400, false))
//                .pushChangeHandler(HorizontalChangeHandler(400)))
        })


        val input = LinearRenderConfigFactory.Input(
            context = view.context,
            orientation = LinearRenderConfigFactory.Orientation.VERTICAL
        )
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvBookInfo, renderConfig)
        rvController.addViewRenderer(BooksRelatedRenderer(), BookRelatedRenderer { model ->
            val bundle =
                BundleOptions.create(bookId = model.id, photo = model.photo, docType = docType)
            router.pushController(
                RouterTransaction.with(BookDetailViewController(bundle))
                    .pushChangeHandler(FadeChangeHandler(false))
            )
        })
        rvController.addViewRenderer(HeaderAudioRenderer())
        rvController.addViewRenderer(AudioRenderer { model ->
            if (docType == ConstAPI.DocType.Video.value) {
                view.andExoPlayerView.setSource(model.url)
                view.andExoPlayerView.setPlayWhenReady(true)
            } else {
                player.myPlaylist?.let { myPlaylist ->
                    var audio: JcAudio? = null
                    run loop@{
                        myPlaylist.forEach {
                            if (it.path == model.url) {
                                audio = it
                                return@loop
                            }
                        }
                    }
                    audio?.let {
                        player.playAudio(it)
                    }
                }

            }
            rvController.getItems().forEach { viewModel ->
                if (viewModel is AudioViewHolderModel) {
                    viewModel.isSelected = viewModel.url == model.url
                }
            }
            rvController.notifyDataChanged()
        })
        player.jcPlayerManagerListener = object : JcPlayerManagerListener {
            override fun onCompletedAudio() {
            }

            override fun onContinueAudio(status: JcStatus) {
            }

            override fun onJcpError(throwable: Throwable) {
            }

            override fun onPaused(status: JcStatus) {
            }

            override fun onPlaying(status: JcStatus) {
            }

            override fun onPreparedAudio(status: JcStatus) {
                rvController.getItems().forEach { viewModel ->
                    if (viewModel is AudioViewHolderModel) {
                        viewModel.isSelected = viewModel.url == status.jcAudio.path
                    }
                }
                rvController.notifyDataChanged()
            }

            override fun onTimeChanged(status: JcStatus) {
            }

        }
    }

    private fun loadImage(imageView: ImageView, url: String) {
        activity?.let { activity ->
            imageView.apply {
                GlideUtil.showAvatar(context = activity, url = url, imageView = imageView)
            }

        }
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
                        val bundle =
                            LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.HandleBook.value)
                        val loginViewController = LoginViewController(bundle)
                        router.pushController(
                            RouterTransaction.with(loginViewController)
                                .pushChangeHandler(FadeChangeHandler(false))
                        )
                    }
                } else {
                    presenter.getStatusLogin()
                }
            }
        }
        view.ivShareBookDetail.setOnClickListener {
            if (doubleTouchPrevent.check("ivShareBookDetail")) {
                val title = "${view.context.getString(R.string.title_share_book)} $titleBook"
                ShareUtil.shareViaMedia(
                    subject = "",
                    body = linkShare,
                    title = title,
                    controller = this,
                    requestCode = 1000
                )
            }
        }
    }

    override fun getBookInfoSuccess(
        lstPathResult: List<String>,
        title: String,
        author: String,
        publisher: String,
        publishYear: String,
        shortDescription: String,
        copyNumberResult: String,
        linkShare: String,
        infoBook: String,
        numFreeBookStr: String,
        numFreeBook: Int
    ) {
        lstPathAudio.clear()
        lstPathAudio.addAll(lstPathResult)
        this.linkShare = linkShare
        this.numFreeBook = numFreeBook
        if (linkShare.isNotEmpty()) {
            view?.ivShareBookDetail?.visible()
        }
        if (numFreeBook == 0 && bookType == BookType.BOOK_NORMAL) {
            view?.tvHandleBook?.text = "Đặt chỗ"
        }
        titleBook = title
        this.author = author
        if (lstPathResult.isNotEmpty()) {
            pathBook = lstPathAudio.first()
        }
        view?.let { view ->
            view.tvInfoBook.text = infoBook
            view.tvFreeBook.text = numFreeBookStr
            if (docType == ConstAPI.DocType.Image.value) {
                val lstImage = mutableListOf<ImageBookViewHolderModel>()
                lstPathAudio.forEach { image ->
                    lstImage.add(ImageBookViewHolderModel(image = image))
                }
                rvImageController.setItems(lstImage)
                rvImageController.notifyDataChanged()
            } else if (docType == ConstAPI.DocType.Video.value) {
                if (lstPathAudio.isNotEmpty()) {
                    view.andExoPlayerView.setSource(lstPathAudio.first())
                    val lstAudio = mutableListOf<AudioViewHolderModel>()
                    lstPathAudio.forEachIndexed { index, path ->
                        lstAudio.add(
                            AudioViewHolderModel(
                                id = index + 1,
                                url = path,
                                isSelected = index == 0,
                                title = titleBook
                            )
                        )
                    }

                    rvController.addItem(0, HeaderAudioViewHolderModel())
                    rvController.addItems(1, lstAudio)
                    rvController.notifyDataChanged()
                }
            } else {
                if (bookType == BookType.SPEAK_BOOK) {
                    if (lstPathAudio.isNotEmpty()) {
                        player.visible()
                        val jcAudios = ArrayList<JcAudio>()
                        val lstAudio = mutableListOf<AudioViewHolderModel>()
                        lstPathAudio.forEachIndexed { index, path ->
                            lstAudio.add(
                                AudioViewHolderModel(
                                    id = index + 1,
                                    url = path,
                                    isSelected = index == 0,
                                    title = titleBook
                                )
                            )
                            jcAudios.add(
                                JcAudio.createFromURL(
                                    "$titleBook Phần ${index + 1}",
                                    path
                                )
                            )
                        }
                        player.initPlaylist(jcAudios, null)
                        rvController.addItem(0, HeaderAudioViewHolderModel())
                        rvController.addItems(1, lstAudio)
                        rvController.notifyDataChanged()
                    }
                }
            }
            view.tvBookName.text = title
            view.tvImageName.text = title
            if (author.isNotEmpty()) {
                view.tvBookAuthor.text = author
                view.tvImageAuthor.text = author
            } else {
                view.vgBookAuthor.gone()
                view.tvImageAuthor.gone()
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
            if (shortDescription.isNotEmpty()) {
                view.tvTitleDescription.visible()
            } else {
                view.tvTitleDescription.gone()
            }
            if (copyNumberResult.isNotEmpty()) {
                view.tvCopyNumber.text = copyNumberResult
            } else {
                view.vgCopyNumber.gone()
            }

        }
    }

    private fun readEBook() {
        if (pathBook.isEmpty()) {
            activity?.let {
                hideLoading()
                Toasty.warning(it, it.resources.getString(R.string.msg_warning_read_book)).show()
            }
            return
        }
        activity?.let {
            val intent = Intent(it, ReadBookActivity::class.java)
            intent.putExtra("LinkBook", pathBook)
            intent.putExtra("BookName", titleBook)
            it.startActivity(intent)
        }

//        activity?.let { activity ->
//            ls?.let {
//                if (it.isDownloaded(pathBook)) {
//
//                    openEBook(activity)
//                } else {
//                    showLoading()
//                    isLoadingFinished = false
//                    updateProgress()
//                    it.startDownload(pathBook, "", titleBook, author)
//                }
//            }
//
//        }
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
                        intent.putExtra(
                            "POSITION",
                            (-1.0f).toDouble()
                        ) // 7.x -1 stands for start position for both LTR and RTL book.
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
        percent += when {
            percent < 60 -> 2
            percent < 99 -> 1
            else -> return
        }
        refreshPieView(percent)
        Handler().postDelayed({ updateProgress() }, 1000)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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
//            Toasty.error(activity, msgError).show()
        }
        hideLoading()
    }

    override fun handleAfterCheckLogin(isLogin: Boolean) {
        isCheckLogin = true
        this.isLogin = isLogin
        if (isLogin) {
            handleBook()
        } else {
            val bundle =
                LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.HandleBook.value)
            val loginViewController = LoginViewController(bundle)
            router.pushController(
                RouterTransaction.with(loginViewController)
                    .pushChangeHandler(FadeChangeHandler(false))
            )
        }
    }

    private fun handleBook() {
        when (bookType) {
            BookType.EBOOK -> {
                readEBook()
            }
            else -> {
                if (numFreeBook == 0) {
                    presenter.reserverBook(bookId)
                } else {
                    presenter.reservationBook(bookId)
                }
            }
        }
    }

    override fun reservationBookSuccess() {
        activity?.let { activity ->
            hideLoading()
            Toasty.success(activity, activity.getString(R.string.msg_reservation_success)).show()
        }
    }

    override fun reserverBookSuccess() {
        activity?.let { activity ->
            hideLoading()
            Toasty.success(activity, activity.getString(R.string.msg_reserver_success)).show()
        }
    }

    override fun reserverBookFail(data: Int) {
        activity?.let { activity ->
            hideLoading()
            val msgError = when (data) {
                2 -> {
                    activity.getString(R.string.msg_reserver_fail)
                }
                else -> {
                    activity.getString(R.string.msg_reserver_fail_nomal)
                }
            }

            val title = activity.getString(R.string.TITLE_ERROR)
            val bundle =
                DialogOneEventViewController.BundleOptions.create(title = title, msg = msgError)
            router.pushController(
                RouterTransaction.with(
                    DialogOneEventViewController(
                        targetController = this,
                        bundle = bundle
                    )
                )
                    .pushChangeHandler(FadeChangeHandler(false))
            )
        }
    }

    override fun reservationBookFail(errorCode: Int) {
        activity?.let { activity ->
            hideLoading()
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
            val bundle =
                DialogOneEventViewController.BundleOptions.create(title = title, msg = msgError)
            router.pushController(
                RouterTransaction.with(
                    DialogOneEventViewController(
                        targetController = this,
                        bundle = bundle
                    )
                )
                    .pushChangeHandler(FadeChangeHandler(false))
            )
        }
    }

    override fun getListBookRelatedSuccess(data: List<BookViewModel>) {
        val lstBook = mutableListOf<BooksViewHolderModel>()
        data.forEach { book ->
            val booksViewHolderModel = BooksViewHolderModel(
                id = book.id,
                title = book.name,
                photo = book.photo,
                author = book.author
            )
            lstBook.add(booksViewHolderModel)
        }
        val lstData = mutableListOf<ViewModel>()
        rvController.getItems().forEach { viewModel ->
            if (viewModel !is BooksRelatedViewHolderModel) {
                lstData.add(viewModel)
            }
        }
        lstData.add(BooksRelatedViewHolderModel(lstBook))
//        lstData.addAll(lstBook)
        rvController.setItems(lstData)
        rvController.notifyDataChanged()
    }

    override fun showErrorGetListBookRelated() {
        hideLoading()
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
                Toasty.error(
                    activity,
                    activity.resources.getString(R.string.msg_error_download_book)
                ).show()
            }
            hideLoading()
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
    }

    override fun onDestroyView(view: View) {
        player.kill()
        KBus.unsubscribe(this)
        activity?.unbindService(mConnection)
        activity?.unregisterReceiver(receiver)
        super.onDestroyView(view)
    }

    enum class BookType(val value: Int) {
        BOOK_NORMAL(1), EBOOK(2), SPEAK_BOOK(3)
    }

}

class ProgressDownloadBook(var bookCode: Int, var percentValue: Int)
