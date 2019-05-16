package basecode.com.ui.features.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.presentation.features.home.HomeContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraLong
import basecode.com.ui.base.extra.BundleOptionsCompanion
import org.koin.standalone.inject

class HomeScreenViewController(bundle: Bundle) : ViewController(bundle), View.OnClickListener, HomeContract.View {
    private var resId: Long = 0
    private val presenter: HomeContract.Presenter by inject()

    object BundleOption {
        var Bundle.resId by BundleExtraLong("currentOptionReceive")
        fun create(resId: Long) = Bundle().apply {
            this.resId = resId
        }
    }

    companion object : BundleOptionsCompanion<HomeScreenViewController.BundleOption>(HomeScreenViewController.BundleOption)

    init {
        bundle.options { options ->
            resId = options.resId
        }
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        presenter.getListNewBook(NewEBookRequest(numberItem = 10, pageIndex = 1, pageSize = 10))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.popup_select_ranked_option_revenue_by_group_topping, container, false)
    }

    override fun onClick(v: View?) {
    }

    override fun getListNewEBookSuccess() {
    }

    override fun showErrorGetListNewEbook() {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onDestroyView(view: View) {
        presenter.detachView()
        super.onDestroyView(view)
    }
}