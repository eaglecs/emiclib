package basecode.com.ui.features.borrowbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.features.borrowbook.tab.TabBookBorrowViewController
import basecode.com.ui.util.DoubleTouchPrevent
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.support.RouterPagerAdapter
import kotlinx.android.synthetic.main.layout_borrow_book.view.*
import org.koin.standalone.inject

class BorrowBookViewController : ViewController(null) {
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_borrow_book, container, false)
    }

    override fun initPostCreateView(view: View) {
        initView(view)
        handleView(view)
    }

    private fun handleView(view: View) {
        view.ivBackBookDetail.setOnClickListener {
            if (doubleTouchPrevent.check("ivBackBookDetail")) {
                router.popController(this)
            }
        }
    }

    private fun initView(view: View) {
        val pagerAdapter = object : RouterPagerAdapter(this) {
            override fun configureRouter(router: Router, position: Int) {
                if (!router.hasRootController()) {

                    val bundle = TabBookBorrowViewController.BundleOptions.create(position = position)
                    val page: Controller = TabBookBorrowViewController(targetController = this@BorrowBookViewController, bundle = bundle)
                    router.setRoot(RouterTransaction.with(page).tag("TabBookBorrowViewController$position"))
                }
            }

            override fun getCount() = 2
            override fun getPageTitle(position: Int): String {
                return if (position == 0) {
                    view.context.getString(R.string.text_dang_muon)
                } else {
                    view.context.getString(R.string.text_da_muon)
                }
            }
        }
        view.vpBorrowBook.adapter = pagerAdapter
        view.vpBorrowBook.offscreenPageLimit = 2
        view.tlBorrowBook.setupWithViewPager(view.vpBorrowBook)

    }
}