package basecode.com.ui.features.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.network.BookType
import basecode.com.presentation.features.books.BookVewModel
import basecode.com.presentation.features.books.BooksContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraInt
import basecode.com.ui.base.extra.BundleExtraString
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.base.listview.view.GridRenderConfigFactory
import basecode.com.ui.base.listview.view.OnItemRvClickedListener
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.features.bookdetail.BookDetailViewController
import basecode.com.ui.util.DoubleTouchPrevent
import com.bluelinelabs.conductor.RouterTransaction
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.LoadMoreViewBinder
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.screen_books.view.*
import org.koin.standalone.inject

class BooksViewController(bundle: Bundle) : ViewController(bundle), BooksContract.View {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private lateinit var rvController: RecyclerViewController
    private var bookType: Int = BookType.BOOK.value
    private val presenter: BooksContract.Presenter by inject()
    private var collectionId = 0
    private var collectionName = ""

    object BundleOptions {
        var Bundle.bookType by BundleExtraInt("BooksViewController.bookType")
        var Bundle.collectionId by BundleExtraInt("BooksViewController.collectionId")
        var Bundle.collectionName by BundleExtraString("BooksViewController.collectionName")
        fun create(bookType: Int, collectionId: Int = 0, collectionName: String = "") = Bundle().apply {
            this.bookType = bookType
            this.collectionId = collectionId
            this.collectionName = collectionName
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            bookType = options.bookType.valueOrZero()
            collectionId = options.collectionId.valueOrZero()
            collectionName = options.collectionName.valueOrEmpty()
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
        when (bookType) {
            BookType.BOOK.value -> {
                presenter.getListNewBook(true)
            }
            BookType.E_BOOK.value -> {
                presenter.getListNewEBook(true)
            }
            BookType.COLLECTION.value -> {
                presenter.getItemInCollectionRecomand(isRefresh = true, collectionId = collectionId)
            }
        }
    }

    private fun initView(view: View) {

        val textTitle = when (bookType) {
            BookType.BOOK.value -> {
                view.context.getString(R.string.text_new_book)
            }
            BookType.E_BOOK.value -> {
                view.context.getString(R.string.text_ebook_new)
            }
            else -> {
                collectionName
            }
        }
        view.tvTitleBookDetail.text = textTitle

        val loadMoreConfig = RecyclerViewController.LoadMoreConfig(viewRenderer = LoadMoreViewBinder(R.layout.item_load_more)) {
            if (presenter.isShowLoadMore(bookType = bookType)) {
                rvController.showLoadMore()

                when (bookType) {
                    BookType.BOOK.value -> {
                        presenter.getListNewBook(false)
                    }
                    BookType.E_BOOK.value -> {
                        presenter.getListNewEBook(false)
                    }
                    BookType.COLLECTION.value -> {
                        presenter.getItemInCollectionRecomand(isRefresh = false, collectionId = collectionId)
                    }
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
                        val isEBook = bookType == BookType.E_BOOK.value
                        val bundle = BookDetailViewController.BundleOptions.create(isEbook = isEBook, bookId = dataItem.id, photo = dataItem.photo)
                        router.pushController(RouterTransaction.with(BookDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
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
        view.vgRefreshBooks.setOnRefreshListener {
            if (doubleTouchPrevent.check("vgRefreshBooks")) {
                loadData()
            }
        }

    }

    override fun getListNewBookSuccess(data: List<BookVewModel>, refresh: Boolean) {
        view?.vgRefreshBooks?.isRefreshing = false
        rvController.hideLoadMore()
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
        view?.vgRefreshBooks?.isRefreshing = false
        rvController.hideLoadMore()
        activity?.let { activity ->
//            Toasty.error(activity, activity.getString(R.string.msg_error_get_list_book)).show()
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