package basecode.com.ui.features.searchbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.SearchAdvanceBookEventBus
import basecode.com.domain.eventbus.model.SearchBookWithKeyEventBus
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.model.bus.HideKeyboardEventBus
import basecode.com.presentation.features.books.BookViewModel
import basecode.com.presentation.features.searchbook.SearchBookContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraInt
import basecode.com.ui.base.extra.BundleExtraLong
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
import com.github.vivchar.rendererrecyclerviewadapter.binder.LoadMoreViewBinder
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_tab_category_book.view.*
import org.koin.standalone.inject

class TabBookCategoryViewController(bundle: Bundle) : ViewController(bundle),
    SearchBookContract.View {
    private val presenter: SearchBookContract.Presenter by inject()
    private var categoryId = 0
    private lateinit var rvController: RecyclerViewController
    private var searchText = ""
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private var isSearchAdvance = false
    private var modelAdvance: SearchAdvanceBookEventBus? = null
    private var boothId: Long = 0

    constructor(targetController: ViewController, bundle: Bundle) : this(bundle) {
        setTargetController(targetController)
    }

    object BundleOptions {
        var Bundle.categoryId by BundleExtraInt("TabBookCategoryViewController.categoryId")
        var Bundle.boothId by BundleExtraLong("TabBookCategoryViewController.boothId")
        fun create(categoryId: Int, boothId: Long) = Bundle().apply {
            this.categoryId = categoryId
            this.boothId = boothId
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            categoryId = options.categoryId.valueOrZero()
            boothId = options.boothId.valueOrZero()
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
        loadData(isRefresh = true)
    }

    private fun initEventBus() {
        KBus.subscribe<SearchBookWithKeyEventBus>(this) { searchBookWithKeyEventBus ->
            if (categoryId == searchBookWithKeyEventBus.categoryId) {
                isSearchAdvance = false
                searchText = searchBookWithKeyEventBus.textSearch
                boothId = searchBookWithKeyEventBus.boothId
                loadData(isRefresh = true)
            }
        }
        KBus.subscribe<SearchAdvanceBookEventBus>(this) { model ->
            val categoryIdSearchAdvance = model.categoryId
            if (categoryIdSearchAdvance == categoryId) {
                isSearchAdvance = true
                modelAdvance = model
                loadData(isRefresh = true)
            }
        }
    }

    private fun loadData(isRefresh: Boolean) {
        if (isSearchAdvance) {
            modelAdvance?.let { model ->
                presenter.searchBookAdvance(
                    isRefresh = isRefresh,
                    docType = categoryId, language = model.language,
                    author = model.author, title = model.title,
                    searchText = searchText,
                    boothId = boothId
                )
            }
        } else {
            presenter.searchBook(
                isRefresh = isRefresh,
                docType = categoryId,
                searchText = searchText,
                boothId = boothId
            )
        }
    }

    private fun handleOnClick(view: View) {
        view.vgRefreshCategoryBooks.setOnRefreshListener {
            if (doubleTouchPrevent.check("vgRefreshCategoryBooks")) {
                loadData(isRefresh = true)
            }
        }
    }

    private fun initView(view: View) {
        val loadMoreConfig =
            RecyclerViewController.LoadMoreConfig(viewRenderer = LoadMoreViewBinder(R.layout.item_load_more)) {
                if (presenter.isShowLoadMore()) {
                    rvController.showLoadMore()
                    loadData(isRefresh = false)
                }
            }
        val input = LinearRenderConfigFactory.Input(
            context = view.context,
            orientation = LinearRenderConfigFactory.Orientation.VERTICAL,
            loadMoreConfig = loadMoreConfig
        )
        val renderConfig = LinearRenderConfigFactory(input).create()
        view.rvCategoryBook.setItemViewCacheSize(20)
        rvController = RecyclerViewController(view.rvCategoryBook, renderConfig)
        rvController.addViewRenderer(BookCategoryRenderer())
        rvController.setOnItemRvClickedListener(object : OnItemRvClickedListener<ViewModel> {
            override fun onItemClicked(view: View, position: Int, dataItem: ViewModel) {
                KBus.post(HideKeyboardEventBus())
                if (dataItem is BookViewHolderModel) {
                    targetController?.let { targetController ->
                        val type = when (categoryId) {
                            3 -> {
                                BookDetailViewController.BookType.SPEAK_BOOK.value
                            }
                            4 -> {
                                BookDetailViewController.BookType.EBOOK.value
                            }
                            else -> {
                                BookDetailViewController.BookType.BOOK_NORMAL.value
                            }
                        }
                        val bundle = BookDetailViewController.BundleOptions.create(
                            bookType = type,
                            bookId = dataItem.id,
                            photo = dataItem.photo,
                            docType = categoryId,
                            boothId = boothId
                        )
                        targetController.router.pushController(
                            RouterTransaction.with(
                                BookDetailViewController(bundle)
                            ).pushChangeHandler(FadeChangeHandler(false))
                        )
                    }
                }
            }
        })
    }


    override fun searchBookSuccess(lstBookSearch: MutableList<BookViewModel>, isRefresh: Boolean) {
        view?.let { view ->
            view.vgNoResult.gone()
            val lstBook = mutableListOf<BookViewHolderModel>()
            lstBookSearch.forEach { book ->
                val bookViewHolderModel = BookViewHolderModel(
                    id = book.id,
                    name = book.name,
                    publisher = book.publisher,
                    photo = book.photo,
                    author = book.author,
                    publishedYear = book.publishedYear
                )
                lstBook.add(bookViewHolderModel)
            }
            if (isRefresh) {
                rvController.setItems(lstBook)
            } else {
                rvController.addItems(lstBook)
            }
            rvController.notifyDataChanged()
            if (lstBook.isEmpty() && isRefresh) {
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
            view.vgRefreshCategoryBooks.isRefreshing = false
            rvController.hideLoadMore()
            view.vgLoadingBooks.hide()
        }
    }

    override fun onDestroyView(view: View) {
        KBus.unsubscribe(this)
        presenter.detachView()
        super.onDestroyView(view)
    }
}