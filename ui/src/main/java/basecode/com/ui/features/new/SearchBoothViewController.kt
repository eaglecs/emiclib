package basecode.com.ui.features.new

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController

class SearchBoothViewController : ViewController(null) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_search_booth, container, false)
    }

    override fun initPostCreateView(view: View) {
    }
}