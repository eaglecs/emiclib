package basecode.com.ui.features.home.tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.features.about.AboutViewController
import basecode.com.ui.features.fqa.FQAViewController
import basecode.com.ui.util.DoubleTouchPrevent
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.layout_tab_setting.view.*
import org.koin.standalone.inject

class SettingViewController() : ViewController(bundle = null) {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()

    constructor(targetController: ViewController) : this() {
        setTargetController(targetController)
    }

    override fun initPostCreateView(view: View) {
        handleView(view)
    }

    private fun handleView(view: View) {
        view.vgFeedback.setOnClickListener {
            if (doubleTouchPrevent.check("vgFeedback")) {

            }
        }

        view.vgSetting.setOnClickListener {
            if (doubleTouchPrevent.check("vgSetting")) {

            }
        }

        view.vgFQA.setOnClickListener {
            if (doubleTouchPrevent.check("vgFQA")) {
                targetController?.let { targetController ->
                    targetController.router.pushController(RouterTransaction.with(FQAViewController()).pushChangeHandler(FadeChangeHandler(false)))
                }
            }
        }

        view.vgAbout.setOnClickListener {
            if (doubleTouchPrevent.check("vgAbout")) {
                targetController?.let { targetController ->
                    targetController.router.pushController(RouterTransaction.with(AboutViewController()).pushChangeHandler(FadeChangeHandler(false)))
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_setting, container, false)
    }
}