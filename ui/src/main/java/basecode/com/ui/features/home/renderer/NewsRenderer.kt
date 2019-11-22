package basecode.com.ui.features.home.renderer

import android.support.v7.widget.AppCompatImageView
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.home.viewholder.NewNewsItemViewHolderModel
import basecode.com.ui.util.GlideUtil
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class NewsRenderer(private val onActionClickNews: (NewNewsItemViewHolderModel) -> Unit): ViewHolderRenderer<NewNewsItemViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_news

    override fun getModelClass(): Class<NewNewsItemViewHolderModel> = NewNewsItemViewHolderModel::class.java

    override fun bindView(model: NewNewsItemViewHolderModel, viewFinder: ViewFinder) {
        viewFinder.setText(R.id.tvTitleNew, model.title)
        val ivPhotoNews = viewFinder.find<AppCompatImageView>(R.id.ivPhotoNews)
        GlideUtil.loadImage(model.photo, ivPhotoNews)
        viewFinder.setOnClickListener(R.id.vgNews) {
            runWithCheckMultiTouch("vgNewBooks"){
                onActionClickNews.invoke(model)
            }
        }
    }
}