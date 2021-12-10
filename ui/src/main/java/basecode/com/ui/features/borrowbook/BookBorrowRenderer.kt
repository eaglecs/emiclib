package basecode.com.ui.features.borrowbook

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatImageView
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.util.GlideUtil
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class BookBorrowRenderer(private val context: Context, private val onActionReNewBook: (copyNumberBook: String) -> Unit) : ViewHolderRenderer<BookBorrowViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_book_borrow

    override fun getModelClass(): Class<BookBorrowViewHolderModel> = BookBorrowViewHolderModel::class.java

    override fun bindView(model: BookBorrowViewHolderModel, viewFinder: ViewFinder) {
        val ivCoverBook = viewFinder.find<AppCompatImageView>(R.id.ivCoverBook)
        GlideUtil.loadImage(url = model.photo, imageView = ivCoverBook)
        viewFinder.setText(R.id.tvTitleBook, model.title)
        viewFinder.setText(R.id.tvCopyNumber, model.copyNumber)
        viewFinder.setText(R.id.tvTimeBorrow, "${model.checkOutDate} - ${model.dueDate}")
        if (model.afterRenewDate.isNotEmpty()) {
            viewFinder.setText(R.id.tvRenewBook, "Gia hạn đến ${model.afterRenewDate}")
            viewFinder.setGone(R.id.tvRenewBook, false)
        } else {
            viewFinder.setGone(R.id.tvRenewBook, true)
        }
        val timeDueDate = model.timeDueDate
        if(model.isBorrowing && System.currentTimeMillis() > timeDueDate){
            viewFinder.setTextColor(R.id.tvTimeBorrow, ContextCompat.getColor(context, R.color.md_red500) )
        } else {
            viewFinder.setTextColor(R.id.tvTimeBorrow, ContextCompat.getColor(context, R.color.md_grey600) )
        }
        viewFinder.setOnClickListener(R.id.tvRenewBook) {
            runWithCheckMultiTouch("tvRenewBook"){
                onActionReNewBook.invoke(model.copyNumber)
            }
        }
    }
}