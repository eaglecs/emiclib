package basecode.com.ui.features.renew

import androidx.appcompat.widget.AppCompatImageView
import android.view.View
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.searchbook.BookViewHolderModel
import basecode.com.ui.util.GlideUtil
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class BookRenewRenderer : ViewHolderRenderer<BookViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_book_renew

    override fun getModelClass(): Class<BookViewHolderModel> = BookViewHolderModel::class.java

    override fun bindView(model: BookViewHolderModel, viewFinder: ViewFinder) {
        val ivCoverBook = viewFinder.find<AppCompatImageView>(R.id.ivCoverBook)
        GlideUtil.loadImage(url = model.photo, imageView = ivCoverBook)
        viewFinder.setText(R.id.tvTitleBook, model.name)
        viewFinder.setText(R.id.tvDueDate, "Hạn trả: ${model.dueDate}")
        viewFinder.setText(R.id.tvPublisher, "Tác giả: ${model.author}")
        viewFinder.setGone(R.id.tvPublisher, model.author.isEmpty())
        viewFinder.setText(R.id.tvPublishedYear, model.publishedYear)
        if (model.inRes == 1) {
            viewFinder.setVisibility(R.id.tvNotRenew, View.VISIBLE)
        } else {
            viewFinder.setVisibility(R.id.tvNotRenew, View.INVISIBLE)
        }
    }
}