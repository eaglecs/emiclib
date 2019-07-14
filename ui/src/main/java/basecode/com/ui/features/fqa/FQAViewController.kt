package basecode.com.ui.features.fqa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.model.network.response.QAResponse
import basecode.com.presentation.features.fqa.FQAContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.extension.view.gone
import basecode.com.ui.extension.view.visible
import basecode.com.ui.util.DoubleTouchPrevent
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_fqa.view.*
import org.koin.standalone.inject

class FQAViewController : ViewController(null), FQAContract.View {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private val presenter: FQAContract.Presenter by inject()
    private lateinit var rvController: RecyclerViewController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_fqa, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        handleView(view)
        presenter.getListFQA()
    }

    private fun initView(view: View) {
        val input = LinearRenderConfigFactory.Input(context = view.context, orientation = LinearRenderConfigFactory.Orientation.VERTICAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvFQA, renderConfig)
        rvController.addViewRenderer(FQARenderer())
    }

    private fun handleView(view: View) {
        view.ivBack.setOnClickListener {
            if (doubleTouchPrevent.check("ivBack")) {
                router.popController(this)
            }

        }
    }

    override fun getListFQASuccess(lstFQA: List<QAResponse>) {
        view?.let { view ->
            if (lstFQA.isEmpty()) {
                view.tvNoFQA.visible()
            } else {
                view.tvNoFQA.gone()
                val lstFQAResult = FQAViewHolderModelMapper().mapList(lstFQA)
                rvController.setItems(lstFQAResult)
                rvController.notifyDataChanged()
            }
        }
    }

    override fun getListFQAFail() {
        activity?.let { activity ->
            Toasty.error(activity, activity.resources.getString(R.string.msg_error_get_list_fqa))
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