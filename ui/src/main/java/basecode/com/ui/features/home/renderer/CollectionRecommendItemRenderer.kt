package basecode.com.ui.features.home.renderer

import android.support.v7.widget.AppCompatImageView
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.home.viewholder.CollectionRecommentItemViewHolderModel
import basecode.com.ui.util.GlideUtil
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class CollectionRecommendItemRenderer : ViewHolderRenderer<CollectionRecommentItemViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_collection_recommend_item

    override fun getModelClass(): Class<CollectionRecommentItemViewHolderModel> = CollectionRecommentItemViewHolderModel::class.java

    override fun bindView(model: CollectionRecommentItemViewHolderModel, viewFinder: ViewFinder) {
        val ivCollection = viewFinder.find<AppCompatImageView>(R.id.ivCollection)
        GlideUtil.loadImage(model.coverPicture, ivCollection)
        viewFinder.setText(R.id.tvTitleCollection, model.title)

    }
}