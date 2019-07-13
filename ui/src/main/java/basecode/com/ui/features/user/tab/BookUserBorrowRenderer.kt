package basecode.com.ui.features.user.tab

import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class BookUserBorrowRenderer : ViewHolderRenderer<ReserveBookViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_reserve_book

    override fun getModelClass(): Class<ReserveBookViewHolderModel> = ReserveBookViewHolderModel::class.java

    override fun bindView(model: ReserveBookViewHolderModel, viewFinder: ViewFinder) {
        viewFinder.setText(R.id.tvTitleBook, model.title)
        viewFinder.setText(R.id.tvTimeBorrow, "${model.createDate} - ${model.expiredDate}")
    }
}