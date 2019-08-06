package basecode.com.ui.features.books

import android.support.v7.widget.AppCompatImageView
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.util.GlideUtil
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class BooksRenderer : ViewHolderRenderer<BooksViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_books

    override fun getModelClass(): Class<BooksViewHolderModel> = BooksViewHolderModel::class.java

    override fun bindView(model: BooksViewHolderModel, viewFinder: ViewFinder) {
        val ivPhotoBooks = viewFinder.find<AppCompatImageView>(R.id.ivPhotoBooks)
        GlideUtil.loadImage(model.photo, ivPhotoBooks)
        viewFinder.setText(R.id.tvTitleBooks, model.title)
    }
}