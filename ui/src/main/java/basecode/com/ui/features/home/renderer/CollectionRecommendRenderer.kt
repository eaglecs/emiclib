package basecode.com.ui.features.home.renderer

import android.support.v7.widget.RecyclerView
import android.view.View
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.OnItemRvClickedListener
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.features.home.viewholder.CollectionRecommentItemViewHolderModel
import basecode.com.ui.features.home.viewholder.NewCollectionRecommendViewHolderModel
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder

class CollectionRecommendRenderer(private val onActionClickRecommend: (CollectionRecommentItemViewHolderModel) -> Unit) : ViewHolderRenderer<NewCollectionRecommendViewHolderModel>() {
    override fun getLayoutId(): Int = R.layout.item_collection_recommend

    override fun getModelClass(): Class<NewCollectionRecommendViewHolderModel> = NewCollectionRecommendViewHolderModel::class.java

    override fun bindView(model: NewCollectionRecommendViewHolderModel, viewFinder: ViewFinder) {
        val rvCollectionRecommend = viewFinder.find<RecyclerView>(R.id.rvCollectionRecommend)
        val input = LinearRenderConfigFactory.Input(context = rvCollectionRecommend.context, orientation = LinearRenderConfigFactory.Orientation.HORIZONTAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        val rvController = RecyclerViewController(rvCollectionRecommend, renderConfig)
        rvController.addViewRenderer(CollectionRecommendItemRenderer())
        rvController.setOnItemRvClickedListener(object : OnItemRvClickedListener<ViewModel> {
            override fun onItemClicked(view: View, position: Int, dataItem: ViewModel) {
                if (dataItem is CollectionRecommentItemViewHolderModel) {
                    onActionClickRecommend.invoke(dataItem)
                }
            }
        })
        val lstBookVHM = mutableListOf<CollectionRecommentItemViewHolderModel>()
        model.lstCollectionRecommend.forEach { recommend ->
            val newBookItemViewHolderModel = CollectionRecommentItemViewHolderModel(id = recommend.id, title = recommend.name, coverPicture = recommend.coverPicture)
            lstBookVHM.add(newBookItemViewHolderModel)
        }
        rvController.setItems(lstBookVHM)
        rvController.notifyDataChanged()
    }
}