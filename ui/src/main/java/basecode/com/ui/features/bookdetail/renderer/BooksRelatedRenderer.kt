package basecode.com.ui.features.bookdetail.renderer

import androidx.recyclerview.widget.RecyclerView
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.listview.model.ViewHolderCompositeRenderer
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.base.listview.view.CustomCompositeViewBinder
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.Orientation
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.features.bookdetail.BookDetailViewController
import basecode.com.ui.features.bookdetail.viewmodel.BooksRelatedViewHolderModel
import basecode.com.ui.features.books.BookRelatedRenderer
import basecode.com.ui.features.books.BooksViewHolderModel
import basecode.com.ui.features.home.renderer.NewBookItemRenderer
import com.bluelinelabs.conductor.RouterTransaction
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class BooksRelatedRenderer(private val onActionSelected: (model: BooksViewHolderModel) -> Unit) :
    ViewHolderRenderer<BooksRelatedViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_books_related
    override fun getModelClass(): Class<BooksRelatedViewHolderModel> =
        BooksRelatedViewHolderModel::class.java

    override fun bindView(
        model: BooksRelatedViewHolderModel,
        viewFinder: ViewFinder
    ) {
        val rvBook = viewFinder.find<RecyclerView>(R.id.rvBook)
        val input = LinearRenderConfigFactory.Input(
            context = rvBook.context,
            orientation = LinearRenderConfigFactory.Orientation.HORIZONTAL
        )
        val renderConfig = LinearRenderConfigFactory(input).create()
        val rvController = RecyclerViewController(rvBook, renderConfig)
        rvController.addViewRenderer(BookRelatedRenderer { modelRelated ->
            onActionSelected.invoke(modelRelated)
        })
        rvController.setItems(model.lstBook)
        rvController.notifyDataChanged()
    }
}