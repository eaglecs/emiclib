package basecode.com.ui.features.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.LogoutSuccessEventBus
import basecode.com.domain.model.network.response.InfoUserResponse
import basecode.com.domain.model.network.response.UserModel
import basecode.com.presentation.features.user.UserContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.features.user.tab.TabUserBookBorrowViewController
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.GlideUtil
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.support.RouterPagerAdapter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_user_info.view.*
import org.koin.standalone.inject

class UserViewController : ViewController(bundle = null), UserContract.View {

    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private val presenter: UserContract.Presenter by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_user_info, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        presenter.getUserInfo()
        initView(view)
        handleOnClickView(view)
    }

    private fun initView(view: View) {
        val pagerAdapter = object : RouterPagerAdapter(this) {
            override fun configureRouter(router: Router, position: Int) {
                if (!router.hasRootController()) {
                    val bundle = TabUserBookBorrowViewController.BundleOptions.create(position = position)
                    val page: Controller = TabUserBookBorrowViewController(targetController = this@UserViewController, bundle = bundle)
                    router.setRoot(RouterTransaction.with(page).tag("TabUserBookBorrowViewController$position"))
                }
            }

            override fun getCount() = 2
            override fun getPageTitle(position: Int): String {
                return if (position == 0) {
                    view.context.getString(R.string.text_dat_cho)
                } else {
                    view.context.getString(R.string.text_dat_muon)
                }
            }
        }
        view.vpUserBorrowBook.adapter = pagerAdapter
        view.vpUserBorrowBook.offscreenPageLimit = 2
        view.tlUserBorrowBook.setupWithViewPager(view.vpUserBorrowBook)
    }

    private fun handleOnClickView(view: View) {
        view.ivBackBookDetail.setOnClickListener {
            if (doubleTouchPrevent.check("ivBackBookDetail")) {
                router.popController(this)
            }
        }
        view.ivLogout.setOnClickListener {
            if (doubleTouchPrevent.check("ivLogout")) {
                presenter.logout()
            }
        }
    }

    override fun getUserInfoSuccess(userModel: UserModel) {
        view?.let { view ->
            view.tvUserName.text = userModel.patronName
            view.tvCodeCard.text = userModel.patronCode

            view.tvPhone.text = userModel.phone
            view.tvEmail.text = userModel.email
            view.tvPatronGroup.text = userModel.patronGroup

            val time = "${userModel.validDate} - ${userModel.expiredDate}"
            view.tvTime.text = time
            GlideUtil.loadImage(url = userModel.linkAvatar, imageView = view.ivAvatarProfile, holderImage = R.drawable.user_default, errorImage = R.drawable.user_default)
        }
        hideLoading()
    }

    override fun getUserInfoFail() {
        activity?.let { activity ->
            Toasty.error(activity, activity.resources.getString(R.string.msg_error_get_user_info))
        }
        hideLoading()
    }

    override fun logoutFail() {
        activity?.let { activity ->
            Toasty.error(activity, activity.resources.getString(R.string.msg_error_logout))
        }
    }

    override fun logoutSuccess() {
        KBus.post(LogoutSuccessEventBus())
        router.popController(this)
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