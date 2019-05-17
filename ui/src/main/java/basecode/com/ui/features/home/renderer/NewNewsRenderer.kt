package basecode.com.ui.features.home.renderer

import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.home.viewholder.NewNewsViewHolderModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class NewNewsRenderer: ViewHolderRenderer<NewNewsViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_new_news

    override fun getModelClass(): Class<NewNewsViewHolderModel> = NewNewsViewHolderModel::class.java

    override fun bindView(model: NewNewsViewHolderModel, viewFinder: ViewFinder) {

    }
}