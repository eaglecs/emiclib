package basecode.com.ui.features.borrowbook

import android.support.v7.widget.AppCompatImageView
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.searchbook.BookViewHolderModel
import basecode.com.ui.util.GlideUtil
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class BookBorrowRenderer : ViewHolderRenderer<BookBorrowViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_book_borrow

    override fun getModelClass(): Class<BookBorrowViewHolderModel> = BookBorrowViewHolderModel::class.java

    override fun bindView(model: BookBorrowViewHolderModel, viewFinder: ViewFinder) {
        val ivCoverBook = viewFinder.find<AppCompatImageView>(R.id.ivCoverBook)
        GlideUtil.loadImage(url = model.photo, imageView = ivCoverBook)
        viewFinder.setText(R.id.tvTitleBook, model.title)
        viewFinder.setText(R.id.tvCopyNumber, model.coppyNumber)
        viewFinder.setText(R.id.tvTimeBorrow, "${model.checkOutDate} - ${model.dueDate}")
    }
}