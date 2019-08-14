package basecode.com.ui.base.slider

import android.widget.ImageView
import basecode.com.ui.util.GlideUtil
import ss.com.bannerslider.ImageLoadingService

class GlideImageLoadingService : ImageLoadingService {
    override fun loadImage(url: String, imageView: ImageView) {
        GlideUtil.loadImageNews(url, imageView, imageView.context)
    }

    override fun loadImage(resource: Int, imageView: ImageView) {
        GlideUtil.loadImageResource(resource, imageView)
    }

    override fun loadImage(url: String, placeHolder: Int, errorDrawable: Int, imageView: ImageView) {
        GlideUtil.loadImageNews(url, imageView, imageView.context)
    }
}