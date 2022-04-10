package basecode.com.ui.features.new.renderer

import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.searchbook.BookViewHolderModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class BorrowReturnBookRenderer(val isScreenBorrow: Boolean) : ViewHolderRenderer<BookViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_borrow_book

    override fun getModelClass(): Class<BookViewHolderModel> = BookViewHolderModel::class.java

    override fun bindView(model: BookViewHolderModel, viewFinder: ViewFinder) {
        viewFinder.setGone(R.id.ivSelected, isScreenBorrow)
        viewFinder.setGone(R.id.btnEnter, !isScreenBorrow)
    }
}