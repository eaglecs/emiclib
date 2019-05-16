package basecode.com.ui.features.home.tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController

class SearchViewController() : ViewController(bundle = null) {
    constructor(targetController: ViewController) : this() {
        setTargetController(targetController)
    }

    override fun initPostCreateView(view: View) {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_search, container, false)
    }
}