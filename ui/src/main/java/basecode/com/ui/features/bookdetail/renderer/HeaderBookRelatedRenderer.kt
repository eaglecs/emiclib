package basecode.com.ui.features.bookdetail.renderer

import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.bookdetail.viewmodel.HeaderBookRelatedViewHolderModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class HeaderBookRelatedRenderer : ViewHolderRenderer<HeaderBookRelatedViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_books_related
    override fun getModelClass(): Class<HeaderBookRelatedViewHolderModel> =
        HeaderBookRelatedViewHolderModel::class.java

    override fun bindView(
        modelRelated: HeaderBookRelatedViewHolderModel,
        viewFinder: ViewFinder
    ) {
    }
}