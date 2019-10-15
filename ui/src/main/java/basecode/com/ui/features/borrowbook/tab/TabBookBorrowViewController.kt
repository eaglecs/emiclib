package basecode.com.ui.features.borrowbook.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.presentation.features.bookborrow.BookBorrowContract
import basecode.com.presentation.features.books.BookBorrowViewModel
import basecode.com.presentation.features.books.BookViewModel
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
import basecode.com.ui.features.borrowbook.BookBorrowRenderer
import basecode.com.ui.features.borrowbook.BookBorrowViewHolderModelMapper
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

    override fun renewSuccess() {
        loadData()
        hideLoading()
    }


    override fun renewfail() {
        activity?.let {
            Toasty.error(it, "Gia hạn thất bại. Bạn làm ơn kiểm tra lại!").show()
        }
        hideLoading()
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
        rvController.addViewRenderer(BookBorrowRenderer { copyNumberBook ->
            presenter.renew(copyNumberBook)
        })
    }

    override fun getListBookSuccess(lstBook: List<BookBorrowViewModel>) {
        val lstBookViewModel = BookBorrowViewHolderModelMapper().mapList(lstBook)
        if (lstBook.isEmpty()) {
            view?.tvNoBook?.visible()
        } else {
            view?.tvNoBook?.gone()
            rvController.setItems(lstBookViewModel)
            rvController.notifyDataChanged()
        }
        hideLoading()
    }

    override fun getListBookFail() {
        hideLoading()
        activity?.let { activity ->
            //            Toasty.error(activity, activity.resources.getString(R.string.msg_error_get_list_book_borrow)).show()
        }
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgRefreshBooksBorrow.isRefreshing = false
            view.vgLoadingBorrowBook.show()
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