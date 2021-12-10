package basecode.com.ui.features.bookdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraString
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.util.GlideUtil
import kotlinx.android.synthetic.main.screen_show_full_image.view.*

class ShowFullImageViewController(bundle: Bundle) : ViewController(bundle) {
    private var image = ""

    object BundleOptions {
        var Bundle.image by BundleExtraString("ShowFullImageViewController.image")
        fun create(
            image: String
        ) =
            Bundle().apply {
                this.image = image
            }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            image = options.image.valueOrEmpty()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_show_full_image, container, false)
    }

    override fun initPostCreateView(view: View) {
        initView(view)
        handleView(view)
    }

    private fun initView(view: View) {
        GlideUtil.loadImage(image, view.ivImage)
    }

    private fun handleView(view: View) {
        view.ivBack.setOnClickListener {
            router.popController(this)
        }
    }
}