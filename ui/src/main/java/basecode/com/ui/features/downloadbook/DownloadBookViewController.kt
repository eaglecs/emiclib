package basecode.com.ui.features.downloadbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.model.dbflow.EBookModel
import basecode.com.presentation.features.downloadboook.DownloadBookContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.listview.view.GridRenderConfigFactory
import basecode.com.ui.base.listview.view.OnItemRvClickedListener
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.extension.view.gone
import basecode.com.ui.extension.view.visible
import basecode.com.ui.features.bookdetail.BookDetailViewController
import basecode.com.ui.features.books.BooksRenderer
import basecode.com.ui.features.books.BooksViewHolderModel
import basecode.com.ui.util.DoubleTouchPrevent
import com.bluelinelabs.conductor.RouterTransaction
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.screen_download_books.view.*
import org.koin.standalone.inject

class DownloadBookViewController : ViewController(null), DownloadBookContract.View {
    private val presenter: DownloadBookContract.Presenter by inject()
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private lateinit var rvController: RecyclerViewController
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_download_books, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        handleView(view)
        loadData()
    }

    private fun loadData() {
        presenter.getListBookDownload()
    }

    private fun initView(view: View) {
        val input = GridRenderConfigFactory.Input(context = view.context, spanCount = 2)
        val renderConfig = GridRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvBookDownload, renderConfig)
        activity?.let { activity ->
            rvController.addViewRenderer(BooksRenderer(activity))
        }
        rvController.setOnItemRvClickedListener(object : OnItemRvClickedListener<ViewModel> {
            override fun onItemClicked(view: View, position: Int, dataItem: ViewModel) {
                if (dataItem is BooksViewHolderModel) {
                    if (doubleTouchPrevent.check("dataItem$position")) {
                        val isEBook = true
                        val bundle = BookDetailViewController.BundleOptions.create(isEbook = isEBook, bookId = dataItem.id, photo = dataItem.photo, titleBook = dataItem.title,
                                author = dataItem.author)
                        router.pushController(RouterTransaction.with(BookDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
                    }
                }
            }
        })
    }

    private fun handleView(view: View) {
        view.ivBackBookDownload.setOnClickListener {
            if (doubleTouchPrevent.check("ivBackBookDownload")) {
                router.popController(this)
            }
        }
        view.vgRefreshBooks.setOnRefreshListener {
            if (doubleTouchPrevent.check("vgRefreshBooks")) {
                loadData()
            }
        }
    }

    override fun getListBookDownloadSuccess(lstBook: List<EBookModel>) {
        view?.let { view ->
            if (lstBook.isEmpty()) {
                view.tvNoBook.visible()
            } else {
                view.tvNoBook.gone()
                val lstResult = mutableListOf<BooksViewHolderModel>()
                lstBook.forEach { book ->
                    val booksViewHolderModel = BooksViewHolderModel(id = book.id, title = book.title, photo = book.photo, author = book.author)
                    lstResult.add(booksViewHolderModel)
                }
                rvController.setItems(lstResult)
                rvController.notifyDataChanged()
            }
        }
    }

    override fun getListBookDownloadFail() {
        activity?.let { activity ->
            Toasty.error(activity, activity.resources.getString(R.string.msg_error_get_list_book_download))
        }
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgRefreshBooks.isRefreshing = false
            view.vgLoadingDownloadBook.show()
        }
    }

    override fun hideLoading() {
        view?.let { view ->
            view.vgLoadingDownloadBook.hide()
        }
    }

    override fun onDestroyView(view: View) {
        presenter.detachView()
        super.onDestroyView(view)
    }
}