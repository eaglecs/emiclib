package basecode.com.ui.features.borrowbook.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.presentation.features.bookborrow.BookBorrowContract
import basecode.com.presentation.features.books.BookVewModel
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraInt
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.OnItemRvClickedListener
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.extension.view.gone
import basecode.com.ui.extension.view.visible
import basecode.com.ui.features.bookdetail.BookDetailViewController
import basecode.com.ui.features.searchbook.BookViewHolderModel
import basecode.com.ui.features.searchbook.renderer.BookCategoryRenderer
import basecode.com.ui.util.DoubleTouchPrevent
import com.bluelinelabs.conductor.RouterTransaction
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_tab_borrow_book.view.*
import org.koin.standalone.inject

class TabBookBorrowViewController(bundle: Bundle) : ViewController(bundle), BookBorrowContract.View {

    private var position = 0
    private lateinit var rvController: RecyclerViewController
    private val presenter: BookBorrowContract.Presenter by inject()
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()

    constructor(targetController: ViewController, bundle: Bundle) : this(bundle) {
        setTargetController(targetController)
    }

    object BundleOptions {
        var Bundle.position by BundleExtraInt("position")
        fun create(position: Int) = Bundle().apply {
            this.position = position
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            position = options.position.valueOrZero()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_borrow_book, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        handleView(view)
        loadData()
    }

    private fun handleView(view: View) {
        view.vgRefreshBooksBorrow.setOnRefreshListener {
            if (doubleTouchPrevent.check("vgRefreshCategoryBooks")) {
                loadData()
            }
        }
    }

    private fun loadData() {
        presenter.getListBook(isRefresh = true, isBorrowing = position == 0)
    }

    private fun initView(view: View) {
        val input = LinearRenderConfigFactory.Input(context = view.context, orientation = LinearRenderConfigFactory.Orientation.VERTICAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        view.rvBookBorrow.setItemViewCacheSize(20)
        rvController = RecyclerViewController(view.rvBookBorrow, renderConfig)
        rvController.addViewRenderer(BookCategoryRenderer())
        rvController.setOnItemRvClickedListener(object : OnItemRvClickedListener<ViewModel> {
            override fun onItemClicked(view: View, position: Int, dataItem: ViewModel) {
                if (dataItem is BookViewHolderModel) {
                    targetController?.let { targetController ->
                        val bundle = BookDetailViewController.BundleOptions.create(isEbook = false, bookId = dataItem.id, photo = dataItem.photo, author = dataItem.author,
                                titleBook = dataItem.name)
                        targetController.router.pushController(RouterTransaction.with(BookDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
                    }
                }
            }
        })
    }

    override fun getListBookSuccess(lstBook: List<BookVewModel>) {
        val lstBook = mutableListOf<BookViewHolderModel>()
        lstBook.forEach { book ->
            val bookViewHolderModel = BookViewHolderModel(id = book.id, author = book.author, photo = book.photo, name = book.name, publishedYear = book.publishedYear)
            lstBook.add(bookViewHolderModel)
        }
        if (lstBook.isEmpty()) {
            view?.tvNoBook?.visible()
        } else {
            view?.tvNoBook?.gone()
            rvController.setItems(lstBook)
            rvController.notifyDataChanged()
        }
    }

    override fun getListBookFail() {
        activity?.let { activity ->
            Toasty.error(activity, activity.resources.getString(R.string.msg_error_get_list_book_borrow)).show()
        }
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgLoadingBorrowBook.hide()
        }
    }

    override fun hideLoading() {
        view?.let { view ->
            view.vgLoadingBorrowBook.hide()
        }
    }

    override fun onDestroyView(view: View) {
        presenter.detachView()
        super.onDestroyView(view)
    }
}