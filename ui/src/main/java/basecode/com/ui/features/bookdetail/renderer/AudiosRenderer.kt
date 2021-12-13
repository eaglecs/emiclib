package basecode.com.ui.features.bookdetail.renderer

import androidx.recyclerview.widget.RecyclerView
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.features.bookdetail.viewmodel.AudioViewHolderModel
import basecode.com.ui.features.bookdetail.viewmodel.AudiosViewHolderModel
import basecode.com.ui.features.books.BookRelatedRenderer
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class AudiosRenderer(private val onSelected: (model: AudioViewHolderModel) -> Unit): ViewHolderRenderer<AudiosViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_audios

    override fun getModelClass(): Class<AudiosViewHolderModel> = AudiosViewHolderModel::class.java

    override fun bindView(
        model: AudiosViewHolderModel,
        viewFinder: ViewFinder
    ) {
        val rvAudios = viewFinder.find<RecyclerView>(R.id.rvAudios)
        val input = LinearRenderConfigFactory.Input(
            context = rvAudios.context,
            orientation = LinearRenderConfigFactory.Orientation.VERTICAL
        )
        val renderConfig = LinearRenderConfigFactory(input).create()
        val rvController = RecyclerViewController(rvAudios, renderConfig)
        rvController.addViewRenderer(AudioRenderer { modelRelated ->
            onSelected.invoke(modelRelated)
        })
        rvController.setItems(model.lstAudio)
        rvController.notifyDataChanged()
    }
}