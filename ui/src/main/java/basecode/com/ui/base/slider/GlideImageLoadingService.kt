package basecode.com.ui.base.slider

import android.content.Context
import android.widget.ImageView
import basecode.com.ui.util.GlideUtil
import ss.com.bannerslider.ImageLoadingService

class GlideImageLoadingService(private val context: Context) : ImageLoadingService {
    override fun loadImage(url: String, imageView: ImageView) {
        GlideUtil.loadImage(url, imageView, context)
    }

    override fun loadImage(resource: Int, imageView: ImageView) {
        GlideUtil.loadImageResource(resource, imageView, context)
    }

    override fun loadImage(url: String, placeHolder: Int, errorDrawable: Int, imageView: ImageView) {
        GlideUtil.loadImage(url, imageView, context)
    }
}