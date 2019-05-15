package basecode.com.ui.base.listview.view

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import basecode.com.ui.base.listview.model.ViewHolderModel
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.extension.view.afterMeasuredSize
import com.github.vivchar.rendererrecyclerviewadapter.binder.LoadMoreViewBinder

class RecyclerViewController(private val recyclerView: RecyclerView, private val renderConfig: RenderConfig) {

    private val viewAdapter: RendererRecyclerActionViewAdapter = RendererRecyclerActionViewAdapter()
    private var isShowingLoadMore = false
    private var itemRvClickedEvent: OnItemRvClickedListener<ViewHolderModel>? = null
    private val items: MutableList<ViewHolderModel> = arrayListOf()

    init {
        recyclerView.afterMeasuredSize { size ->
            viewAdapter.viewAction = renderConfig.viewHolderSizer?.size(size)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = renderConfig.layoutManager
            renderConfig.itemAnimator?.let {
                recyclerView.itemAnimator = it
            }
            renderConfig.itemDecoration?.let {
                recyclerView.addItemDecoration(it)
            }
            recyclerView.adapter = viewAdapter
            renderConfig.loadMoreConfig?.let {
                configLoadMore(it)
            }

            RvItemClickSupport.addTo(recyclerView).setOnItemClickListener(onItemClickListener)
        }
    }

    fun setOnItemRvClickedListener(mOnItemRvClickedListener: OnItemRvClickedListener<ViewHolderModel>) {
        this.itemRvClickedEvent = mOnItemRvClickedListener
    }

    private var onItemClickListener: RvItemClickSupport.OnItemClickListener = object : RvItemClickSupport.OnItemClickListener {
        override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View?) {
            if (v != null && position >= 0 && position <= viewAdapter.itemCount) {
                itemRvClickedEvent?.onItemClicked(v, position, viewAdapter.getItem(position))
            }
        }
    }

    private fun configLoadMore(loadMoreConfig: LoadMoreConfig) {
        viewAdapter.registerRenderer(loadMoreConfig.viewRenderer)
        recyclerView.addOnScrollListener(EndlessRecyclerViewScrollListener(recyclerView.layoutManager, loadMoreConfig.loadMoreEvent))
    }

    fun showLoadMore() {
        if (!isShowingLoadMore) {
            recyclerView.post {
                isShowingLoadMore = true
                viewAdapter.showLoadMore()
            }
        }
    }

    private fun hideLoadMore() {
        recyclerView.post {
            isShowingLoadMore = false
            viewAdapter.hideLoadMore()
        }
    }

    fun scrollToPosition(position: Int) {
        recyclerView.scrollToPosition(position)
    }

    fun addViewRenderer(viewHolderRenderer: ViewHolderRenderer<out ViewHolderModel>) {
        viewAdapter.registerRenderer(viewHolderRenderer.createViewBinder())
    }

    fun <T : ViewHolderModel> setItems(items: List<T>) {
        hideLoadMore()
        this.items.clear()
        this.items.addAll(items)
        viewAdapter.setItems(this.items)
    }

    fun <T : ViewHolderModel> addItem(item: T) {
        this.items.add(item)
        viewAdapter.setItems(this.items)
    }

    fun getItem(position: Int): ViewHolderModel {
        return viewAdapter.getItem(position)
    }

    fun notifyDataChanged() {
        viewAdapter.notifyDataSetChanged()
    }

    fun setPadding(padding: Int) {
        recyclerView.setPadding(padding, padding, padding, padding)
    }

    class RenderConfig(val layoutManager: RecyclerView.LayoutManager, val itemAnimator: RecyclerView.ItemAnimator? = null, val viewHolderSizer: ViewSizer? = null, val itemDecoration: RecyclerView.ItemDecoration? = null, val loadMoreConfig: LoadMoreConfig? = null)

    interface RenderConfigFactory {
        fun create(): RenderConfig
    }

    class EndlessRecyclerViewScrollListener(private val layoutManager: RecyclerView.LayoutManager?, private val onReachEndItemEvent: () -> Unit) : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val totalItem = layoutManager?.itemCount ?: 0
            var lastVisibleItem = 0
            if (layoutManager is LinearLayoutManager) {
                lastVisibleItem = layoutManager.findLastVisibleItemPosition()
            } else if (layoutManager is GridLayoutManager) {
                lastVisibleItem = layoutManager.findLastVisibleItemPosition()
            }

            if (lastVisibleItem >= totalItem - 2) {
                onReachEndItemEvent.invoke()
            }
        }
    }

    class LoadMoreConfig(val viewRenderer: LoadMoreViewBinder, val loadMoreEvent: () -> Unit)

}