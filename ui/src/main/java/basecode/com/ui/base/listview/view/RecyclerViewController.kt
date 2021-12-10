package basecode.com.ui.base.listview.view

import android.content.Context
import android.support.v7.widget.*
import android.view.View
import basecode.com.ui.base.listview.model.ViewHolderCompositeRenderer
import basecode.com.ui.base.listview.model.ViewHolderModel
import basecode.com.ui.base.listview.model.ViewHolderRenderer
import basecode.com.ui.extension.view.afterMeasuredSize
import com.github.vivchar.rendererrecyclerviewadapter.DefaultCompositeViewModel
import com.github.vivchar.rendererrecyclerviewadapter.ViewModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.LoadMoreViewBinder


class RecyclerViewController(private val recyclerView: RecyclerView, private val renderConfig: RenderConfig) {
    private val viewAdapter: RendererRecyclerActionViewAdapter = RendererRecyclerActionViewAdapter()
    private var isShowingLoadMore = false
    private var itemRvClickedEvent: OnItemRvClickedListener<ViewModel>? = null
    private val items: MutableList<ViewModel> = mutableListOf()

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
//                viewAdapter.registerRenderer(LoadMoreViewRenderer(R.layout.item_load_more))
                configLoadMore(it)
            }
            RvItemClickSupport.addTo(recyclerView).setOnItemClickListener(onItemClickListener)
        }
    }

    fun getItems(): MutableList<ViewModel> {
        return items
    }

    fun removeItem(model: ViewModel) {
        items.remove(model)
        viewAdapter.setItems(this.items)
    }

    fun getNumItem(): Int {
        return items.size
    }

    fun setOnItemRvClickedListener(mOnItemRvClickedListener: OnItemRvClickedListener<ViewModel>) {
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
        loadMoreConfig.viewRenderer?.let {
            viewAdapter.registerRenderer(loadMoreConfig.viewRenderer)
        }
        recyclerView.addOnScrollListener(EndlessRecyclerViewScrollListener(recyclerView.layoutManager, loadMoreConfig))
    }

    fun onScrollListener(action: (position: Int, isScrollDown: Boolean) -> Unit) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager
                layoutManager?.let {
                    var firstVisibleItem = 0
                    if (layoutManager is LinearLayoutManager) {
                        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    } else if (layoutManager is GridLayoutManager) {
                        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    }
                    action.invoke(firstVisibleItem, dy < 0)
                }
            }
        })
    }

    fun onScrollBottomListener(direction: Direction, action: (isBottom: Boolean) -> Unit) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(direction.value)) {
                    action.invoke(true)
                } else {
                    action.invoke(false)
                }
            }
        })
    }

    fun showLoadMore() {
        if (!isShowingLoadMore) {
            recyclerView.post {
                isShowingLoadMore = true
                viewAdapter.showLoadMore()
            }
        }
    }

    fun hideLoadMore() {
        recyclerView.post {
            renderConfig.loadMoreConfig?.let { loadMoreConfig ->
                loadMoreConfig.isLoadingMore = false
            }
            isShowingLoadMore = false
            viewAdapter.hideLoadMore()
        }
    }

    fun scrollSmoothToPosition(context: Context, position: Int) {
        val smoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = position
        recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
    }

    fun findFirstVisibleItemPosition(): Int {
        var findFirstVisibleItemPosition = 0
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            findFirstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        }
        return findFirstVisibleItemPosition
    }

    fun scrollToPosition(position: Int) {
        if (renderConfig.layoutManager is LinearLayoutManager) {
            renderConfig.layoutManager.scrollToPositionWithOffset(position, 0)
        } else {
            recyclerView.scrollToPosition(position)
        }
    }

    fun scrollToBottom() {
        if (this.items.size > 0) {
            val position = this.items.size - 1
            if (renderConfig.layoutManager is LinearLayoutManager) {
                renderConfig.layoutManager.scrollToPositionWithOffset(position, 0)
            } else {
                recyclerView.scrollToPosition(position)
            }
        }
    }

    fun addViewRenderer(viewHolderRenderer: ViewHolderRenderer<out ViewHolderModel>) {
        viewAdapter.registerRenderer(viewHolderRenderer.createViewBinder())
    }

    fun addViewRenderer(parentViewHolderRenderer: ViewHolderCompositeRenderer<out DefaultCompositeViewModel>,
                        childViewHolderRenderer: ViewHolderRenderer<out ViewHolderModel>
    ) {
        viewAdapter.registerRenderer(parentViewHolderRenderer.createViewBinder()
            .registerRenderer(childViewHolderRenderer.createViewBinder()))
    }

    fun addViewRenderer(
        parentViewHolderRenderer: ViewHolderCompositeRenderer<out DefaultCompositeViewModel>,
        vararg lstChildViewHolderRenderer: ViewHolderRenderer<out ViewHolderModel>
    ) {
        val createViewBinder = parentViewHolderRenderer.createViewBinder()
        lstChildViewHolderRenderer.forEach { childRenderer ->
            createViewBinder.registerRenderer(childRenderer.createViewBinder())
        }
        viewAdapter.registerRenderer(createViewBinder)
    }

    fun addViewRenderer(
        parentViewHolderRenderer: ViewHolderCompositeRenderer<out DefaultCompositeViewModel>,
        subParentViewHolderRenderer: ViewHolderCompositeRenderer<out DefaultCompositeViewModel>,
        childViewHolderRenderer: ViewHolderRenderer<out ViewHolderModel>
    ) {
        val parentViewBinder = parentViewHolderRenderer.createViewBinder()
        val subParentViewBinder = subParentViewHolderRenderer.createViewBinder()
        parentViewBinder.registerRenderer(subParentViewBinder)
        subParentViewBinder.registerRenderer(childViewHolderRenderer.createViewBinder())
        viewAdapter.registerRenderer(parentViewBinder)
    }

    fun addViewRenderer(
        parentViewHolderRenderer: ViewHolderCompositeRenderer<out DefaultCompositeViewModel>,
        subParentViewHolderRenderer: ViewHolderCompositeRenderer<out DefaultCompositeViewModel>,
        firstChildViewHolderRenderer: ViewHolderRenderer<out ViewHolderModel>,
        secondChildViewHolderRenderer: ViewHolderRenderer<out ViewHolderModel>
    ) {
        val parentViewBinder = parentViewHolderRenderer.createViewBinder()
        val subParentViewBinder = subParentViewHolderRenderer.createViewBinder()
        parentViewBinder.registerRenderer(subParentViewBinder)
        subParentViewBinder.registerRenderer(firstChildViewHolderRenderer.createViewBinder())
        subParentViewBinder.registerRenderer(secondChildViewHolderRenderer.createViewBinder())
        viewAdapter.registerRenderer(parentViewBinder)
    }


    fun clearData() {
        this.items.clear()
    }

    fun <T : ViewModel> setItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        viewAdapter.setItems(this.items)
    }

    fun <T : ViewModel> addItems(items: List<T>) {
        this.items.addAll(items)
        viewAdapter.setItems(this.items)
    }

    fun <T : ViewModel> addItems(index: Int, items: List<T>) {
        this.items.addAll(index, items)
        viewAdapter.setItems(this.items)
    }

    fun <T : ViewModel> addItem(item: T) {
        this.items.add(item)
        viewAdapter.setItems(this.items)
    }

    fun <T : ViewModel> addItem(index: Int, item: T) {
        if (this.items.size > index) {
            this.items.add(index, item)
        } else {
            this.items.add(item)
        }
        viewAdapter.setItems(this.items)
    }

    fun <T : ViewModel> replace(position: Int, item: T) {
        this.items[position] = item
        viewAdapter.setItems(this.items)
    }

    fun getItem(position: Int): ViewModel {
        return viewAdapter.getItem(position)
    }

    fun disableDiffUtil() {
        viewAdapter.disableDiffUtil()
    }

    fun notifyDataChanged() {
        viewAdapter.notifyDataSetChanged()
    }

    fun notifyItemChanged(position: Int) {
        viewAdapter.notifyItemChanged(position)
    }

    fun setPadding(padding: Int) {
        recyclerView.setPadding(padding, padding, padding, padding)
    }

    class RenderConfig(val layoutManager: RecyclerView.LayoutManager, val itemAnimator: RecyclerView.ItemAnimator? = null, val viewHolderSizer: ViewSizer? = null, val itemDecoration: RecyclerView.ItemDecoration? = null, val loadMoreConfig: LoadMoreConfig? = null)

    interface RenderConfigFactory {
        fun create(): RenderConfig
    }

    class EndlessRecyclerViewScrollListener(private val layoutManager: RecyclerView.LayoutManager?, private val loadMoreConfig: LoadMoreConfig) : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!loadMoreConfig.isLoadingMore) {
                val totalItem = layoutManager?.itemCount ?: 0
                if (!loadMoreConfig.isLoadMoreTop) {
                    var lastVisibleItem = 0
                    when (layoutManager) {
                        is LinearLayoutManager -> {
                            lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                        }
                        is GridLayoutManager -> {
                            lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                        }
                        is StaggeredGridLayoutManager -> {
                            var lastVisibleItems: IntArray? = null
                            lastVisibleItems = layoutManager.findLastVisibleItemPositions(lastVisibleItems)
                            lastVisibleItems?.let {
                                if (lastVisibleItems.isNotEmpty()) {
                                    lastVisibleItem = lastVisibleItems.last()
                                }
                            }
                        }
                    }

                    if (lastVisibleItem >= totalItem - 5) {
                        loadMoreConfig.isLoadingMore = true
                        loadMoreConfig.loadMoreEvent.invoke()
                    }

                } else {
                    var firstVisibleItem = 0
                    when (layoutManager) {
                        is LinearLayoutManager -> {
                            firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                        }
                        is GridLayoutManager -> {
                            firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                        }
                        is StaggeredGridLayoutManager -> {
                            var firstVisibleItems: IntArray? = null
                            firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems)
                            firstVisibleItems?.let {
                                if (firstVisibleItems.isNotEmpty()) {
                                    firstVisibleItem = firstVisibleItems.first()
                                }
                            }
                        }
                    }

                    if (firstVisibleItem <= 5) {
                        loadMoreConfig.isLoadingMore = true
                        loadMoreConfig.loadMoreEvent.invoke()
                    }
                }
            }
        }
    }

    fun clear() {
        this.items.clear()
    }

    fun removeAllView() {
        recyclerView.removeAllViewsInLayout()
    }

    fun notifyItemMoved(positionCurrent: Int, targetPosition: Int) {
        viewAdapter.notifyItemMoved(positionCurrent, targetPosition)
    }

    enum class Direction(val value: Int) {
        UP(-1), DOWN(1)
    }

    class LoadMoreConfig(
        var isLoadingMore: Boolean = false,
        val viewRenderer: LoadMoreViewBinder,
        val isLoadMoreTop: Boolean = false,
        val loadMoreEvent: () -> Unit
    )

}