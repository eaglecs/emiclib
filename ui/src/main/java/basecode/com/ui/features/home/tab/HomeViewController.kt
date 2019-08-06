package basecode.com.ui.features.home.tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.LogoutSuccessEventBus
import basecode.com.domain.eventbus.model.ResultScanQRCodeEventBus
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.domain.model.network.BookType
import basecode.com.domain.model.network.response.InfoHomeResponse
import basecode.com.presentation.features.home.HomeContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.features.bookdetail.BookDetailViewController
import basecode.com.ui.features.books.BooksViewController
import basecode.com.ui.features.home.renderer.CollectionRecommendRenderer
import basecode.com.ui.features.home.renderer.NewBookRenderer
import basecode.com.ui.features.home.renderer.NewEBookRenderer
import basecode.com.ui.features.home.renderer.NewNewsRenderer
import basecode.com.ui.features.home.viewholder.NewBookViewHolderModel
import basecode.com.ui.features.home.viewholder.NewEBookViewHolderModel
import basecode.com.ui.features.home.viewholder.NewNewsViewHolderModel
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.newnewsdetail.NewsDetailViewController
import basecode.com.ui.features.user.UserViewController
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.ScanQRCode
import com.bluelinelabs.conductor.RouterTransaction
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_tab_home.view.*
import org.koin.standalone.inject

class HomeViewController() : ViewController(bundle = null), HomeContract.View {

    private val presenter: HomeContract.Presenter by inject()
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private lateinit var rvController: RecyclerViewController
    private var isLogin = false

    constructor(targetController: ViewController) : this() {
        setTargetController(targetController)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        initEventBus(view)
        handleView(view)
        loadData()
    }

    private fun initEventBus(view: View) {
        KBus.subscribe<LoginSuccessEventBus>(this) {
            isLogin = true
            view.ivLogin.setImageResource(R.drawable.ic_person)
        }
        KBus.subscribe<LogoutSuccessEventBus>(this) {
            isLogin = false
            view.ivLogin.setImageResource(R.drawable.ic_login)
        }
        KBus.subscribe<ResultScanQRCodeEventBus>(this) {
            val contentQRCode = it.contentQRCode
            val bookInfo = contentQRCode.replace("{", "").replace("}", "").split(":")
            if (bookInfo.size == 2) {
                val bookId = bookInfo.first().trim().toLong()
                targetController?.let { targetController ->
                    val bundle = BookDetailViewController.BundleOptions.create(isEbook = false, bookId = bookId, photo = "")
                    targetController.router.pushController(RouterTransaction.with(BookDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
                }
            }
        }
    }

    private fun handleView(view: View) {
        view.vgRefreshInfo.setOnRefreshListener {
            if (doubleTouchPrevent.check("vgRefreshInfo")) {
                loadData()
            }
        }
        view.ivLogin.setOnClickListener {
            if (doubleTouchPrevent.check("ivLogin")) {
                if (isLogin) {
                    targetController?.let { targetController ->
                        targetController.router.pushController(RouterTransaction.with(UserViewController()).pushChangeHandler(FadeChangeHandler(false)))
                    }
                } else {
                    targetController?.let { targetController ->
                        val bundle = LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.Normal.value)
                        val loginViewController = LoginViewController(bundle)
                        targetController.router.pushController(RouterTransaction.with(loginViewController).pushChangeHandler(FadeChangeHandler(false)))
                    }
                }
            }
        }

        view.ivScanQRCode.setOnClickListener {
            if (doubleTouchPrevent.check("ivScanQRCode")) {
                ScanQRCode.openScreenQRCode(activity, this)
            }
        }
    }

    private fun initView(view: View) {
        val input = LinearRenderConfigFactory.Input(context = view.context, orientation = LinearRenderConfigFactory.Orientation.VERTICAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        view.rvHome.setItemViewCacheSize(10)
        rvController = RecyclerViewController(view.rvHome, renderConfig)
        rvController.addViewRenderer(NewNewsRenderer() { newsModel ->
            targetController?.let { targetController ->
                val bundle = NewsDetailViewController.BundleOptions.create(title = newsModel.title, photo = newsModel.picture, content = newsModel.content)
                targetController.router.pushController(RouterTransaction.with(NewsDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
            }
        })


        rvController.addViewRenderer(NewBookRenderer(onActionClickBook = { bookItem ->
            targetController?.let { targetController ->
                val bundle = BookDetailViewController.BundleOptions.create(isEbook = false, bookId = bookItem.id, photo = bookItem.coverPicture)
                targetController.router.pushController(RouterTransaction.with(BookDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
            }
        }, onActionReadMore = {
            targetController?.let { targetController ->
                val bundle = BooksViewController.BundleOptions.create(bookType = BookType.BOOK.value)
                targetController.router.pushController(RouterTransaction.with(BooksViewController(bundle = bundle)).pushChangeHandler(FadeChangeHandler(false)))

            }
        }))
        rvController.addViewRenderer(NewEBookRenderer(onActionClickBook = { bookItem ->
            targetController?.let { targetController ->
                val bundle = BookDetailViewController.BundleOptions.create(isEbook = true, bookId = bookItem.id, photo = bookItem.coverPicture)
                targetController.router.pushController(RouterTransaction.with(BookDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
            }
        }, onActionReadMore = {
            targetController?.let { targetController ->
                val bundle = BooksViewController.BundleOptions.create(bookType = BookType.E_BOOK.value)
                targetController.router.pushController(RouterTransaction.with(BooksViewController(bundle = bundle)).pushChangeHandler(FadeChangeHandler(false)))

            }
        }))
        rvController.addViewRenderer(CollectionRecommendRenderer { collectionRecommend ->
            targetController?.let { targetController ->
                val title = collectionRecommend.title
                val id = collectionRecommend.id
                val bundle = BooksViewController.BundleOptions.create(bookType = BookType.COLLECTION.value, collectionId = id, collectionName = title)
                targetController.router.pushController(RouterTransaction.with(BooksViewController(bundle = bundle)).pushChangeHandler(FadeChangeHandler(false)))
            }
        })

    }

    private fun loadData() {
        presenter.getListNewBook()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_home, container, false)
    }

    override fun getListNewEBookSuccess(data: InfoHomeResponse) {
        rvController.clear()
        view?.vgRefreshInfo?.isRefreshing = false
        val newNewsViewHolderModel = NewNewsViewHolderModel(lstNewNews = data.lstNewNews)
        rvController.addItem(newNewsViewHolderModel)
        val newBookViewHolderModel = NewBookViewHolderModel(lstNewBook = data.lstNewBook)
        rvController.addItem(newBookViewHolderModel)
        val newEBookViewHolderModel = NewEBookViewHolderModel(lstNewEBook = data.lstNewEBook)
        rvController.addItem(newEBookViewHolderModel)
//        val newCollectionRecommendViewHolderModel = NewCollectionRecommendViewHolderModel(lstCollectionRecommend = data.lstCollectionRecommend)
//        rvController.addItem(newCollectionRecommendViewHolderModel)
        rvController.notifyDataChanged()
        hideLoading()
    }

    override fun showErrorGetListNewEbook() {
        view?.let { view ->
            view.vgRefreshInfo.isRefreshing = false
            Toasty.error(view.context, view.context.getString(R.string.msg_error_get_info)).show()
        }
    }

    override fun handleAfterCheckLogin(isLogin: Boolean) {
        if (isLogin) {
            KBus.post(LoginSuccessEventBus(LoginSuccessEventBus.Type.Normal))
        } else {
            KBus.post(LogoutSuccessEventBus())
        }
        this.isLogin = isLogin
        view?.let { view ->
            if (isLogin) {
                view.ivLogin.setImageResource(R.drawable.ic_person)
            } else {
                view.ivLogin.setImageResource(R.drawable.ic_login)
            }
        }
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgRefreshInfo.isRefreshing = false
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