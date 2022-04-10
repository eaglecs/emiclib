package basecode.com.ui.features.home

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.ResultScanQRCodeEventBus
import basecode.com.domain.model.bus.LoadStatusNotify
import basecode.com.domain.model.bus.SelectedHomeEventBus
import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.presentation.features.home.HomeContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraLong
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.features.bookdetail.BookDetailViewController
import basecode.com.ui.features.home.tab.*
import com.bluelinelabs.conductor.*
import com.bluelinelabs.conductor.support.RouterPagerAdapter
import kotlinx.android.synthetic.main.screen_home.view.*
import org.koin.standalone.inject

class HomeScreenViewController(bundle: Bundle) : ViewController(bundle) {
    private var resId: Long = 0


    object BundleOption {
        var Bundle.resId by BundleExtraLong("currentOptionReceive")
        fun create(resId: Long) = Bundle().apply {
            this.resId = resId
        }
    }

    companion object : BundleOptionsCompanion<BundleOption>(BundleOption)

    init {
        bundle.options { options ->
            resId = options.resId
        }
    }

    override fun initPostCreateView(view: View) {
        initEventBus(view)
        initView(view)
    }

    private fun initEventBus(view: View) {
        KBus.subscribe<SelectedHomeEventBus>(this) {
            view.navigation.currentItem = indexTabHome
        }
        KBus.subscribe<ResultScanQRCodeEventBus>(this) {
            val bundle =
                BookDetailViewController.BundleOptions.create(
                    bookId = it.bookId,
                    photo = "",
                    docType = it.docType
                )
            router.pushController(
                RouterTransaction.with(
                    BookDetailViewController(bundle)
                ).pushChangeHandler(FadeChangeHandler(false))
            )
        }
    }

    private val indexTabHome = 0

    //    private val indexTabNews = 1
    private val indexTabSearch = 1
    private val indexTabProfile = 2
    private val indexTabSetting = 3
    private val iconSizeOfTab: Float = 28.toFloat()
    private val pageHomeSize = 4
    private var prevMenuItem: MenuItem? = null
    private var isFirstEnter = true
    override fun onChangeEnded(
        changeHandler: ControllerChangeHandler,
        changeType: ControllerChangeType
    ) {
        super.onChangeEnded(changeHandler, changeType)
        if (changeType.isEnter) {
            if (isFirstEnter) {
                isFirstEnter = false
            } else {
                KBus.post(LoadStatusNotify())
            }
        }
    }

    private fun initView(view: View) {
        val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        view.vpHome.currentItem = indexTabHome
                        return@OnNavigationItemSelectedListener true
                    }
//                R.id.navigation_news -> {
//                    view.vpHome.currentItem = indexTabNews
//                    return@OnNavigationItemSelectedListener true
//                }
                    R.id.navigation_search -> {
                        view.vpHome.currentItem = indexTabSearch
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_profile -> {
                        view.vpHome.currentItem = indexTabProfile
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_setting -> {
                        view.vpHome.currentItem = indexTabSetting
                        return@OnNavigationItemSelectedListener true
                    }
                    else -> {
                        view.vpHome.currentItem = indexTabHome
                        return@OnNavigationItemSelectedListener true
                    }
                }

            }
        view.navigation.onNavigationItemSelectedListener = mOnNavigationItemSelectedListener
        view.navigation.setIconSize(iconSizeOfTab)
        val targetController = this
        val pagerAdapter = object : RouterPagerAdapter(targetController) {
            override fun configureRouter(router: Router, position: Int) {
                if (!router.hasRootController()) {
                    var page: Controller? = null
                    when (position) {
                        indexTabHome -> {
                            page = HomeViewController(targetController)
                        }
//                        indexTabNews -> {
//                            page = NewsViewController(targetController)
//                        }
                        indexTabSearch -> {
//                            page = SearchViewController(targetController)
                        }
//                        indexTabProfile -> {
//                            page = ProfileViewController(targetController)
//                        }
                        indexTabSetting -> {
                            page = SettingViewController(targetController)
                        }
                    }
                    page?.let {
                        router.setRoot(
                            RouterTransaction.with(page).tag("HomeViewController$position")
                        )
                    }
                }
            }

            override fun getCount(): Int {
                return pageHomeSize
            }
        }
        view.vpHome.adapter = pagerAdapter
        view.vpHome.offscreenPageLimit = pageHomeSize
        view.vpHome.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position == indexTabProfile) {
                    KBus.post(LoadStatusNotify())
                }
                prevMenuItem?.let { prevMenuItem ->
                    prevMenuItem.setChecked(false)
                }
                val menuItem = view.navigation.menu.getItem(position)
                menuItem.isChecked = true
                prevMenuItem = menuItem
            }

        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_home, container, false)
    }

    override fun onDestroyView(view: View) {
        KBus.unsubscribe(this)
        super.onDestroyView(view)
    }
}