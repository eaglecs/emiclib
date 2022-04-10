package basecode.com.ui.features.new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.LogoutSuccessEventBus
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.extention.valueOrFalse
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraBoolean
import basecode.com.ui.base.extra.BundleExtraString
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.new.renderer.BorrowReturnBookRenderer
import basecode.com.ui.features.user.UserViewController
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.GlideUtil
import basecode.com.ui.util.ScanQRCode
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.layout_header_new_app.view.*
import kotlinx.android.synthetic.main.screen_borrow_books.view.*
import org.koin.standalone.inject

class BorrowReturnBookViewController(bundle: Bundle) : ViewController(bundle) {
    private val doubleTouchPrevent by inject<DoubleTouchPrevent>()
    private lateinit var rvController: RecyclerViewController
    private var isBorrow = false
    private var isLogin = false
    private var avatar: String = ""

    object BundleOptions {
        var Bundle.isLogin by BundleExtraBoolean("isLogin")
        var Bundle.avatar by BundleExtraString("avatar")
        var Bundle.isBorrow by BundleExtraBoolean("isBorrow")
        fun create(isBorrow: Boolean, isLogin: Boolean, avatar: String) = Bundle().apply {
            this.isBorrow = isBorrow
            this.isLogin = isLogin
            this.avatar = avatar
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            isBorrow = options.isBorrow.valueOrFalse()
            isLogin = options.isLogin.valueOrFalse()
            avatar = options.avatar.valueOrEmpty()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_borrow_books, container, false)
    }

    override fun initPostCreateView(view: View) {
        initView(view)
        initEventBus(view)
        handleView(view)
    }

    private fun initEventBus(view: View) {
        KBus.subscribe<LoginSuccessEventBus>(this) {
            isLogin = true
            this.avatar = it.avatar
            GlideUtil.loadImage(
                url = it.avatar,
                imageView = view.ivLogin,
                holderImage = R.drawable.user_default,
                errorImage = R.drawable.user_default
            )
        }
        KBus.subscribe<LogoutSuccessEventBus>(this) {
            isLogin = false
            avatar = ""
            view.ivLogin.setImageResource(R.drawable.ic_login)
        }
    }

    private fun initView(view: View) {
        val input = LinearRenderConfigFactory.Input(
            context = view.context,
            orientation = LinearRenderConfigFactory.Orientation.VERTICAL
        )
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvBooks, renderConfig)
        rvController.addViewRenderer(BorrowReturnBookRenderer(isScreenBorrow = isBorrow))

        if (isLogin) {
            GlideUtil.loadImage(
                url = avatar,
                imageView = view.ivLogin,
                holderImage = R.drawable.user_default,
                errorImage = R.drawable.user_default
            )
        } else {
            view.ivLogin.setImageResource(R.drawable.ic_login)
        }
    }


    private fun handleView(view: View) {
        view.ivLogin.setOnClickListener {
            if (doubleTouchPrevent.check("ivLogin")) {
                if (isLogin) {
                    router.pushController(
                        RouterTransaction.with(
                            UserViewController()
                        ).pushChangeHandler(FadeChangeHandler(false))
                    )
                } else {
                    val bundle =
                        LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.Normal.value)
                    val loginViewController = LoginViewController(bundle)
                    router.pushController(
                        RouterTransaction.with(loginViewController)
                            .pushChangeHandler(FadeChangeHandler(false))
                    )
                }
            }
        }

        view.ivScanQRCode.setOnClickListener {
            if (doubleTouchPrevent.check("ivScanQRCode")) {
                ScanQRCode.openScreenQRCode(activity, this)
            }
        }
    }
}