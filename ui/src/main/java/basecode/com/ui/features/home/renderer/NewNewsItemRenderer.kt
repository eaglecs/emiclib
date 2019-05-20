package basecode.com.ui.features.home.renderer

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.extension.formatHtml
import basecode.com.ui.features.home.viewholder.NewNewsItemViewHolderModel
import basecode.com.ui.util.GlideUtil
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder
import org.koin.standalone.inject

class NewNewsItemRenderer(private val context: Context) : ViewHolderRenderer<NewNewsItemViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_new_news_item

    override fun getModelClass(): Class<NewNewsItemViewHolderModel> = NewNewsItemViewHolderModel::class.java

    override fun bindView(model: NewNewsItemViewHolderModel, viewFinder: ViewFinder) {
        viewFinder.setText(R.id.tvTitleNews, model.title)
        viewFinder.setText(R.id.tvContent, model.content.formatHtml())
        val ivNewNews = viewFinder.find<AppCompatImageView>(R.id.ivNewNews)
        GlideUtil.loadImageNews(model.photo, ivNewNews, context)
        viewFinder.setGone(R.id.tvTitleNews, model.title.isEmpty())
    }
}