package basecode.com.ui.util

import android.support.v4.content.ContextCompat
import android.widget.ImageView
import basecode.com.ui.R
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class GlideUtil: AppGlideModule() {
    companion object {
        @JvmStatic
        fun loadImage(url: String, imageView: ImageView) {
            val context = imageView.context
            GlideApp.with(context)
                    .load(url)
                    .fitCenter()
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_image_default))
                    .centerCrop()
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_image_default))
                    .into(imageView)
        }

        @JvmStatic
        fun loadImageResource(resource: Int, imageView: ImageView) {
            val context = imageView.context
            GlideApp.with(context)
                    .load(resource)
                    .fitCenter()
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_image_default))
                    .centerCrop()
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_image_default))
                    .into(imageView)
        }
    }
}