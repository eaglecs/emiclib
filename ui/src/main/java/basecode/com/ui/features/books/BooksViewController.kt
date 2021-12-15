package basecode.com.ui.features.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.network.BookType
import basecode.com.domain.util.ConstAPI
import basecode.com.presentation.features.books.BookViewModel
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
import kotlinx.android.synthetic.main.screen_books.view.*
import org.koin.standalone.inject

class BooksViewController(bundle: Bundle) : ViewController(bundle), BooksContract.View {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private lateinit var rvController: RecyclerViewController
    private var bookType: Int = BookType.BOOK.value
    private val presenter: BooksContract.Presenter by inject()
    private var collectionId = 0
    private var collectionName = ""
    private var docType = ConstAPI.DocType.Book.value

    object BundleOptions {
        var Bundle.docType by BundleExtraInt("BooksViewController.docType")
        var Bundle.collectionId by BundleExtraInt("BooksViewController.collectionId")
        var Bundle.collectionName by BundleExtraString("BooksViewController.collectionName")
        fun create(docType: Int, collectionId: Int = 0, collectionName: String = "") =
            Bundle().apply {
                this.docType = docType
                this.collectionId = collectionId
                this.collectionName = collectionName
            }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            docType = options.docType.valueOrZero()
            collectionId = options.collectionId.valueOrZero()
            collectionName = options.collectionName.valueOrEmpty()
            bookType = if (docType == ConstAPI.DocType.Ebook.value){
                BookType.E_BOOK.value
            } else if (docType == ConstAPI.DocType.Book.value){
                BookType.BOOK.value
            } else {
                BookType.BOOK_RECOMMEND.value
            }
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
        when (docType) {
            ConstAPI.DocType.Ebook.value -> {
                presenter.getListNewEBook(true)
            }
            ConstAPI.DocType.Book.value -> {
                presenter.getListNewBook(true)
            }
            else -> {
                presenter.getListBookRecommend(isRefresh = false)
            }
        }

//        when (bookType) {
//            BookType.BOOK.value -> {
//                presenter.getListNewBook(true)
//            }
//            BookType.E_BOOK.value -> {
//                presenter.getListNewEBook(true)
//            }
//            BookType.COLLECTION.value -> {
//                presenter.getItemInCollectionRecomand(isRefresh = true, collectionId = collectionId)
//            }
//            BookType.BOOK_RECOMMEND.value -> {
//                presenter.getListBookRecommend(isRefresh = false)
//            }
//        }
    }

    private fun initView(view: View) {

        val textTitle = when (bookType) {
            BookType.BOOK.value -> {
                view.context.getString(R.string.text_new_book)
            }
            BookType.E_BOOK.value -> {
                view.context.getString(R.string.text_ebook_new)
            }
            BookType.BOOK_RECOMMEND.value -> {
                view.context.getString(R.string.text_collection_recommend)
            }
            else -> {
                collectionName
            }
        }
        view.tvTitleBookDetail.text = textTitle

        val loadMoreConfig =
            RecyclerViewController.LoadMoreConfig(viewRenderer = LoadMoreViewBinder(R.layout.item_load_more)) {
                if (presenter.isShowLoadMore(bookType = bookType)) {
                    rvController.showLoadMore()

                    when (bookType) {
                        BookType.BOOK.value -> {
                            presenter.getListNewBook(false)
                        }
                        BookType.E_BOOK.value -> {
                            presenter.getListNewEBook(false)
                        }
                        BookType.BOOK_RECOMMEND.value -> {
                            presenter.getListBookRecommend(false)
                        }
                        BookType.COLLECTION.value -> {
                            presenter.getItemInCollectionRecomand(
                                isRefresh = false,
                                collectionId = collectionId
                            )
                        }
                    }
                }
            }
        val input = GridRenderConfigFactory.Input(
            context = view.context,
            loadMoreConfig = loadMoreConfig,
            spanCount = 2
        )
        val renderConfig = GridRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvBookDetail, renderConfig)
        rvController.addViewRenderer(BooksRenderer())
        rvController.setOnItemRvClickedListener(object : OnItemRvClickedListener<ViewModel> {
            override fun onItemClicked(view: View, position: Int, dataItem: ViewModel) {
                if (dataItem is BooksViewHolderModel) {
                    if (doubleTouchPrevent.check("dataItem$position")) {
                        val isEBook = bookType == BookType.E_BOOK.value
                        val type = if (isEBook) {
                            BookDetailViewController.BookType.EBOOK.value
                        } else {
                            BookDetailViewController.BookType.BOOK_NORMAL.value
                        }
                        val bundle = BookDetailViewController.BundleOptions.create(
                            bookId = dataItem.id,
                            photo = dataItem.photo,
                            docType = docType
                        )
                        router.pushController(
                            RouterTransaction.with(BookDetailViewController(bundle))
                                .pushChangeHandler(FadeChangeHandler(false))
                        )
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

    override fun getListNewBookSuccess(data: List<BookViewModel>, refresh: Boolean) {
        view?.vgRefreshBooks?.isRefreshing = false
        rvController.hideLoadMore()
        val lstData = mutableListOf<BooksViewHolderModel>()
        data.forEach { book ->
            val booksViewHolderModel = BooksViewHolderModel(
                id = book.id,
                photo = book.photo,
                title = book.name,
                author = book.author
            )
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