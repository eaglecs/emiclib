package basecode.com.ui.features.new.renderer

import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.extension.formatHtml
import basecode.com.ui.features.new.viewholder.BookBorrowNewViewHolderModel
import basecode.com.ui.features.searchbook.BookViewHolderModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class BorrowReturnBookRenderer(val isScreenBorrow: Boolean) : ViewHolderRenderer<BookBorrowNewViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_borrow_book

    override fun getModelClass(): Class<BookBorrowNewViewHolderModel> = BookBorrowNewViewHolderModel::class.java

    override fun bindView(model: BookBorrowNewViewHolderModel, viewFinder: ViewFinder) {
//        viewFinder.setGone(R.id.ivSelected, isScreenBorrow)
//        viewFinder.setGone(R.id.btnEnter, !isScreenBorrow)
        viewFinder.setText(R.id.tvTitle, model.bookName)
        viewFinder.setText(R.id.tvBookCode, model.code)
        viewFinder.setText(R.id.tvBorrowDate, model.borrowDate.replace("<br/> "," - ").ifEmpty { "_ _" })
        viewFinder.setText(R.id.tvReturnDate, model.returnDate.replace("<br/> "," - ").ifEmpty { "_ _" })
    }
}