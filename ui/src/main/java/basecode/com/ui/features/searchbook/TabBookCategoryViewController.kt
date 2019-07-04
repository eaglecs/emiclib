package basecode.com.ui.features.searchbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.SearchBookWithKeyEventBus
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.presentation.features.books.BookVewModel
import basecode.com.presentation.features.searchbook.SearchBookContract
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
import basecode.com.ui.features.searchbook.renderer.BookCategoryRenderer
import basecode.com.ui.util.DoubleTouchPrevent
import com.bluelinelabs.conductor.RouterTransaction
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_tab_category_book.view.*
import org.koin.standalone.inject

class TabBookCategoryViewController(bundle: Bundle) : ViewController(bundle), SearchBookContract.View {
    private val presenter: SearchBookContract.Presenter by inject()
    private var categoryId = 0
    private lateinit var rvController: RecyclerViewController
    private var searchText = ""
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()

    constructor(targetController: ViewController, bundle: Bundle) : this(bundle) {
        setTargetController(targetController)
    }

    object BundleOptions {
        var Bundle.categoryId by BundleExtraInt("TabBookCategoryViewController.categoryId")
        fun create(categoryId: Int) = Bundle().apply {
            this.categoryId = categoryId
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            categoryId = options.categoryId.valueOrZero()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_category_book, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        initEventBus()
        handleOnClick(view)
        loadData()
    }

    private fun initEventBus() {
        KBus.subscribe<SearchBookWithKeyEventBus>(this) { searchBookWithKeyEventBus ->
            if (categoryId == searchBookWithKeyEventBus.categoryId) {
                searchText = searchBookWithKeyEventBus.textSearch
                loadData()
            }
        }
    }

    private fun loadData() {
        presenter.searchBook(docType = categoryId, searchText = searchText)
    }

    private fun handleOnClick(view: View) {
        view.vgRefreshCategoryBooks.setOnRefreshListener {
            if (doubleTouchPrevent.check("vgRefreshCategoryBooks")) {
                loadData()
            }
        }
    }

    private fun initView(view: View) {
        val input = LinearRenderConfigFactory.Input(context = view.context, orientation = LinearRenderConfigFactory.Orientation.VERTICAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvCategoryBook, renderConfig)
        rvController.addViewRenderer(BookCategoryRenderer())
        rvController.setOnItemRvClickedListener(object : OnItemRvClickedListener<ViewModel> {
            override fun onItemClicked(view: View, position: Int, dataItem: ViewModel) {
                if (dataItem is BookViewHolderModel) {
                    targetController?.let { targetController ->
                        val bundle = BookDetailViewController.BundleOptions.create(isEbook = categoryId == 4, bookId = dataItem.id, photo = dataItem.photo, author = dataItem.author,
                                titleBook = dataItem.name)
                        targetController.router.pushController(RouterTransaction.with(BookDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
                    }
                }
            }
        })
    }


    override fun searchBookSuccess(lstBookSearch: MutableList<BookVewModel>) {
        view?.let { view ->
            view.vgNoResult.gone()
            val lstBook = mutableListOf<BookViewHolderModel>()
            lstBookSearch.forEach { book ->
                val bookViewHolderModel = BookViewHolderModel(id = book.id, author = book.author, photo = book.photo, name = book.name, publishedYear = book.publishedYear)
                lstBook.add(bookViewHolderModel)
            }
            rvController.setItems(lstBook)
            rvController.notifyDataChanged()
            if (lstBook.isEmpty()) {
                view.vgNoResult.visible()
            }
            hideLoading()
        }
    }

    override fun searchBookFail() {
        activity?.let { activity ->
            Toasty.error(activity, activity.getString(R.string.msg_error_get_list_book)).show()
        }
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgRefreshCategoryBooks.isRefreshing = false
            view.vgLoadingBooks.show()
        }
    }

    override fun hideLoading() {
        view?.let { view ->
            view.vgLoadingBooks.hide()
        }
    }

    override fun onDestroyView(view: View) {
        KBus.unsubscribe(this)
        presenter.detachView()
        super.onDestroyView(view)
    }
}