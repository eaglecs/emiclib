package basecode.com.ui.util

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import basecode.com.ui.R
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

@GlideModule
class GlideUtil : AppGlideModule(), KoinComponent {
    private val context: Application by inject()

    companion object {
        @JvmStatic
        fun loadImage(url: String, imageView: ImageView, context: Context, onActionRequestFail: (() -> Unit)? = null) {
            GlideApp.with(context)
                    .load(url)
                    .fitCenter()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            onActionRequestFail?.invoke()
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                    })
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_image_default))
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_image_default))
                    .into(imageView)
        }

        @JvmStatic
        fun loadImageResource(resource: Int, imageView: ImageView, context: Context) {
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