package basecode.com.ui.features.bookdetail.renderer

import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderCompositeRenderer
import basecode.com.ui.base.listview.view.CustomCompositeViewBinder
import basecode.com.ui.base.listview.view.Orientation
import basecode.com.ui.features.bookdetail.viewmodel.BooksRelatedViewHolderModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class BooksRelatedRenderer : ViewHolderCompositeRenderer<BooksRelatedViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_books_related
    override fun getModelClass(): Class<BooksRelatedViewHolderModel> =
        BooksRelatedViewHolderModel::class.java

    override fun bindView(
        modelRelated: BooksRelatedViewHolderModel,
        viewFinder: ViewFinder
    ) {
    }

    override fun getRenderConfigChild(): CustomCompositeViewBinder.CompositeRecycleConfig =
        CustomCompositeViewBinder.CompositeRecycleConfig(
            managerType = CustomCompositeViewBinder.LayoutManagerType.LinearRender,
            orientation = Orientation.HORIZONTAL
        )

    override fun getRecyclerViewID(): Int = R.id.rvBook
}