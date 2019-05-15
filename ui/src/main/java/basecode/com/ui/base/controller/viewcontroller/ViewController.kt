package basecode.com.ui.base.controller.viewcontroller

import android.os.Bundle
import android.view.View
import basecode.com.ui.base.controller.lifecycleevent.ControllerLogLifecycleListener
import com.bluelinelabs.conductor.Controller
import org.koin.standalone.KoinComponent

abstract class ViewController(bundle: Bundle?) : Controller(bundle), KoinComponent {
    init {
        addLifecycleListener(ControllerLogLifecycleListener(this.javaClass.simpleName))
        addLifecycleListener(object : LifecycleListener() {
            override fun postCreateView(controller: Controller, view: View) {
                super.postCreateView(controller, view)
                initPostCreateView(view)
            }
        })
    }

    abstract fun initPostCreateView(view: View)
}