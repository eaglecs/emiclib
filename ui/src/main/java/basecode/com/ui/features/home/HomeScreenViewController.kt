package basecode.com.ui.features.home

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.presentation.features.home.HomeContract
import basecode.com.ui.R
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraLong
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.features.home.tab.*
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
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
        initView(view)
    }

    private val indexTabHome = 0
    private val indexTabNews = 1
    private val indexTabSearch = 2
    private val indexTabProfile = 3
    private val indexTabSetting = 4
    private val iconSizeOfTab: Float = 28.toFloat()
    private val pageHomeSize = 5
    private var prevMenuItem: MenuItem? = null

    private fun initView(view: View) {
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    view.vpHome.currentItem = indexTabHome
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_news -> {
                    view.vpHome.currentItem = indexTabNews
                    return@OnNavigationItemSelectedListener true
                }
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
                        indexTabNews -> {
                            page = NewsViewController(targetController)
                        }
                        indexTabSearch -> {
                            page = SearchViewController(targetController)
                        }
                        indexTabProfile -> {
                            page = ProfileViewController(targetController)
                        }
                        indexTabSetting -> {
                            page = SettingViewController(targetController)
                        }
                    }
                    page?.let {
                        router.setRoot(RouterTransaction.with(page).tag("HomeViewController$position"))
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
}