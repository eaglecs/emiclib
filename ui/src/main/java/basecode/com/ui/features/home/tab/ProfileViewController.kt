package basecode.com.ui.features.home.tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.features.borrowbook.BorrowBookViewController
import basecode.com.ui.features.user.UserViewController
import basecode.com.ui.util.DoubleTouchPrevent
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.layout_tab_profile.view.*
import org.koin.standalone.inject

class ProfileViewController() : ViewController(bundle = null) {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()

    constructor(targetController: ViewController) : this() {
        setTargetController(targetController)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_profile, container, false)
    }

    override fun initPostCreateView(view: View) {
        handleOnClick(view)
    }

    private fun handleOnClick(view: View) {
        view.vgInfoUser.setOnClickListener {
            if (doubleTouchPrevent.check("vgInfoUser")) {
                targetController?.let { targetController ->
                    targetController.router.pushController(RouterTransaction.with(UserViewController())
                            .pushChangeHandler(FadeChangeHandler(false)))

                }
            }
        }
        view.vgBorrow.setOnClickListener {
            if (doubleTouchPrevent.check("vgBorrow")) {
                targetController?.let { targetController ->
                    targetController.router.pushController(RouterTransaction.with(BorrowBookViewController())
                            .pushChangeHandler(FadeChangeHandler(false)))

                }
            }
        }

        view.vgNotify.setOnClickListener {
            if (doubleTouchPrevent.check("vgNotify")) {

            }
        }

        view.vgDelayUser.setOnClickListener {
            if (doubleTouchPrevent.check("vgDelayUser")) {

            }
        }

        view.vgDownload.setOnClickListener {
            if (doubleTouchPrevent.check("vgDownload")) {

            }
        }

        view.vgChangePass.setOnClickListener {
            if (doubleTouchPrevent.check("vgChangePass")) {

            }
        }
    }
}