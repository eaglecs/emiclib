package basecode.com.ui.features.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraString
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.extension.view.gone
import kotlinx.android.synthetic.main.dialog_one_action.view.*

class DialogOneEventViewController<T>(bundle: Bundle?) : ViewController(bundle = bundle)
        where T : ViewController, T : DialogOneEventViewController.ActionEvent {

    constructor(targetController: T, bundle: Bundle?) : this(bundle) {
        setTargetController(targetController)
    }

    object BundleOptions {
        var Bundle.msg by BundleExtraString("msg")
        var Bundle.title by BundleExtraString("title")
        var Bundle.textEvent by BundleExtraString("textEvent")
        fun create(title: String, msg: String, textEvent: String = "") = Bundle().apply {
            this.title = title
            this.msg = msg
            this.textEvent = textEvent
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    private var title: String = ""
    private var msg: String = ""
    private var textEvent = ""

    init {
        bundle?.options { options ->
            title = options.title.valueOrEmpty()
            msg = options.msg.valueOrEmpty()
            textEvent = options.textEvent.valueOrEmpty()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.dialog_one_action, container, false)
    }

    override fun initPostCreateView(view: View) {
        initView(view)
    }

    private fun initView(view: View) {
        setDataView(view)
        handleClick(view)
    }

    private fun setDataView(view: View) {
        if (title.isEmpty()) {
            view.tvTitleDialogOneEvent.gone()
        }
        view.tvTitleDialogOneEvent.text = title
        view.tvMsgError.text = msg
        if (textEvent.isNotEmpty()) {
            view.tvButton.text = textEvent
        }
    }

    interface ActionEvent {
        fun onResultAfterHandleDialog()
    }

    private fun handleClick(view: View) {
        view.tvButton.setOnClickListener {
            router.popController(this)
            val targetController = this.targetController
            if (targetController is ActionEvent) {
                targetController.onResultAfterHandleDialog()
            }
        }

        view.vgRoot.setOnClickListener {
            router.popController(this)
        }
    }
}