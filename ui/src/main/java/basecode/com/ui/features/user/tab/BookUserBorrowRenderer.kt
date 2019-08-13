package basecode.com.ui.features.user.tab

import basecode.com.domain.extention.formatTime
import basecode.com.domain.util.DateTimeFormat
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class BookUserBorrowRenderer : ViewHolderRenderer<ReserveBookViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_reserve_book

    override fun getModelClass(): Class<ReserveBookViewHolderModel> = ReserveBookViewHolderModel::class.java

    override fun bindView(model: ReserveBookViewHolderModel, viewFinder: ViewFinder) {
        viewFinder.setText(R.id.tvTitleBook, model.title)
        val createDateStr: String = model.createDate.formatTime(DateTimeFormat.YY_MM_DD_T_HH_MM_SS_SS, DateTimeFormat.DDMMYYFORMAT)
        val expiredDateStr = model.expiredDate.formatTime(DateTimeFormat.YY_MM_DD_T_HH_MM_SS_SS, DateTimeFormat.DDMMYYFORMAT)
        viewFinder.setText(R.id.tvTimeBorrow, "$createDateStr - $expiredDateStr")
    }
}