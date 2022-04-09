package basecode.com.ui.features.home.tab

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.SearchAdvanceBookEventBus
import basecode.com.domain.eventbus.model.SearchBookWithKeyEventBus
import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.model.bus.HideKeyboardEventBus
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraLong
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.extension.view.gone
import basecode.com.ui.extension.view.hideKeyboard
import basecode.com.ui.extension.view.visible
import basecode.com.ui.features.home.viewholder.CategoryBookViewModel
import basecode.com.ui.features.search.SearchAdvanceViewController
import basecode.com.ui.features.searchbook.TabBookCategoryViewController
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.ScanQRCode
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.support.RouterPagerAdapter
import kotlinx.android.synthetic.main.layout_tab_search.view.*
import org.koin.standalone.inject
import java.util.*

class SearchViewController(bundle: Bundle) : ViewController(bundle = bundle),
    SearchAdvanceViewController.Action {
    private val lstCategoryBook = mutableListOf<CategoryBookViewModel>()
    private var timer: Timer? = null
    private var categoryIdCurrent = 0
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private var boothId: Long = 0

    object BundleOptions {
        var Bundle.boothId by BundleExtraLong("boothId")
        fun create(boothId: Long) = Bundle().apply {
            this.boothId = boothId
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            boothId = options.boothId.valueOrZero()
        }
    }

//    constructor(targetController: ViewController, bundle: Bundle) : this(bundle) {
//        setTargetController(targetController)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.layout_tab_search, container, false)
    }

    override fun initPostCreateView(view: View) {
        initView(view)
        initEventBus(view)
        handleOnClick(view)
    }


    private fun initEventBus(view: View) {
        KBus.subscribe<HideKeyboardEventBus>(this) {
            view.edtSearchBook.hideKeyboard()
            view.edtSearchBook.clearFocus()
        }
    }

    private fun handleOnClick(view: View) {
        view.edtSearchBook.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                event?.let {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.action == KeyEvent.ACTION_DOWN
                        && event.keyCode == KeyEvent.KEYCODE_ENTER
                    ) {
                        val textSearch = view.edtSearchBook.text.toString().trim()
                        handleSearchBookWithText(textSearch)
                        view.edtSearchBook.hideKeyboard()
                        view.edtSearchBook.clearFocus()
                        return true
                    }

                }
                return false
            }

        })
        view.edtSearchBook.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(textSearchDestination: Editable?) {
                val textSearch = textSearchDestination.toString().trim()
                if (textSearch.isNotEmpty()) {
                    view.btnClearTextSearchPlace.visible()
                } else {
                    view.btnClearTextSearchPlace.gone()
                }
                handleSearchBookWithText(textSearch)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                timer?.cancel()
            }

        })
        view.btnClearTextSearchPlace.setOnClickListener {
            if (doubleTouchPrevent.check("btnClearTextSearchPlace")) {
                view.edtSearchBook.setText("")
            }
        }

        view.ivSearchPlace.setOnClickListener {
            if (doubleTouchPrevent.check("ivSearchPlace")) {
                view.edtSearchBook.hideKeyboard()
                view.edtSearchBook.clearFocus()
                val textSearch = view.edtSearchBook.text.toString()
                KBus.post(
                    SearchBookWithKeyEventBus(
                        categoryId = categoryIdCurrent,
                        textSearch = textSearch,
                        boothId = boothId
                    )
                )
            }
        }
        view.ivSearchAdvance.setOnClickListener {
            if (doubleTouchPrevent.check("ivSearchAdvance")) {
                router.pushController(
                    RouterTransaction.with(SearchAdvanceViewController(targetController = this))
                        .pushChangeHandler(FadeChangeHandler(false))
                )
            }
        }

        view.ivScanQRCode.setOnClickListener {
            if (doubleTouchPrevent.check("ivScanQRCode")) {
                ScanQRCode.openScreenQRCode(activity, this)
            }
        }
    }

    override fun searchAdvanceBook(
        title: String,
        author: String,
        keyword: String,
        language: String
    ) {
        KBus.post(
            SearchAdvanceBookEventBus(
                categoryId = categoryIdCurrent, title = title, author = author,
                language = language, keyword = keyword
            )
        )
    }

    private fun handleSearchBookWithText(textSearch: String) {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                KBus.post(
                    SearchBookWithKeyEventBus(
                        categoryId = categoryIdCurrent,
                        textSearch = textSearch,
                        boothId = boothId
                    )
                )
            }
        }, 500)
    }

    private fun initView(view: View) {
        lstCategoryBook.add(
            CategoryBookViewModel(
                categoryId = 0,
                categoryName = view.context.getString(R.string.text_book)
            )
        )
//            lstCategoryBook.add(CategoryBookViewModel(categoryId = 4, categoryName = view.context.getString(R.string.text_ebook)))
//            lstCategoryBook.add(CategoryBookViewModel(categoryId = -1, categoryName = view.context.getString(R.string.text_thesis)))
//            lstCategoryBook.add(CategoryBookViewModel(categoryId = 3, categoryName = view.context.getString(R.string.text_speak_book)))
//            lstCategoryBook.add(CategoryBookViewModel(categoryId = 2, categoryName = view.context.getString(R.string.text_movie)))
//            lstCategoryBook.add(CategoryBookViewModel(categoryId = 1, categoryName = view.context.getString(R.string.text_picture)))
        val pagerAdapter = object : RouterPagerAdapter(this) {
            override fun configureRouter(router: Router, position: Int) {
                if (!router.hasRootController()) {
                    val categoryBookViewModel = lstCategoryBook[position]
                    val bundle = TabBookCategoryViewController.BundleOptions.create(
                        categoryId = categoryBookViewModel.categoryId,
                        boothId = boothId
                    )
                    val page: Controller = TabBookCategoryViewController(
                        targetController = this@SearchViewController,
                        bundle = bundle
                    )
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

        view.tlCategoryBook.addOnTabSelectedListener(object :
            TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                if (lstCategoryBook.size > position) {
                    val categoryBookViewModel = lstCategoryBook[position]
                    categoryIdCurrent = categoryBookViewModel.categoryId
                }
            }

        })

    }

    override fun onDestroyView(view: View) {
        KBus.unsubscribe(this)
        view.edtSearchBook.hideKeyboard()
        view.edtSearchBook.clearFocus()
        super.onDestroyView(view)
    }
}