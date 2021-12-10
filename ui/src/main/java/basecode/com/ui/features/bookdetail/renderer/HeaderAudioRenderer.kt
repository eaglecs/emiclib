package basecode.com.ui.features.bookdetail.renderer

import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderCompositeRenderer
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.base.listview.view.CustomCompositeViewBinder
import basecode.com.ui.base.listview.view.Orientation
import basecode.com.ui.features.bookdetail.viewmodel.HeaderAudioViewHolderModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class HeaderAudioRenderer: ViewHolderRenderer<HeaderAudioViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_audios

    override fun getModelClass(): Class<HeaderAudioViewHolderModel> = HeaderAudioViewHolderModel::class.java

    override fun bindView(
        model: HeaderAudioViewHolderModel,
        viewFinder: ViewFinder
    ) {
    }
}