package basecode.com.ui.features.searchbook.renderer

import androidx.appcompat.widget.AppCompatImageView
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.searchbook.BookViewHolderModel
import basecode.com.ui.util.GlideUtil
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class BookCategoryRenderer : ViewHolderRenderer<BookViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_book_category

    override fun getModelClass(): Class<BookViewHolderModel> = BookViewHolderModel::class.java

    override fun bindView(model: BookViewHolderModel, viewFinder: ViewFinder) {
        val ivCoverBook = viewFinder.find<AppCompatImageView>(R.id.ivCoverBook)
        GlideUtil.loadImage(url = model.photo, imageView = ivCoverBook)
        viewFinder.setText(R.id.tvTitleBook, model.name)
        viewFinder.setText(R.id.tvPublisher, model.publisher)
        viewFinder.setGone(R.id.tvPublisher, model.publisher.isEmpty())
        viewFinder.setText(R.id.tvPublishedYear, "Năm xuất bản: ${model.publishedYear}")
    }
}