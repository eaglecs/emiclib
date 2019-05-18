package basecode.com.ui.features.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.extention.valueOrFalse
import basecode.com.presentation.features.books.BookVewModel
import basecode.com.presentation.features.books.BooksContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraBoolean
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.base.listview.view.GridRenderConfigFactory
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.OnItemRvClickedListener
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.util.DoubleTouchPrevent
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.LoadMoreViewBinder
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.screen_books.view.*
import org.koin.standalone.inject

class BooksViewController(bundle: Bundle) : ViewController(bundle), BooksContract.View {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private var isEBook = false
    private lateinit var rvController: RecyclerViewController
    private val presenter: BooksContract.Presenter by inject()

    object BundleOptions {
        var Bundle.isEBook by BundleExtraBoolean("BooksViewController.isEBook")
        fun create(isEbook: Boolean) = Bundle().apply {
            this.isEBook = isEbook
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            isEBook = options.isEBook.valueOrFalse()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_books, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        handleOnClick(view)
        loadData()
    }

    private fun loadData() {
        if (isEBook) {
            presenter.getListNewEBook(true)
        } else {
            presenter.getListNewBook(true)
        }
    }

    private fun initView(view: View) {
        if (isEBook) {
            view.tvTitleBookDetail.text = view.context.getString(R.string.text_ebook_new)
        } else {
            view.tvTitleBookDetail.text = view.context.getString(R.string.text_new_book)
        }
        val loadMoreConfig = RecyclerViewController.LoadMoreConfig(viewRenderer = LoadMoreViewBinder(R.layout.item_load_more)) {
            if (presenter.isShowLoadMore(isEBook)) {
                rvController.showLoadMore()
                if (isEBook) {
                    presenter.getListNewEBook(false)
                } else {
                    presenter.getListNewBook(false)
                }
            }
        }
        val input = GridRenderConfigFactory.Input(context = view.context, loadMoreConfig = loadMoreConfig, spanCount = 2)
        val renderConfig = GridRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvBookDetail, renderConfig)
        rvController.addViewRenderer(BooksRenderer())
        rvController.setOnItemRvClickedListener(object : OnItemRvClickedListener<ViewModel> {
            override fun onItemClicked(view: View, position: Int, dataItem: ViewModel) {
                if (dataItem is BooksViewHolderModel) {
                    if (doubleTouchPrevent.check("dataItem$position")) {

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
    }

    override fun getListNewBookSuccess(data: List<BookVewModel>, refresh: Boolean) {
        val lstData = mutableListOf<BooksViewHolderModel>()
        data.forEach { book ->
            val booksViewHolderModel = BooksViewHolderModel(id = book.id, photo = book.photo, title = book.name, author = book.author)
            lstData.add(booksViewHolderModel)
        }
        if (refresh) {
            rvController.setItems(lstData)
        } else {
            rvController.addItems(lstData)
        }
        rvController.notifyDataChanged()
        hideLoading()
    }

    override fun showErrorGetListNewBook() {
        activity?.let { activity ->
            Toasty.error(activity, activity.getString(R.string.msg_error_get_list_book)).show()
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

    override fun onDestroyView(view: View) {
        presenter.detachView()
        super.onDestroyView(view)
    }
}