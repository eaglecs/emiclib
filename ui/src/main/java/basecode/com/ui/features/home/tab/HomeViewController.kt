package basecode.com.ui.features.home.tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import basecode.com.ui.features.home.viewholder.NewCollectionRecommendViewHolderModel
import basecode.com.ui.features.home.viewholder.NewEBookViewHolderModel
import basecode.com.ui.features.home.viewholder.NewNewsViewHolderModel
import basecode.com.ui.features.newnewsdetail.NewsDetailViewController
import basecode.com.ui.util.DoubleTouchPrevent
import com.bluelinelabs.conductor.RouterTransaction
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_tab_home.view.*
import org.koin.standalone.inject

class HomeViewController() : ViewController(bundle = null), HomeContract.View {

    private val presenter: HomeContract.Presenter by inject()
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private lateinit var rvController: RecyclerViewController

    constructor(targetController: ViewController) : this() {
        setTargetController(targetController)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        handleView(view)
        loadData()
    }

    private fun handleView(view: View) {
        view.vgRefreshInfo.setOnRefreshListener {
            if (doubleTouchPrevent.check("vgRefreshInfo")) {
                loadData()
            }
        }
    }

    private fun initView(view: View) {
        val input = LinearRenderConfigFactory.Input(context = view.context, orientation = LinearRenderConfigFactory.Orientation.VERTICAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvHome, renderConfig)
        activity?.let { activity ->
            rvController.addViewRenderer(NewNewsRenderer(activity) { newsModel ->
                targetController?.let { targetController ->
                    val bundle = NewsDetailViewController.BundleOptions.create(title = newsModel.title, photo = newsModel.picture, content = newsModel.content)
                    targetController.router.pushController(RouterTransaction.with(NewsDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
                }
            })


            rvController.addViewRenderer(NewBookRenderer(onActionClickBook = { bookItem ->
                targetController?.let { targetController ->
                    val bundle = BookDetailViewController.BundleOptions.create(isEbook = true, bookId = bookItem.id, photo = bookItem.coverPicture, titleBook = bookItem.title,
                            author = bookItem.author)
                    targetController.router.pushController(RouterTransaction.with(BookDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
                }
            }, onActionReadMore = {
                targetController?.let { targetController ->
                    val bundle = BooksViewController.BundleOptions.create(isEbook = false)
                    targetController.router.pushController(RouterTransaction.with(BooksViewController(bundle = bundle)).pushChangeHandler(FadeChangeHandler(false)))

                }
            }, context = activity))
            rvController.addViewRenderer(NewEBookRenderer(context = activity, onActionClickBook = { bookItem ->
                targetController?.let { targetController ->
                    val bundle = BookDetailViewController.BundleOptions.create(isEbook = true, bookId = bookItem.id, photo = bookItem.coverPicture, titleBook = bookItem.title,
                            author = bookItem.author)
                    targetController.router.pushController(RouterTransaction.with(BookDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
                }
            }, onActionReadMore = {
                targetController?.let { targetController ->
                    val bundle = BooksViewController.BundleOptions.create(isEbook = true)
                    targetController.router.pushController(RouterTransaction.with(BooksViewController(bundle = bundle)).pushChangeHandler(FadeChangeHandler(false)))

                }
            }))
            rvController.addViewRenderer(CollectionRecommendRenderer(activity) { collectionRecommend ->

            })
        }

    }

    private fun loadData() {
        presenter.getListNewBook()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_home, container, false)
    }

    override fun getListNewEBookSuccess(data: InfoHomeResponse) {
        view?.vgRefreshInfo?.isRefreshing = false
        val newNewsViewHolderModel = NewNewsViewHolderModel(lstNewNews = data.lstNewNews)
        rvController.addItem(newNewsViewHolderModel)
        val newBookViewHolderModel = NewBookViewHolderModel(lstNewBook = data.lstNewBook)
        rvController.addItem(newBookViewHolderModel)
        val newEBookViewHolderModel = NewEBookViewHolderModel(lstNewEBook = data.lstNewEBook)
        rvController.addItem(newEBookViewHolderModel)
        val newCollectionRecommendViewHolderModel = NewCollectionRecommendViewHolderModel(lstCollectionRecommend = data.lstCollectionRecommend)
        rvController.addItem(newCollectionRecommendViewHolderModel)
        rvController.notifyDataChanged()
        hideLoading()
    }

    override fun showErrorGetListNewEbook() {
        view?.let { view ->
            view.vgRefreshInfo.isRefreshing = false
            Toasty.error(view.context, view.context.getString(R.string.msg_error_get_info)).show()
        }
    }

    override fun showLoading() {
        view?.let { view ->
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