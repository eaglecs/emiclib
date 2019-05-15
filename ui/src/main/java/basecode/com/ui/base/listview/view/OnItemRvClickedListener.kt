package basecode.com.ui.base.listview.view

import android.view.View

interface OnItemRvClickedListener<in D> {
    fun onItemClicked(view: View, position: Int, dataItem: D)
}