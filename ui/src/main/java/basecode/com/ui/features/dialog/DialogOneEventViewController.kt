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
import kotlinx.android.synthetic.main.dialog_one_action.view.*

class DialogOneEventViewController<T>(bundle: Bundle?) : ViewController(bundle = bundle)
        where T : ViewController, T : DialogOneEventViewController.ActionEvent {

    constructor(targetController: T, bundle: Bundle?) : this(bundle) {
        setTargetController(targetController)
    }

    object BundleOptions {
        var Bundle.msg by BundleExtraString("msg")
        fun create(msg: String) = Bundle().apply {
            this.msg = msg
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    private var msg: String = ""

    init {
        bundle?.options { options ->
            msg = options.msg.valueOrEmpty()
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
        view.tvMsgError.text = msg
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
    }
}