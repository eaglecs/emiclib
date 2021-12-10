package basecode.com.ui.features.bookdetail.renderer

import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.features.bookdetail.viewmodel.AudioViewHolderModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class AudioRenderer(private val onSelected: (model: AudioViewHolderModel) -> Unit) :
    ViewHolderRenderer<AudioViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_audio

    override fun getModelClass(): Class<AudioViewHolderModel> = AudioViewHolderModel::class.java

    override fun bindView(model: AudioViewHolderModel, viewFinder: ViewFinder) {
        viewFinder.setText(R.id.tvTitleAudio, "${model.title} Phần ${model.id}")
        viewFinder.setOnClickListener {
            runWithCheckMultiTouch("onSelected") {
                onSelected.invoke(model)
            }
        }
        viewFinder.setSelected(R.id.tvTitleAudio, model.isSelected)
    }
}