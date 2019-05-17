package basecode.com.ui.features.home.renderer

import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.home.viewholder.NewBookViewHolderModel
import basecode.com.ui.features.home.viewholder.NewEBookViewHolderModel
import basecode.com.ui.features.home.viewholder.NewNewsViewHolderModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class NewEBookRenderer: ViewHolderRenderer<NewEBookViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_new_e_book

    override fun getModelClass(): Class<NewEBookViewHolderModel> = NewEBookViewHolderModel::class.java

    override fun bindView(model: NewEBookViewHolderModel, viewFinder: ViewFinder) {

    }
}