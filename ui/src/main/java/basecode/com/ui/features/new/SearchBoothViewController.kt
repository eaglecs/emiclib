package basecode.com.ui.features.new

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import basecode.com.domain.eventbus.KBus
import basecode.com.domain.eventbus.model.LogoutSuccessEventBus
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.extention.valueOrFalse
import basecode.com.domain.model.bus.ComeHomeScreenEventBus
import basecode.com.domain.model.bus.LoginSuccessEventBus
import basecode.com.domain.util.ConstApp
import basecode.com.presentation.features.new.SearchBoothContract
import basecode.com.presentation.features.new.model.BoothViewModel
import basecode.com.ui.R
import basecode.com.ui.base.controller.screenchangehandler.FadeChangeHandler
import basecode.com.ui.base.controller.screenchangehandler.HorizontalChangeHandler
import basecode.com.ui.base.controller.viewcontroller.ViewController
import basecode.com.ui.base.extra.BundleExtraBoolean
import basecode.com.ui.base.extra.BundleExtraString
import basecode.com.ui.base.extra.BundleOptionsCompanion
import basecode.com.ui.base.listview.view.LinearRenderConfigFactory
import basecode.com.ui.base.listview.view.RecyclerViewController
import basecode.com.ui.features.home.tab.SearchViewController
import basecode.com.ui.features.login.LoginViewController
import basecode.com.ui.features.new.mapper.BoothViewHolderModelMapper
import basecode.com.ui.features.new.renderer.BoothRenderer
import basecode.com.ui.features.user.UserViewController
import basecode.com.ui.util.DoubleTouchPrevent
import basecode.com.ui.util.GlideUtil
import basecode.com.ui.util.LocationUtil
import basecode.com.ui.util.ScanQRCode
import com.bluelinelabs.conductor.RouterTransaction
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import kotlinx.android.synthetic.main.layout_custom_market_booth.view.*
import kotlinx.android.synthetic.main.layout_header_new_app.view.*
import kotlinx.android.synthetic.main.screen_search_booth.view.*
import org.koin.standalone.inject

class SearchBoothViewController(bundle: Bundle) : ViewController(bundle), OnMapReadyCallback,
    SearchBoothContract.View, GoogleMap.OnMarkerClickListener {
    private val locationUtil by inject<LocationUtil>()
    private val doubleTouchPrevent by inject<DoubleTouchPrevent>()
    private val presenter by inject<SearchBoothContract.Presenter>()
    private lateinit var googleMap: GoogleMap
    private var mapFragment: SupportMapFragment? = null
    private lateinit var rvController: RecyclerViewController
    private var isLogin = false
    private var avatar: String = ""
    private val lstAllBooth = mutableListOf<BoothViewModel>()
    private val lstMarker = mutableListOf<Marker>()
    private val booths = mutableListOf<BoothViewModel>()

    object BundleOptions {
        var Bundle.isLogin by BundleExtraBoolean("isLogin")
        var Bundle.avatar by BundleExtraString("avatar")
        fun create(isLogin: Boolean, avatar: String) = Bundle().apply {
            this.isLogin = isLogin
            this.avatar = avatar
        }
    }

    companion object : BundleOptionsCompanion<BundleOptions>(BundleOptions)

    init {
        bundle.options { options ->
            isLogin = options.isLogin.valueOrFalse()
            avatar = options.avatar.valueOrEmpty()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_search_booth, container, false)
    }

    override fun initPostCreateView(view: View) {
        presenter.attachView(this)
        initView(view)
        initEventBus(view)
        handleView(view)
        presenter.getBooths()
    }

    private fun initEventBus(view: View) {
        KBus.subscribe<ComeHomeScreenEventBus>(this){
            router.popController(this)
        }
        KBus.subscribe<LoginSuccessEventBus>(this) {
            isLogin = true
            this.avatar = it.avatar
            GlideUtil.loadImage(
                url = it.avatar,
                imageView = view.ivLogin,
                holderImage = R.drawable.user_default,
                errorImage = R.drawable.user_default
            )
        }
        KBus.subscribe<LogoutSuccessEventBus>(this) {
            isLogin = false
            avatar = ""
            view.ivLogin.setImageResource(R.drawable.ic_login)
        }
    }

    private fun initView(view: View) {
        val input = LinearRenderConfigFactory.Input(
            context = view.context,
            orientation = LinearRenderConfigFactory.Orientation.VERTICAL
        )
        val renderConfig = LinearRenderConfigFactory(input).create()
        rvController = RecyclerViewController(view.rvBooth, renderConfig)
        rvController.addViewRenderer(BoothRenderer { model ->
            val bundle = SearchViewController.BundleOptions.create(model.id)
            router.pushController(
                RouterTransaction.with(SearchViewController(bundle))
                    .pushChangeHandler(HorizontalChangeHandler(ConstApp.timeEffectScreen, false))
                    .popChangeHandler(HorizontalChangeHandler(ConstApp.timeEffectScreen))
            )
        })
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance()
            mapFragment?.apply {
                getMapAsync(this@SearchBoothViewController)
            }
            activity?.let { activity ->
                if (activity is AppCompatActivity) {
                    mapFragment?.let { mapFragment ->
                        activity.supportFragmentManager.beginTransaction()
                            .replace(R.id.vgMap, mapFragment).commitAllowingStateLoss()
                    }
                }
            }
        }

        if (isLogin) {
            GlideUtil.loadImage(
                url = avatar,
                imageView = view.ivLogin,
                holderImage = R.drawable.user_default,
                errorImage = R.drawable.user_default
            )
        } else {
            view.ivLogin.setImageResource(R.drawable.ic_login)
        }
    }

    private fun handleView(view: View) {
        view.vgComeHome.setOnClickListener {
            if (doubleTouchPrevent.check("vgComeHome")) {
                KBus.post(ComeHomeScreenEventBus())
            }
        }
        view.ivLogin.setOnClickListener {
            if (doubleTouchPrevent.check("ivLogin")) {
                if (isLogin) {
                    router.pushController(
                        RouterTransaction.with(
                            UserViewController()
                        ).pushChangeHandler(FadeChangeHandler(false))
                    )
                } else {
                    val bundle =
                        LoginViewController.BundleOptions.create(LoginSuccessEventBus.Type.Normal.value)
                    val loginViewController = LoginViewController(bundle)
                    router.pushController(
                        RouterTransaction.with(loginViewController)
                            .pushChangeHandler(FadeChangeHandler(false))
                    )
                }
            }
        }

        view.ivScanQRCode.setOnClickListener {
            if (doubleTouchPrevent.check("ivScanQRCode")) {
                ScanQRCode.openScreenQRCode(activity, this)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        activity?.let { activity ->
            MapsInitializer.initialize(activity)
            this.googleMap = googleMap
            googleMap.setOnMarkerClickListener(this)
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                googleMap.isMyLocationEnabled = true
            }
            with(googleMap) {
                uiSettings.isMapToolbarEnabled = false
                uiSettings.isZoomControlsEnabled = true
                mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }
        initDataMap()
    }

    @SuppressLint("MissingPermission")
    private fun initDataMap() {
        if (::googleMap.isInitialized && lstAllBooth.isNotEmpty()) {
            with(googleMap) {
                setOnMapLoadedCallback {
                    activity?.let { activity ->
                        if (ContextCompat.checkSelfPermission(
                                activity,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                                activity,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            googleMap.isMyLocationEnabled = true
                        }
                    }
                    mapType = GoogleMap.MAP_TYPE_NORMAL
                    uiSettings.isMapToolbarEnabled = false
                    uiSettings.isZoomControlsEnabled = true
                    drawMarkerBooth(googleMap = googleMap, lstBooth = lstAllBooth)
                    locationUtil.getLatLongCurrent { latitude, longitude ->
                        val myLocation = LatLng(latitude, longitude)
                        val markerOptions = MarkerOptions()
                            .position(myLocation)
                            .title("Vị trí của bạn")
                        val addMarker = addMarker(markerOptions)
                        addMarker?.showInfoWindow()
                        moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16f))
                    }
                }

            }
        }
    }

    private fun drawMarkerBooth(googleMap: GoogleMap, lstBooth: List<BoothViewModel>) {
        activity?.let { context ->
            lstMarker.clear()
            booths.clear()
            val iconFactory = IconGenerator(context)
            val myInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val activityView = myInflater.inflate(R.layout.layout_custom_market_booth, null, false)
            iconFactory.setContentView(activityView)
            iconFactory.setBackground(null)
            lstBooth.forEach { booth ->
                activityView.tvBoothName.text = booth.codeLoc
                val markerOptions = MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon()))
                    .position(LatLng(booth.bLatitude, booth.bLongitude))
                    .anchor(iconFactory.anchorU, iconFactory.anchorV)
                val marker = googleMap.addMarker(markerOptions)
                marker?.let {
                    booths.add(booth)
                    lstMarker.add(marker)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun showBooths(lstBooth: List<BoothViewModel>) {
        this.lstAllBooth.clear()
        this.lstAllBooth.addAll(lstBooth)
        locationUtil.getLatLongCurrent { latitude, longitude ->
            val input =
                BoothViewHolderModelMapper.Input(lat = latitude, lng = longitude, booths = lstBooth)
            val booths = BoothViewHolderModelMapper().map(input).sortedBy { it.distanceValue }
            rvController.setItems(booths)
            rvController.notifyDataChanged()
        }
        initDataMap()
    }

    override fun showErrorGetBooths() {

    }

    override fun showLoading() {
        view?.apply {
            vgLoading.show()
        }
    }

    override fun hideLoading() {
        view?.apply {
            vgLoading.hide()
        }
    }

    override fun onDestroyView(view: View) {
        presenter.detachView()
        super.onDestroyView(view)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (lstMarker.contains(marker)){
            val index = lstMarker.indexOf(marker)
            val boothViewModel = booths[index]

            val bundle = SearchViewController.BundleOptions.create(boothViewModel.id)
            router.pushController(
                RouterTransaction.with(SearchViewController(bundle))
                    .pushChangeHandler(HorizontalChangeHandler(ConstApp.timeEffectScreen, false))
                    .popChangeHandler(HorizontalChangeHandler(ConstApp.timeEffectScreen))
            )
        }
        return true
    }
}

// search booth: removeAccentAndSpace