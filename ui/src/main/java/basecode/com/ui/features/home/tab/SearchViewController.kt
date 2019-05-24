package basecode.com.ui.features.home.tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.features.home.viewholder.CategoryBookViewModel
import basecode.com.ui.features.searchbook.TabBookCategoryViewController
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.support.RouterPagerAdapter
import kotlinx.android.synthetic.main.layout_tab_search.view.*

class SearchViewController() : ViewController(bundle = null) {
    private val lstCategoryBook = mutableListOf<CategoryBookViewModel>()

    constructor(targetController: ViewController) : this() {
        setTargetController(targetController)
    }

    override fun initPostCreateView(view: View) {
        initView(view)
    }

    private fun initView(view: View) {
        lstCategoryBook.add(CategoryBookViewModel(categoryId = 0, categoryName = view.context.getString(R.string.text_book)))
        lstCategoryBook.add(CategoryBookViewModel(categoryId = 4, categoryName = view.context.getString(R.string.text_ebook)))
        lstCategoryBook.add(CategoryBookViewModel(categoryId = 3, categoryName = view.context.getString(R.string.text_speak_book)))
        lstCategoryBook.add(CategoryBookViewModel(categoryId = 2, categoryName = view.context.getString(R.string.text_movie)))
        lstCategoryBook.add(CategoryBookViewModel(categoryId = 1, categoryName = view.context.getString(R.string.text_picture)))
        val pagerAdapter = object : RouterPagerAdapter(this) {
            override fun configureRouter(router: Router, position: Int) {
                if (!router.hasRootController()) {
                    val categoryBookViewModel = lstCategoryBook[position]
                    val bundle = TabBookCategoryViewController.BundleOptions.create(categoryId = categoryBookViewModel.categoryId)
                    val page: Controller = TabBookCategoryViewController(bundle)
                    router.setRoot(RouterTransaction.with(page).tag("PlaceDetail$position"))

                }
            }

            override fun getCount() = lstCategoryBook.size
            override fun getPageTitle(position: Int): String {
                val categoryBookViewModel = lstCategoryBook[position]
                return categoryBookViewModel.categoryName
            }
        }
        view.vpBook.adapter = pagerAdapter
        view.vpBook.offscreenPageLimit = lstCategoryBook.size
        view.tlCategoryBook.setupWithViewPager(view.vpBook)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_search, container, false)
    }
}