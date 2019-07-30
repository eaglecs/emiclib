package basecode.com.ui.features.home.tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.LogoutSuccessEventBus
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.presentation.features.setting.SettingContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.features.borrowbook.BorrowBookViewController
import basecode.com.ui.features.changepass.ChangePassViewController
import basecode.com.ui.features.downloadbook.DownloadBookViewController
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.notify.NotifyViewController
import basecode.com.ui.features.renew.RenewViewController
import basecode.com.ui.features.user.UserViewController
import basecode.com.ui.util.DoubleTouchPrevent
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.layout_tab_profile.view.*
import org.koin.standalone.inject

class ProfileViewController() : ViewController(bundle = null), SettingContract.View {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private val presenter: SettingContract.Presenter by inject()
    private var isLogin = false

    constructor(targetController: ViewController) : this() {
        setTargetController(targetController)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_profile, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        handleOnClick(view)
        iniEventBus(view)
        presenter.checkLogin()
    }

    private fun iniEventBus(view: View) {
        KBus.subscribe<LoginSuccessEventBus>(this) {
            isLogin = true
        }
        KBus.subscribe<LogoutSuccessEventBus>(this){
            isLogin = false
        }
    }

    private fun handleOnClick(view: View) {
        view.vgInfoUser.setOnClickListener {
            if (doubleTouchPrevent.check("vgInfoUser")) {
                targetController?.let { targetController ->
                    if (isLogin) {
                        targetController.router.pushController(RouterTransaction.with(UserViewController())
                                .pushChangeHandler(FadeChangeHandler(false)))
                    } else {
                        gotoScreenLogin(targetController)
                    }
                }
            }
        }
        view.vgBorrow.setOnClickListener {
            if (doubleTouchPrevent.check("vgBorrow")) {
                targetController?.let { targetController ->
                    if (isLogin) {
                        targetController.router.pushController(RouterTransaction.with(BorrowBookViewController())
                                .pushChangeHandler(FadeChangeHandler(false)))
                    } else {
                        gotoScreenLogin(targetController)
                    }

                }
            }
        }

        view.vgNotify.setOnClickListener {
            if (doubleTouchPrevent.check("vgNotify")) {
                targetController?.let { targetController ->
                    if (isLogin) {
                        targetController.router.pushController(RouterTransaction.with(NotifyViewController())
                                .pushChangeHandler(FadeChangeHandler(false)))
                    } else {
                        gotoScreenLogin(targetController)
                    }
                }


            }
        }

        view.vgRenew.setOnClickListener {
            if (doubleTouchPrevent.check("vgDelayUser")) {
                targetController?.let { targetController ->

                    if (isLogin) {
                        targetController.router.pushController(RouterTransaction.with(RenewViewController()).pushChangeHandler(FadeChangeHandler(false)))
                    } else {
                        gotoScreenLogin(targetController)
                    }
                }
            }
        }

        view.vgDownload.setOnClickListener {
            if (doubleTouchPrevent.check("vgDownload")) {
                targetController?.let { targetController ->
                    if (isLogin) {
                        targetController.router.pushController(RouterTransaction.with(DownloadBookViewController()).pushChangeHandler(FadeChangeHandler(false)))
                    } else {
                        gotoScreenLogin(targetController)
                    }
                }
            }
        }

        view.vgChangePass.setOnClickListener {
            if (doubleTouchPrevent.check("vgChangePass")) {
                targetController?.let { targetController ->
                    if (isLogin) {
                        targetController.router.pushController(RouterTransaction.with(ChangePassViewController()).pushChangeHandler(FadeChangeHandler(false)))
                    } else {
                        gotoScreenLogin(targetController)
                    }
                }
            }
        }
    }

    private fun gotoScreenLogin(targetController: Controller) {
        targetController.router.pushController(RouterTransaction.with(LoginViewController())
                .pushChangeHandler(FadeChangeHandler(false)))
    }

    override fun resultCheckLogin(isLogin: Boolean) {
        this.isLogin = isLogin
    }

    override fun showLoading() {
        view?.let { view ->
            view.vgLoading.hide()
        }
    }

    override fun hideLoading() {
        view?.let { view ->
            view.vgLoading.hide()
        }
    }

    override fun onDestroyView(view: View) {
        presenter.detachView()
        KBus.unsubscribe(this)
        super.onDestroyView(view)
    }
}