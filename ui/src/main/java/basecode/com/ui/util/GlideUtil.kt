package basecode.com.ui.util

import android.content.Context
import androidx.core.content.ContextCompat
import android.widget.ImageView
import basecode.com.ui.BuildConfig
import basecode.com.ui.R
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class GlideUtil : AppGlideModule() {
    companion object {
        @JvmStatic
        fun loadImage(url: String, imageView: ImageView) {
            val context = imageView.context
            val imgBookDefault = if (BuildConfig.USE_DATA_OTHER_APP) {
                if (BuildConfig.USE_DATA_TDN) {
                    R.drawable.img_book_default_tdn
                } else {
                    R.drawable.img_book_default
                }
            } else {
                R.drawable.img_book_default
            }

            GlideApp.with(context)
                    .load(url)
                    .centerCrop()
                    .placeholder(ContextCompat.getDrawable(context, imgBookDefault))
                    .error(ContextCompat.getDrawable(context, imgBookDefault))
                    .into(imageView)
        }

        @JvmStatic
        fun loadImage(url: String, imageView: ImageView, errorImage: Int) {
            val imgBookDefault = if (BuildConfig.USE_DATA_OTHER_APP) {
                if (BuildConfig.USE_DATA_TDN) {
                    R.drawable.img_book_default_tdn
                } else {
                    R.drawable.img_book_default
                }
            } else {
                R.drawable.img_book_default
            }
            val context = imageView.context
            GlideApp.with(context)
                    .load(url)
                    .centerCrop()
                    .placeholder(ContextCompat.getDrawable(context, imgBookDefault))
                    .error(ContextCompat.getDrawable(context, errorImage))
                    .into(imageView)
        }

        @JvmStatic
        fun loadImage(url: String, imageView: ImageView, holderImage: Int, errorImage: Int) {
            val context = imageView.context
            GlideApp.with(context)
                    .load(url)
                    .centerCrop()
                    .placeholder(ContextCompat.getDrawable(context, holderImage))
                    .error(ContextCompat.getDrawable(context, errorImage))
                    .into(imageView)
        }

        @JvmStatic
        fun loadImageNews(url: String, imageView: ImageView, context: Context) {
            GlideApp.with(imageView.context)
                    .load(url)
                    .fitCenter()
                    .centerCrop()
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.news))
                    .error(ContextCompat.getDrawable(context, R.drawable.news))
                    .into(imageView)
        }

        @JvmStatic
        fun loadImageResource(resource: Int, imageView: ImageView) {
            val imgBookDefault = if (BuildConfig.USE_DATA_OTHER_APP) {
                if (BuildConfig.USE_DATA_TDN) {
                    R.drawable.img_book_default_tdn
                } else {
                    R.drawable.img_book_default
                }
            } else {
                R.drawable.img_book_default
            }
            val context = imageView.context
            GlideApp.with(context)
                    .load(resource)
                    .fitCenter()
                    .placeholder(ContextCompat.getDrawable(context, imgBookDefault))
                    .centerCrop()
                    .error(ContextCompat.getDrawable(context, imgBookDefault))
                    .into(imageView)
        }
    }
}