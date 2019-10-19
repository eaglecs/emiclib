package basecode.com.ui.features.home.tab

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.LogoutSuccessEventBus
import basecode.com.domain.eventbus.model.ResultScanQRCodeEventBus
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.presentation.features.setting.SettingContract
import basecode.com.ui.R
import basecode.com.ui.SettingActivity
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.features.about.AboutViewController
import basecode.com.ui.features.bookdetail.BookDetailViewController
import basecode.com.ui.features.feedback.FeedbackViewController
import basecode.com.ui.features.fqa.FQAViewController
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.readbook.SkyDatabase
import basecode.com.ui.features.user.UserViewController
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.ScanQRCode
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.layout_tab_setting.view.*
import org.koin.standalone.inject

class SettingViewController() : ViewController(bundle = null) {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private var isLogin = false

    constructor(targetController: ViewController) : this() {
        setTargetController(targetController)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_setting, container, false)
    }

    override fun initPostCreateView(view: View) {
        handleView(view)
        initEventBus(view)
    }

    private fun initEventBus(view: View) {
        KBus.subscribe<LoginSuccessEventBus>(this) {
            isLogin = true
            view.ivLogin.setImageResource(R.drawable.ic_person)
        }
        KBus.subscribe<LogoutSuccessEventBus>(this) {
            isLogin = false
            view.ivLogin.setImageResource(R.drawable.ic_login)
        }
        KBus.subscribe<ResultScanQRCodeEventBus>(this) {
            val contentQRCode = it.contentQRCode
            val bookInfo = contentQRCode.replace("{", "").replace("}", "").split(":")
            if (bookInfo.size == 2) {
                val bookId = bookInfo.first().trim().toLong()
                targetController?.let { targetController ->
                    val bundle = BookDetailViewController.BundleOptions.create(bookId = bookId, photo = "")
                    targetController.router.pushController(RouterTransaction.with(BookDetailViewController(bundle)).pushChangeHandler(FadeChangeHandler(false)))
                }
            }
        }
    }

    private fun handleView(view: View) {
        view.vgFeedback.setOnClickListener {
            if (doubleTouchPrevent.check("vgFeedback")) {
                targetController?.let { targetController ->
                    targetController.router.pushController(RouterTransaction.with(FeedbackViewController())
                            .pushChangeHandler(FadeChangeHandler(false)))
                }
            }
        }

        view.vgSetting.setOnClickListener {
            if (doubleTouchPrevent.check("vgSetting")) {
                openSetting()
            }
        }

        view.vgFQA.setOnClickListener {
            if (doubleTouchPrevent.check("vgFQA")) {
                targetController?.let { targetController ->
                    targetController.router.pushController(RouterTransaction.with(FQAViewController())
                            .pushChangeHandler(FadeChangeHandler(false)))
                }
            }
        }

        view.vgAbout.setOnClickListener {
            if (doubleTouchPrevent.check("vgAbout")) {
                targetController?.let { targetController ->
                    targetController.router.pushController(RouterTransaction.with(AboutViewController())
                            .pushChangeHandler(FadeChangeHandler(false)))
                }
            }
        }

        view.ivScanQRCode.setOnClickListener {
            if (doubleTouchPrevent.check("ivScanQRCode")) {
                ScanQRCode.openScreenQRCode(activity, this)
            }
        }
        view.ivLogin.setOnClickListener {
            if (doubleTouchPrevent.check("ivLogin")) {
                if (isLogin) {
                    targetController?.let { targetController ->
                        targetController.router.pushController(RouterTransaction.with(UserViewController()).pushChangeHandler(FadeChangeHandler(false)))
                    }
                } else {
                    targetController?.let { targetController ->
                        val bundle = LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.Normal.value)
                        val loginViewController = LoginViewController(bundle)
                        targetController.router.pushController(RouterTransaction.with(loginViewController).pushChangeHandler(FadeChangeHandler(false)))
                    }
                }
            }
        }
    }

    private fun openSetting() {
        activity?.let { activity ->
            val intent = Intent(activity, SettingActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onDestroyView(view: View) {
        KBus.unsubscribe(this)
        super.onDestroyView(view)
    }
}