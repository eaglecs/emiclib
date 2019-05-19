package basecode.com.ui.features.newnewsdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraString
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.GlideUtil
import kotlinx.android.synthetic.main.screen_news_detail.view.*
import org.koin.standalone.inject

class NewsDetailViewController(bundle: Bundle) : ViewController(bundle) {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private var title = ""
    private var content = ""
    private var photo = ""

    object BundleOptions {
        var Bundle.title by BundleExtraString("BooksViewController.title")
        var Bundle.content by BundleExtraString("BooksViewController.content")
        var Bundle.photo by BundleExtraString("BooksViewController.photo")
        fun create(title: String, content: String, photo: String) = Bundle().apply {
            this.title = title
            this.content = content
            this.photo = photo
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            title = options.title.valueOrEmpty()
            content = options.content.valueOrEmpty()
            photo = options.photo.valueOrEmpty()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_news_detail, container, false)
    }

    override fun initPostCreateView(view: View) {
        initView(view)
        handleOnClick(view)
    }

    private fun initView(view: View) {
        GlideUtil.loadImage(photo, view.ivNewNews, view.context)
        view.tvTitleNews.text = title
        view.tvContent.text = content
    }

    private fun handleOnClick(view: View) {
        view.ivBackNewsDetail.setOnClickListener {
            if (doubleTouchPrevent.check("ivBackNewsDetail")) {
                router.popController(this)
            }
        }
    }

}