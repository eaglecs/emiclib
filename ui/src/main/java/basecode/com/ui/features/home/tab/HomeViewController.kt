package basecode.com.ui.features.home.tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.LogoutSuccessEventBus
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.domain.model.network.response.InfoHomeResponse
import basecode.com.domain.util.ConstAPI
import basecode.com.presentation.features.home.HomeContract
import basecode.com.ui.BuildConfig
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.features.bookdetail.BookDetailViewController
import basecode.com.ui.features.books.BooksViewController
import basecode.com.ui.features.home.renderer.*
import basecode.com.ui.features.home.viewholder.*
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.newnewsdetail.NewsDetailViewController
import basecode.com.ui.features.news.ListNewsViewController
import basecode.com.ui.features.user.UserViewController
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.GlideUtil
import basecode.com.ui.util.ScanQRCode
import com.bluelinelabs.conductor.RouterTransaction
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_tab_home.view.*
import org.koin.standalone.inject

class HomeViewController() : ViewController(bundle = null), HomeContract.View {

    private val presenter: HomeContract.Presenter by inject()
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private var isLogin = false
    private lateinit var rvController: RecyclerViewController

    constructor(targetController: ViewController) : this() {
        setTargetController(targetController)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_home, container, false)
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
            GlideUtil.loadImage(
                url = it.avatar,
                imageView = view.ivLogin,
                holderImage = R.drawable.user_default,
                errorImage = R.drawable.user_default
            )
        }
        KBus.subscribe<LogoutSuccessEventBus>(this) {
            isLogin = false
            view.ivLogin.setImageResource(R.drawable.ic_login)
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
                        targetController.router.pushController(
                            RouterTransaction.with(
                                UserViewController()
                            ).pushChangeHandler(FadeChangeHandler(false))
                        )
                    }
                } else {
                    targetController?.let { targetController ->
                        val bundle =
                            LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.Normal.value)
                        val loginViewController = LoginViewController(bundle)
                        targetController.router.pushController(
                            RouterTransaction.with(loginViewController)
                                .pushChangeHandler(FadeChangeHandler(false))
                        )
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
        if (BuildConfig.USE_DATA_OTHER_APP) {
            if (BuildConfig.USE_DATA_GSL) {
                view.ivComeHome.setImageResource(R.drawable.ic_logo_gsl)
            } else {
                view.ivComeHome.setImageResource(R.drawable.ic_logo_tdn)
            }
        } else {
            view.ivComeHome.setImageResource(R.drawable.ic_logo_new)
        }

        val input = LinearRenderConfigFactory.Input(
            context = view.context,
            orientation = LinearRenderConfigFactory.Orientation.VERTICAL
        )
        val renderConfig = LinearRenderConfigFactory(input).create()
        view.rvHome.setItemViewCacheSize(10)
        rvController = RecyclerViewController(view.rvHome, renderConfig)
        rvController.addViewRenderer(NewNewsRenderer { newsModel ->
            targetController?.let { targetController ->
                val bundle = NewsDetailViewController.BundleOptions.create(
                    title = newsModel.title,
                    photo = newsModel.picture,
                    content = newsModel.content
                )
                targetController.router.pushController(
                    RouterTransaction.with(
                        NewsDetailViewController(bundle)
                    ).pushChangeHandler(FadeChangeHandler(false))
                )
            }
        })


        rvController.addViewRenderer(NewBookRenderer(onActionClickBook = { bookItem ->
            targetController?.let { targetController ->
                val bundle = BookDetailViewController.BundleOptions.create(
                    bookId = bookItem.id,
                    photo = bookItem.coverPicture,
                    docType = ConstAPI.DocType.Book.value
                )
                targetController.router.pushController(
                    RouterTransaction.with(
                        BookDetailViewController(bundle)
                    ).pushChangeHandler(FadeChangeHandler(false))
                )
            }
        }, onActionReadMore = {
            targetController?.let { targetController ->
                val bundle =
                    BooksViewController.BundleOptions.create(docType = ConstAPI.DocType.Book.value)
                targetController.router.pushController(
                    RouterTransaction.with(
                        BooksViewController(
                            bundle = bundle
                        )
                    ).pushChangeHandler(FadeChangeHandler(false))
                )

            }
        }))
        rvController.addViewRenderer(NewEBookRenderer(onActionClickBook = { bookItem ->
            targetController?.let { targetController ->
                val bundle = BookDetailViewController.BundleOptions.create(
                    bookType = BookDetailViewController.BookType.EBOOK.value,
                    bookId = bookItem.id,
                    photo = bookItem.coverPicture,
                    docType = ConstAPI.DocType.Ebook.value
                )
                targetController.router.pushController(
                    RouterTransaction.with(
                        BookDetailViewController(bundle)
                    ).pushChangeHandler(FadeChangeHandler(false))
                )
            }
        }, onActionReadMore = {
            targetController?.let { targetController ->
                val bundle =
                    BooksViewController.BundleOptions.create(docType = ConstAPI.DocType.Ebook.value)
                targetController.router.pushController(
                    RouterTransaction.with(
                        BooksViewController(
                            bundle = bundle
                        )
                    ).pushChangeHandler(FadeChangeHandler(false))
                )

            }
        }))
        rvController.addViewRenderer(NewsBottomRenderer(onActionClickNews = { newsModel ->
            targetController?.let { targetController ->
                val bundle = NewsDetailViewController.BundleOptions.create(
                    title = newsModel.title,
                    photo = newsModel.photo,
                    content = newsModel.content
                )
                targetController.router.pushController(
                    RouterTransaction.with(
                        NewsDetailViewController(bundle)
                    ).pushChangeHandler(FadeChangeHandler(false))
                )
            }
        }, onActionReadMore = {
            targetController?.let { targetController ->
                targetController.router.pushController(
                    RouterTransaction.with(ListNewsViewController())
                        .pushChangeHandler(FadeChangeHandler(false))
                )
            }
        }))

        rvController.addViewRenderer(CollectionRecommendRenderer(onActionClickRecommend = { collectionRecommend ->
            targetController?.let { targetController ->
                val bundle = BookDetailViewController.BundleOptions.create(
                    bookId = collectionRecommend.id.toLong(),
                    photo = collectionRecommend.coverPicture,
                    docType = ConstAPI.DocType.Book.value
                )
                targetController.router.pushController(
                    RouterTransaction.with(
                        BookDetailViewController(bundle)
                    ).pushChangeHandler(FadeChangeHandler(false))
                )
            }
        }, onReadMore = {
            targetController?.let { targetController ->
                val bundle =
                    BooksViewController.BundleOptions.create(docType = ConstAPI.DocType.BookRecommend.value)
                targetController.router.pushController(
                    RouterTransaction.with(
                        BooksViewController(
                            bundle = bundle
                        )
                    ).pushChangeHandler(FadeChangeHandler(false))
                )

            }
        }))

    }

    private fun loadData() {
        presenter.getListNewBook(BuildConfig.USE_DATA_TDN)
    }

    override fun getListNewEBookSuccess(data: InfoHomeResponse) {
        rvController.clear()
        view?.vgRefreshInfo?.isRefreshing = false
        if (data.lstNewNews.isNotEmpty()) {
            val newNewsViewHolderModel = NewNewsViewHolderModel(lstNewNews = data.lstNewNews)
            rvController.addItem(newNewsViewHolderModel)
        }
        if (data.lstNewBook.isNotEmpty()) {
            val newBookViewHolderModel = NewBookViewHolderModel(lstNewBook = data.lstNewBook)
            rvController.addItem(newBookViewHolderModel)
        }

        if (data.lstNewEBook.isNotEmpty()) {
            val newEBookViewHolderModel = NewEBookViewHolderModel(lstNewEBook = data.lstNewEBook)
            rvController.addItem(newEBookViewHolderModel)
        }
        if (data.lstNewNewsBottom.isNotEmpty()) {
            val newNewsBottomViewHolderModel =
                NewNewsBottomViewHolderModel(lstNewNews = data.lstNewNewsBottom)
            rvController.addItem(newNewsBottomViewHolderModel)
        }

        if (data.lstCollectionRecommend.isNotEmpty()) {
            val newCollectionRecommendViewHolderModel =
                NewCollectionRecommendViewHolderModel(lstCollectionRecommend = data.lstCollectionRecommend)
            rvController.addItem(newCollectionRecommendViewHolderModel)
        }

        rvController.notifyDataChanged()
        hideLoading()
    }

    override fun showErrorGetListNewEbook() {
        view?.let { view ->
            view.vgRefreshInfo.isRefreshing = false
            Toasty.error(view.context, view.context.getString(R.string.msg_error_get_info)).show()
            hideLoading()
        }
    }

    override fun handleAfterCheckLogin(isLogin: Boolean, linkAvatar: String) {
        if (isLogin) {
            KBus.post(LoginSuccessEventBus(LoginSuccessEventBus.Type.Normal, linkAvatar))
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