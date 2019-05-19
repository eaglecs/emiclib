package basecode.com.ui.features.home.tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.presentation.features.newnews.NewNewsContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.OnItemRvClickedListener
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.features.home.renderer.NewNewsItemRenderer
import basecode.com.ui.features.home.viewholder.NewNewsItemViewHolderModel
import basecode.com.ui.features.newnewsdetail.NewsDetailViewController
import com.bluelinelabs.conductor.RouterTransaction
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.LoadMoreViewBinder
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_tab_news.view.*
import org.koin.standalone.inject

class NewsViewController() : ViewController(bundle = null), NewNewsContract.View {

    private val presenter: NewNewsContract.Presenter by inject()
    private lateinit var rvController: RecyclerViewController

    constructor(targetController: ViewController) : this() {
        setTargetController(targetController)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        onClickView(view)
        presenter.getListNewNews(isRefresh = true)
    }

    private fun initView(view: View) {
        val loadMoreConfig = RecyclerViewController.LoadMoreConfig(viewRenderer = LoadMoreViewBinder(R.layout.item_load_more)) {
            if (presenter.isShowLoadMore()) {
                rvController.showLoadMore()
                presenter.getListNewNews(false)
            }
        }
        val input = LinearRenderConfigFactory.Input(context = view.context, loadMoreConfig = loadMoreConfig, orientation = LinearRenderConfigFactory.Orientation.VERTICAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvNewNews, renderConfig)
        activity?.let { activity ->
            rvController.addViewRenderer(NewNewsItemRenderer(activity))
        }
        rvController.setOnItemRvClickedListener(object : OnItemRvClickedListener<ViewModel> {
            override fun onItemClicked(view: View, position: Int, dataItem: ViewModel) {
                if (dataItem is NewNewsItemViewHolderModel) {
                    targetController?.let { targetController ->
                        val bundle = NewsDetailViewController.BundleOptions.create(title = dataItem.title, photo = dataItem.photo, content = dataItem.content)
                        targetController.router.pushController(RouterTransaction.with(NewsDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
                    }
                }
            }

        })
    }

    private fun onClickView(view: View) {
        view.vgRefreshNewNews.setOnRefreshListener {
            presenter.getListNewNews(isRefresh = true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_news, container, false)
    }

    override fun getListNewNewsSuccess(data: List<NewNewsResponse>, refresh: Boolean) {
        rvController.hideLoadMore()
        val lstData = mutableListOf<NewNewsItemViewHolderModel>()
        data.forEach { news ->
            val detail = news.details.valueOrEmpty()
            val newNewsItemViewHolderModel = NewNewsItemViewHolderModel(id = news.id.valueOrZero(), title = news.title.valueOrEmpty(), content = detail, photo = news.picture.valueOrEmpty())
            lstData.add(newNewsItemViewHolderModel)
        }
        if (refresh) {
            rvController.setItems(lstData)
        } else {
            rvController.addItems(lstData)
        }
        rvController.notifyDataChanged()
        hideLoading()
    }

    override fun showErrorGetListNewNews() {
        rvController.hideLoadMore()
        activity?.let { activity ->
            Toasty.error(activity, activity.getString(R.string.msg_error_get_list_new_news))
        }
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgRefreshNewNews.isRefreshing = false
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