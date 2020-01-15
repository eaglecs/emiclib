package basecode.com.ui.features.notify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.CheckStatusNewMessageEventBus
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.network.response.MessageResponse
import basecode.com.presentation.features.notify.NotifyContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.extension.view.gone
import basecode.com.ui.extension.view.visible
import basecode.com.ui.features.dialog.DialogOneEventViewController
import basecode.com.ui.util.DoubleTouchPrevent
import com.bluelinelabs.conductor.RouterTransaction
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_notifies.view.*
import org.koin.standalone.inject

class NotifyViewController : ViewController(null), NotifyContract.View, DialogOneEventViewController.ActionEvent {

    private val presenter: NotifyContract.Presenter by inject()
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private lateinit var rvController: RecyclerViewController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_notifies, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        handleView(view)
        loadData()
    }

    private fun loadData() {
        presenter.getListNotify()
    }

    private fun handleView(view: View) {
        view.vgRefreshNotify.setOnRefreshListener {
            presenter.getListNotify()
        }
        view.ivBackNotify.setOnClickListener {
            if (doubleTouchPrevent.check("ivBackNotify")) {
                router.popController(this)
            }
        }
    }

    private fun initView(view: View) {
        val input = LinearRenderConfigFactory.Input(context = view.context, orientation = LinearRenderConfigFactory.Orientation.VERTICAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvNotify, renderConfig)
        rvController.addViewRenderer(NotifyRenderer { model ->
            presenter.readMessage(model.id)
            val bundle = DialogOneEventViewController.BundleOptions.create(title = model.title, msg = model.message, textEvent = "Đóng")
            router.pushController(RouterTransaction.with(DialogOneEventViewController(targetController = this, bundle = bundle))
                    .pushChangeHandler(FadeChangeHandler(false)))
        })
    }

    override fun onResultAfterHandleDialog() {
    }

    override fun getListNotifySuccess(data: List<MessageResponse>) {
        view?.let { view ->
            if (data.isEmpty()) {
                view.tvNoMessage.visible()
            } else {
                view.tvNoMessage.gone()
                val lstResult = mutableListOf<NotifyViewHolderModel>()
                data.forEach { model ->
                    val notifyViewHolderModel = NotifyViewHolderModel(id = model.id.valueOrZero(), title = model.title.valueOrEmpty(), message = model.message.valueOrEmpty(),
                            status = model.status.valueOrZero(), createDate = model.createDate.valueOrEmpty())
                    lstResult.add(notifyViewHolderModel)
                }
                rvController.setItems(lstResult)
                rvController.notifyDataChanged()
            }

        }
        hideLoading()
    }

    override fun getListNotifyFail() {
        activity?.let { activity ->
            //            Toasty.error(activity, activity.resources.getString(R.string.msg_error_read_message_fail)).show()
            hideLoading()
        }
    }

    override fun readMessageSuccess(messageId: Long) {
        val items = rvController.getItems()
        kotlin.run loop@{
            items.forEach { viewModel ->
                if (viewModel is NotifyViewHolderModel && viewModel.id == messageId) {
                    viewModel.status = 1
                    return@loop
                }
            }
        }
        rvController.notifyDataChanged()
        KBus.post(CheckStatusNewMessageEventBus())
    }

    override fun readMessageFail() {
        activity?.let { activity ->
            //            Toasty.error(activity, activity.resources.getString(R.string.msg_error_read_message_fail)).show()
        }
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgRefreshNotify.isRefreshing = false
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