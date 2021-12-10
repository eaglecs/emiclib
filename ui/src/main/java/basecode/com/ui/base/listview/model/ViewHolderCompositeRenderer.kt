package basecode.com.ui.base.listview.model

import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import basecode.com.ui.base.listview.view.CustomCompositeViewBinder
import basecode.com.ui.util.DoubleTouchPrevent
import com.github.vivchar.rendererrecyclerviewadapter.DefaultCompositeViewModel
import com.github.vivchar.rendererrecyclerviewadapter.binder.CompositeViewBinder
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewBinder
import com.github.vivchar.rendererrecyclerviewadapter.binder.ViewFinder
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

abstract class ViewHolderCompositeRenderer<M : DefaultCompositeViewModel> : ViewBinder.Binder<M>,
    KoinComponent {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    @LayoutRes
    abstract fun getLayoutId(): Int
    abstract fun getRenderConfigChild(): CustomCompositeViewBinder.CompositeRecycleConfig

    abstract fun getRecyclerViewID(): Int
    abstract fun getModelClass(): Class<M>
    abstract fun bindView(model: M, viewFinder: ViewFinder)

    @CallSuper
    override fun bindView(model: M, finder: ViewFinder, payloads: MutableList<Any>) {
        bindView(model, finder)
    }

    fun createViewBinder(): CompositeViewBinder<M> = CustomCompositeViewBinder(getLayoutId(), getRecyclerViewID(), getModelClass(), getRenderConfigChild(), this)

    fun runWithCheckMultiTouch(actionKey: String, event: () -> Unit) {
        if (doubleTouchPrevent.check(actionKey)) {
            event.invoke()
        }
    }
}