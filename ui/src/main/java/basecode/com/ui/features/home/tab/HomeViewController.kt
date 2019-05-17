package basecode.com.ui.features.home.tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.domain.model.network.response.InfoHomeResponse
import basecode.com.presentation.features.home.HomeContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.features.home.renderer.NewNewsRenderer
import basecode.com.ui.features.home.viewholder.NewBookViewHolderModel
import basecode.com.ui.features.home.viewholder.NewCollectionRecommendViewHolderModel
import basecode.com.ui.features.home.viewholder.NewEBookViewHolderModel
import basecode.com.ui.features.home.viewholder.NewNewsViewHolderModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_tab_home.view.*
import org.koin.standalone.inject

class HomeViewController() : ViewController(bundle = null), HomeContract.View {

    private val presenter: HomeContract.Presenter by inject()
    private lateinit var rvController: RecyclerViewController

    constructor(targetController: ViewController) : this() {
        setTargetController(targetController)
    }

    override fun initPostCreateView(view: View) {
        initView(view)
        loadData()
    }

    private fun initView(view: View) {
        val input = LinearRenderConfigFactory.Input(context = view.context, orientation = LinearRenderConfigFactory.Orientation.VERTICAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvHome, renderConfig)
        rvController.addViewRenderer(NewNewsRenderer { newsId ->

        })
    }

    private fun loadData() {
        presenter.attachView(this)
        presenter.getListNewBook()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_home, container, false)
    }

    override fun getListNewEBookSuccess(data: InfoHomeResponse) {
        val newNewsViewHolderModel = NewNewsViewHolderModel(lstNewNews = data.lstNewNews)
        rvController.addItem(newNewsViewHolderModel)
//        val newBookViewHolderModel = NewBookViewHolderModel(lstNewBook = data.lstNewBook)
//        rvController.addItem(newBookViewHolderModel)
//        val newEBookViewHolderModel = NewEBookViewHolderModel(lstNewEBook = data.lstNewEBook)
//        rvController.addItem(newEBookViewHolderModel)
//        val newCollectionRecommendViewHolderModel = NewCollectionRecommendViewHolderModel(lstCollectionRecommend = data.lstCollectionRecommend)
//        rvController.addItem(newCollectionRecommendViewHolderModel)
        rvController.notifyDataChanged()
        hideLoading()
    }

    override fun showErrorGetListNewEbook() {
        view?.let { view ->
            Toasty.error(view.context, view.context.getString(R.string.msg_error_get_info)).show()
        }
    }

    override fun showLoading() {
        view?.let { view ->
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