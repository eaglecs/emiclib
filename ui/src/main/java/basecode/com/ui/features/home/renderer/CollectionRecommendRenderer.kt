package basecode.com.ui.features.home.renderer

import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.home.viewholder.NewCollectionRecommendViewHolderModel
import basecode.com.ui.features.home.viewholder.NewNewsViewHolderModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class CollectionRecommendRenderer: ViewHolderRenderer<NewCollectionRecommendViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_collection_recommend

    override fun getModelClass(): Class<NewCollectionRecommendViewHolderModel> = NewCollectionRecommendViewHolderModel::class.java

    override fun bindView(model: NewCollectionRecommendViewHolderModel, viewFinder: ViewFinder) {

    }
}