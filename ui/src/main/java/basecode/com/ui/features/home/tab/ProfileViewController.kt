package basecode.com.ui.features.home.tab

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.CheckStatusNewMessageEventBus
import basecode.com.domain.eventbus.model.LogoutSuccessEventBus
import basecode.com.domain.eventbus.model.ResultScanQRCodeEventBus
import basecode.com.domain.model.bus.LoadStatusNotify
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.presentation.features.setting.SettingContract
import basecode.com.ui.BuildConfig
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.extension.view.gone
import basecode.com.ui.extension.view.invisible
import basecode.com.ui.extension.view.visible
import basecode.com.ui.features.bookdetail.BookDetailViewController
import basecode.com.ui.features.borrowbook.BorrowBookViewController
import basecode.com.ui.features.changepass.ChangePassViewController
import basecode.com.ui.features.downloadbook.DownloadBookViewController
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.notify.NotifyViewController
import basecode.com.ui.features.renew.RenewViewController
import basecode.com.ui.features.requestdocument.RequestDocumentViewController
import basecode.com.ui.features.user.UserViewController
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.GlideUtil
import basecode.com.ui.util.ScanQRCode
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
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
        initView(view)
        handleOnClick(view)
        iniEventBus(view)
        presenter.checkNewMessage()
        presenter.checkLogin()
    }

    private fun initView(view: View) {
        if (BuildConfig.USE_DATA_OTHER_APP) {
            view.vgBookRoom.gone()
            view.vgChangePass.invisible()
        }
    }

    override fun setStatusNewMessage(isHasNewMessage: Boolean) {
        view?.let { view ->
            if (isHasNewMessage) {
                view.tvNewNotify.visible()
            } else {
                view.tvNewNotify.gone()
            }
        }
    }

    private fun iniEventBus(view: View) {
        KBus.subscribe<LoadStatusNotify>(this) {
            presenter.checkNewMessage()
        }
        KBus.subscribe<CheckStatusNewMessageEventBus>(this) {
            presenter.checkNewMessage()
        }
        KBus.subscribe<LoginSuccessEventBus>(this) {
            isLogin = true
            GlideUtil.loadImage(url = it.avatar, imageView = view.ivLogin, holderImage = R.drawable.user_default, errorImage = R.drawable.user_default)
            targetController?.let { targetController ->
                when (it.type) {
                    LoginSuccessEventBus.Type.UserInfo -> {
                        targetController.router.pushController(RouterTransaction.with(UserViewController())
                                .pushChangeHandler(FadeChangeHandler(false)))
                    }
                    LoginSuccessEventBus.Type.ChangePass -> {
                        targetController.router.pushController(RouterTransaction.with(ChangePassViewController()).pushChangeHandler(FadeChangeHandler(false)))
                    }
                    LoginSuccessEventBus.Type.DownloadBook -> {
                        targetController.router.pushController(RouterTransaction.with(DownloadBookViewController()).pushChangeHandler(FadeChangeHandler(false)))
                    }
                    LoginSuccessEventBus.Type.Notify -> {
                        targetController.router.pushController(RouterTransaction.with(NotifyViewController())
                                .pushChangeHandler(FadeChangeHandler(false)))
                    }
                    LoginSuccessEventBus.Type.BorrowBook -> {
                        targetController.router.pushController(RouterTransaction.with(BorrowBookViewController())
                                .pushChangeHandler(FadeChangeHandler(false)))
                    }
                    LoginSuccessEventBus.Type.RenewBook -> {
                        targetController.router.pushController(RouterTransaction.with(RenewViewController()).pushChangeHandler(FadeChangeHandler(false)))
                    }
                    LoginSuccessEventBus.Type.RequestDocument -> {
                        targetController.router.pushController(RouterTransaction.with(RequestDocumentViewController())
                                .pushChangeHandler(FadeChangeHandler(false)))
                    }
                    LoginSuccessEventBus.Type.BookRoom -> {
                        bookRoom()
                    }

                }
            }
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

    private fun handleOnClick(view: View) {
        view.vgInfoUser.setOnClickListener {
            if (doubleTouchPrevent.check("vgInfoUser")) {
                targetController?.let { targetController ->
                    if (isLogin) {
                        targetController.router.pushController(RouterTransaction.with(UserViewController())
                                .pushChangeHandler(FadeChangeHandler(false)))
                    } else {
                        gotoScreenLogin(targetController, LoginSuccessEventBus.Type.UserInfo)
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
                        gotoScreenLogin(targetController, LoginSuccessEventBus.Type.BorrowBook)
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
                        gotoScreenLogin(targetController, LoginSuccessEventBus.Type.Notify)
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
                        gotoScreenLogin(targetController, LoginSuccessEventBus.Type.RenewBook)
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
                        gotoScreenLogin(targetController, LoginSuccessEventBus.Type.DownloadBook)
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
                        gotoScreenLogin(targetController, LoginSuccessEventBus.Type.ChangePass)
                    }
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
        view.vgRequestDocument.setOnClickListener {
            if (doubleTouchPrevent.check("vgRequestDocument")) {
                targetController?.let { targetController ->
                    if (isLogin) {
                        targetController.router.pushController(RouterTransaction.with(RequestDocumentViewController())
                                .pushChangeHandler(FadeChangeHandler(false)))
                    } else {
                        val bundle = LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.RequestDocument.value)
                        val loginViewController = LoginViewController(bundle)
                        targetController.router.pushController(RouterTransaction.with(loginViewController).pushChangeHandler(FadeChangeHandler(false)))
                    }
                }
            }
        }
        view.vgBookRoom.setOnClickListener {
            if (doubleTouchPrevent.check("vgBookRoom")) {
                if (isLogin) {
                    bookRoom()
                } else {
                    targetController?.let { targetController ->
                        val bundle = LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.BookRoom.value)
                        val loginViewController = LoginViewController(bundle)
                        targetController.router.pushController(RouterTransaction.with(loginViewController).pushChangeHandler(FadeChangeHandler(false)))
                    }
                }
            }
        }
    }

    private fun bookRoom() {
        presenter.getUserInfo()
    }

    override fun getUserInfoSuccess(data: LoginRequest) {
        val url = "https://lib.eiu.edu.vn/ORegisterBooking.aspx?key=eMicLib&uid=${data.username}"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun gotoScreenLogin(targetController: Controller, type: LoginSuccessEventBus.Type) {
        val bundle = LoginViewController.BundleOptions.create(type.value)
        val loginViewController = LoginViewController(bundle)
        targetController.router.pushController(RouterTransaction.with(loginViewController)
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