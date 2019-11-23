package basecode.com.ui.features.renew

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.presentation.features.renew.RenewContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.OnItemRvClickedListener
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.extension.view.gone
import basecode.com.ui.extension.view.visible
import basecode.com.ui.features.searchbook.BookViewHolderModel
import basecode.com.ui.features.searchbook.renderer.BookCategoryRenderer
import basecode.com.ui.util.DoubleTouchPrevent
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_renew.view.*
import org.koin.standalone.inject

class RenewViewController : ViewController(null), RenewContract.View {
    private val presenter: RenewContract.Presenter by inject()
    private lateinit var rvController: RecyclerViewController

    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_renew, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        handleView(view)
        presenter.getListLoanRenew()
    }

    private fun initView(view: View) {
        val input = LinearRenderConfigFactory.Input(context = view.context, orientation = LinearRenderConfigFactory.Orientation.VERTICAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvRenewBook, renderConfig)
        rvController.addViewRenderer(BookCategoryRenderer())
        rvController.setOnItemRvClickedListener(object : OnItemRvClickedListener<ViewModel> {
            override fun onItemClicked(view: View, position: Int, dataItem: ViewModel) {
                if (dataItem is BookViewHolderModel) {
                    presenter.renew(dataItem.copyNumber)
                }
            }
        })
    }

    private fun handleView(view: View) {
        view.vgRefreshRenewBooks.setOnRefreshListener {
            presenter.getListLoanRenew()
        }
        view.ivBack.setOnClickListener {
            if (doubleTouchPrevent.check("ivBack")) {
                router.popController(this)
            }
        }
    }

    override fun getListLoanRenewSuccess(lstBook: List<NewNewsResponse>) {
        view?.let { view ->
            if (lstBook.isEmpty()) {
                view.tvNoBook.visible()
            } else {
                view.tvNoBook.gone()
                val lstBookViewModel = mutableListOf<BookViewHolderModel>()
                lstBook.forEach { book ->
                    val bookViewHolderModel = BookViewHolderModel(copyNumber = book.copyNumber.valueOrEmpty(), author = book.author.valueOrEmpty(),
                            photo = book.imageCover.valueOrEmpty(), name = book.title.valueOrEmpty(), publishedYear = book.publishedYear.valueOrEmpty(),
                            publisher = book.publisher.valueOrEmpty(), id = book.id.valueOrZero())
                    lstBookViewModel.add(bookViewHolderModel)
                }
                rvController.setItems(lstBookViewModel)
                rvController.notifyDataChanged()
            }
        }
        hideLoading()
    }

    override fun getListLoanRenewFail(msgError: String) {
        view?.let { view ->
            //            Toasty.error(view.context, view.context.resources.getString(R.string.msg_change_get_list_book_renew_fail)).show()
            hideLoading()
        }
    }

    override fun renewSuccess() {
        view?.let { view ->
            Toasty.success(view.context, view.context.resources.getString(R.string.msg_change_renew_success)).show()
            hideLoading()
        }
    }

    override fun renewfail() {
        view?.let { view ->
            Toasty.error(view.context, view.context.resources.getString(R.string.msg_change_renew_fail)).show()
            hideLoading()
        }
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgRefreshRenewBooks.isRefreshing = false
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