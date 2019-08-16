package basecode.com.ui.features.user.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.network.response.ReserveResponse
import basecode.com.presentation.features.user.ReserveQueueRequestContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraInt
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.extension.view.gone
import basecode.com.ui.extension.view.visible
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_tab_user_borrow_book.view.*
import org.koin.standalone.inject

class TabUserBookBorrowViewController(bundle: Bundle) : ViewController(bundle), ReserveQueueRequestContract.View {

    private var position = 0
    private lateinit var rvController: RecyclerViewController
    private val presenter: ReserveQueueRequestContract.Presenter by inject()

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
        return inflater.inflate(R.layout.layout_tab_user_borrow_book, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        loadData()
    }

    private fun loadData() {
        if (position == 0) {
            presenter.getListBookReserveRequest()
        } else {
            presenter.getListBookReserveQueue()
        }
    }

    private fun initView(view: View) {
        val input = LinearRenderConfigFactory.Input(context = view.context, orientation = LinearRenderConfigFactory.Orientation.VERTICAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvBookBorrow, renderConfig)
        rvController.addViewRenderer(BookUserBorrowRenderer())
    }

    override fun getListBookSuccess(data: List<ReserveResponse>) {
        val lstBook = mutableListOf<ReserveBookViewHolderModel>()
        data.forEach { book ->
            val bookViewHolderModel = ReserveBookViewHolderModel(title = book.title.valueOrEmpty(),
                    copyNumber = book.copyNumber.valueOrEmpty(), createDate = book.createDate.valueOrEmpty(), expiredDate = book.expiredDate.valueOrEmpty())
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
//            Toasty.error(activity, activity.resources.getString(R.string.msg_error_get_list_book_borrow)).show()
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