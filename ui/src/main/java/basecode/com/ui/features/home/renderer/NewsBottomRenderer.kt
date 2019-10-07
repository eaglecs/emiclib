package basecode.com.ui.features.home.renderer

import android.support.v7.widget.RecyclerView
import android.view.View
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.OnItemRvClickedListener
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.features.home.viewholder.NewBookItemViewHolderModel
import basecode.com.ui.features.home.viewholder.NewEBookViewHolderModel
import basecode.com.ui.features.home.viewholder.NewNewsBottomViewHolderModel
import basecode.com.ui.features.home.viewholder.NewNewsItemViewHolderModel
import basecode.com.ui.util.DoubleTouchPrevent
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder
import org.koin.standalone.inject

class NewsBottomRenderer(private val onActionClickNews: (NewNewsItemViewHolderModel) -> Unit, private val onActionReadMore: () -> Unit) : ViewHolderRenderer<NewNewsBottomViewHolderModel>() {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    override fun getLayoutId(): Int = R.layout.item_new_news_bottom

    override fun getModelClass(): Class<NewNewsBottomViewHolderModel> = NewNewsBottomViewHolderModel::class.java

    override fun bindView(model: NewNewsBottomViewHolderModel, viewFinder: ViewFinder) {
        viewFinder.setGone(R.id.vgNewsBottom, model.lstNewNews.isEmpty())
        val rvNewBook = viewFinder.find<RecyclerView>(R.id.rvNewNewsBottom)
        val input = LinearRenderConfigFactory.Input(context = rvNewBook.context, orientation = LinearRenderConfigFactory.Orientation.HORIZONTAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        val rvController = RecyclerViewController(rvNewBook, renderConfig)
        rvController.addViewRenderer(NewsRenderer(onActionClickNews))
        viewFinder.setOnClickListener(R.id.tvReadMoreNews) {
            if (doubleTouchPrevent.check("tvReadMoreNews")) {
                onActionReadMore.invoke()
            }
        }
        val lstBookVHM = mutableListOf<NewNewsItemViewHolderModel>()
        model.lstNewNews.forEach { news ->
            val newBookItemViewHolderModel = NewNewsItemViewHolderModel(id = news.id, title = news.title, content = news.content, photo = news.picture, summary = news.summary)
            lstBookVHM.add(newBookItemViewHolderModel)
        }
        rvController.setItems(lstBookVHM)
        rvController.notifyDataChanged()
    }
}