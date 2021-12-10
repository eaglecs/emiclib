package basecode.com.ui.base.listview.view

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.github.vivchar.rendererrecyclerviewadapter.CompositeViewModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.CompositeViewBinder
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewBinder


class CustomCompositeViewBinder<M : CompositeViewModel>(layoutID: Int, recyclerViewID: Int, type: Class<M>, private val config: CompositeRecycleConfig, binder: ViewBinder.Binder<M>) : CompositeViewBinder<M>(layoutID, recyclerViewID, type, binder) {
    override fun createLayoutManager(): RecyclerView.LayoutManager {
        return when (config.managerType) {
            LayoutManagerType.GridLayout -> {
                GridLayoutManager(context, config.spanCount)
            }
            LayoutManagerType.LinearRender -> {
                LinearLayoutManager(context, getOrientation(config.orientation), false)
            }
        }
    }

    private fun getOrientation(orientation: Orientation): Int =
            when (orientation) {
                Orientation.HORIZONTAL -> LinearLayoutManager.HORIZONTAL
                Orientation.VERTICAL -> LinearLayoutManager.VERTICAL
            }

    class CompositeRecycleConfig(val managerType: LayoutManagerType, var spanCount: Int = 0,
                                 var orientation: Orientation = Orientation.VERTICAL)

    enum class LayoutManagerType {
        GridLayout, LinearRender
    }
}