package basecode.com.ui.features.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraLong
import basecode.com.ui.base.extra.BundleOptionsCompanion

class SplashScreenViewController(bundle: Bundle) : ViewController(bundle), View.OnClickListener {

    private var resId: Long = 0

    object BundleOption {
        var Bundle.resId by BundleExtraLong("currentOptionReceive")
        fun create(resId: Long) = Bundle().apply {
            this.resId = resId
        }
    }

    companion object : BundleOptionsCompanion<SplashScreenViewController.BundleOption>(SplashScreenViewController.BundleOption)

    init {
        bundle.options { options ->
            resId = options.resId
        }
    }

    override fun initPostCreateView(view: View) {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.popup_select_ranked_option_revenue_by_group_topping, container, false)
    }

    override fun onClick(v: View?) {
    }
}