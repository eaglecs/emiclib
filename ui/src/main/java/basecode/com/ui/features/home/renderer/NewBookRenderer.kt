package basecode.com.ui.features.home.renderer

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import basecode.com.ui.R
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.OnItemRvClickedListener
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.features.home.viewholder.NewBookItemViewHolderModel
import basecode.com.ui.features.home.viewholder.NewBookViewHolderModel
import basecode.com.ui.util.DoubleTouchPrevent
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder
import org.koin.standalone.inject

class NewBookRenderer(private val onActionClickBook: (NewBookItemViewHolderModel) -> Unit, private val onActionReadMore: () -> Unit) : ViewHolderRenderer<NewBookViewHolderModel>() {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    override fun getLayoutId(): Int = R.layout.item_new_book

    override fun getModelClass(): Class<NewBookViewHolderModel> = NewBookViewHolderModel::class.java

    override fun bindView(model: NewBookViewHolderModel, viewFinder: ViewFinder) {
        val rvNewBook = viewFinder.find<RecyclerView>(R.id.rvNewBook)
        val input = LinearRenderConfigFactory.Input(context = rvNewBook.context, orientation = LinearRenderConfigFactory.Orientation.HORIZONTAL)
        val renderConfig = LinearRenderConfigFactory(input).create()
        val rvController = RecyclerViewController(rvNewBook, renderConfig)
        rvController.addViewRenderer(NewBookItemRenderer())
        rvController.setOnItemRvClickedListener(object : OnItemRvClickedListener<ViewModel> {
            override fun onItemClicked(view: View, position: Int, dataItem: ViewModel) {
                if (dataItem is NewBookItemViewHolderModel) {
                    onActionClickBook.invoke(dataItem)
                }
            }
        })
        viewFinder.setOnClickListener(R.id.tvReadMore) {
            if (doubleTouchPrevent.check("tvReadMore")) {
                onActionReadMore.invoke()
            }
        }
        val lstBookVHM = mutableListOf<NewBookItemViewHolderModel>()
        model.lstNewBook.forEach { newBook ->
            val newBookItemViewHolderModel = NewBookItemViewHolderModel(id = newBook.id, title = newBook.title,
                    coverPicture = newBook.coverPicture, author = newBook.author)
            lstBookVHM.add(newBookItemViewHolderModel)
        }
        rvController.setItems(lstBookVHM)
        rvController.notifyDataChanged()

    }
}