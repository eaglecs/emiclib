package basecode.com.ui.features.bookdetail.renderer

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.bookdetail.viewmodel.ImageBookViewHolderModel
import basecode.com.ui.util.GlideUtil
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class ImageBookRenderer(private val onClick: (image: String, viewPhoto: ImageView) -> Unit) :
    ViewHolderRenderer<ImageBookViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_image_book

    override fun getModelClass(): Class<ImageBookViewHolderModel> =
        ImageBookViewHolderModel::class.java

    override fun bindView(model: ImageBookViewHolderModel, viewFinder: ViewFinder) {
        val ivImage = viewFinder.find<AppCompatImageView>(R.id.ivImage)
        GlideUtil.loadImage(model.image, ivImage)
        viewFinder.setOnClickListener {
            runWithCheckMultiTouch("root") {
                onClick.invoke(model.image, ivImage)
            }
        }
    }
}