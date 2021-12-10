package basecode.com.ui.features.home.renderer

import androidx.appcompat.widget.AppCompatImageView
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.home.viewholder.NewBookItemViewHolderModel
import basecode.com.ui.util.GlideUtil
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class NewBookItemRenderer : ViewHolderRenderer<NewBookItemViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_new_book_item

    override fun getModelClass(): Class<NewBookItemViewHolderModel> = NewBookItemViewHolderModel::class.java

    override fun bindView(model: NewBookItemViewHolderModel, viewFinder: ViewFinder) {
        val ivPhotoBook = viewFinder.find<AppCompatImageView>(R.id.ivPhotoBook)
        GlideUtil.loadImage(model.coverPicture, ivPhotoBook)
        viewFinder.setText(R.id.tvTitleBook, model.title)

    }
}