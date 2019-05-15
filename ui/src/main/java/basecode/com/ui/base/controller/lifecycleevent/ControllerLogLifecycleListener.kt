package basecode.com.ui.base.controller.lifecycleevent

import android.content.Context
import android.os.Bundle
import android.view.View
import basecode.com.ui.util.LogDebug
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType

class ControllerLogLifecycleListener(private val controllerName: String) : Controller.LifecycleListener() {

    override fun postContextUnavailable(controller: Controller) {
        super.postContextUnavailable(controller)
        LogDebug.d("$controllerName postContextUnavailable ${this}")
    }

    override fun postCreateView(controller: Controller, view: View) {
        super.postCreateView(controller, view)
        LogDebug.d("$controllerName postCreateView ${this}")
    }

    override fun postDestroyView(controller: Controller) {
        super.postDestroyView(controller)
        LogDebug.d("$controllerName postDestroyView ${this}")
    }

    override fun onRestoreInstanceState(controller: Controller, savedInstanceState: Bundle) {
        super.onRestoreInstanceState(controller, savedInstanceState)
        LogDebug.d("$controllerName onRestoreInstanceState ${this}")
    }

    override fun preContextUnavailable(controller: Controller, context: Context) {
        super.preContextUnavailable(controller, context)
        LogDebug.d("$controllerName preContextUnavailable ${this}")
    }

    override fun onRestoreViewState(controller: Controller, savedViewState: Bundle) {
        super.onRestoreViewState(controller, savedViewState)
        LogDebug.d("$controllerName onRestoreViewState ${this}")
    }

    override fun preDetach(controller: Controller, view: View) {
        super.preDetach(controller, view)
        LogDebug.d("$controllerName preDetach ${this}")
    }

    override fun onSaveInstanceState(controller: Controller, outState: Bundle) {
        super.onSaveInstanceState(controller, outState)
        LogDebug.d("$controllerName onSaveInstanceState ${this}")
    }

    override fun preCreateView(controller: Controller) {
        super.preCreateView(controller)
        LogDebug.d("$controllerName preCreateView ${this}")
    }

    override fun onSaveViewState(controller: Controller, outState: Bundle) {
        super.onSaveViewState(controller, outState)
        LogDebug.d("$controllerName onSaveViewState ${this}")
    }

    override fun onChangeEnd(controller: Controller, changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
        super.onChangeEnd(controller, changeHandler, changeType)
        LogDebug.d("$controllerName onChangeEnd ${this}")
    }

    override fun postDetach(controller: Controller, view: View) {
        super.postDetach(controller, view)
        LogDebug.d("$controllerName postDetach ${this}")
    }

    override fun preAttach(controller: Controller, view: View) {
        super.preAttach(controller, view)
        LogDebug.d("$controllerName preAttach ${this}")
    }

    override fun preContextAvailable(controller: Controller) {
        super.preContextAvailable(controller)
        LogDebug.d("$controllerName preContextAvailable ${this}")
    }

    override fun preDestroy(controller: Controller) {
        super.preDestroy(controller)
        LogDebug.d("$controllerName preDestroy ${this}")
    }

    override fun postContextAvailable(controller: Controller, context: Context) {
        super.postContextAvailable(controller, context)
        LogDebug.d("$controllerName postContextAvailable ${this}")
    }

    override fun onChangeStart(controller: Controller, changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
        super.onChangeStart(controller, changeHandler, changeType)
        LogDebug.d("$controllerName onChangeStart ${this}")
    }

    override fun postAttach(controller: Controller, view: View) {
        super.postAttach(controller, view)
        LogDebug.d("$controllerName postAttach ${this}")
    }

    override fun postDestroy(controller: Controller) {
        super.postDestroy(controller)
        LogDebug.d("$controllerName postDestroy ${this}")
    }

    override fun preDestroyView(controller: Controller, view: View) {
        super.preDestroyView(controller, view)
        LogDebug.d("$controllerName preDestroyView ${this}")
    }
}