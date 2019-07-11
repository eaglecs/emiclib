package basecode.com.ui.features.fqa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.util.DoubleTouchPrevent
import kotlinx.android.synthetic.main.layout_fqa.view.*
import org.koin.standalone.inject

class FQAViewController : ViewController(null) {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private lateinit var rvController: RecyclerViewController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_about, container, false)
    }

    override fun initPostCreateView(view: View) {
        handleView(view)
    }

    private fun handleView(view: View) {
        view.ivBack.setOnClickListener {
            if (doubleTouchPrevent.check("ivBack")) {
                router.popController(this)
            }

        }
    }
}