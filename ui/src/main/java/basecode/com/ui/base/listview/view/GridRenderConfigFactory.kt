package basecode.com.ui.base.listview.view

import android.content.Context
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridRenderConfigFactory(private val input: Input) : RecyclerViewController.RenderConfigFactory {
    override fun create(): RecyclerViewController.RenderConfig {
        val spanCount: Int = input.spanCount
        val layoutManager = GridLayoutManager(
            input.context,
            spanCount
        )
        layoutManager.spanSizeLookup = input.spanSizeLookup
        return RecyclerViewController.RenderConfig(layoutManager, input.animator, input.viewHolderSizer, input.decoration, input.loadMoreConfig)
    }

    class Input(val context: Context, val spanCount: Int, val spanSizeLookup: GridLayoutManager.SpanSizeLookup = GridLayoutManager.DefaultSpanSizeLookup(), val decoration: RecyclerView.ItemDecoration? = null, val loadMoreConfig: RecyclerViewController.LoadMoreConfig? = null, val animator: RecyclerView.ItemAnimator = DefaultItemAnimator(), val viewHolderSizer: ViewSizer? = null)
}