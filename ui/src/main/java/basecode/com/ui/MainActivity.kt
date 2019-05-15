package basecode.com.ui

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import basecode.com.ui.features.splash.SplashScreenViewController
import basecode.com.ui.util.DoubleTouchPrevent
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.franmontiel.localechanger.LocaleChanger
import kotlinx.android.synthetic.main.layout_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() { // KoinComponent(add inject)
    private val doubleTouchPrevent: DoubleTouchPrevent by inject()
    private lateinit var router: Router
    override fun attachBaseContext(newBase: Context) {
        val newBaseUpdate = LocaleChanger.configureBaseContext(newBase)
        super.attachBaseContext(newBaseUpdate)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        router = Conductor.attachRouter(this, this.controllerContainer, savedInstanceState)
        if (!router.hasRootController()) {
            val bundle = SplashScreenViewController.BundleOption.create(resId = 1)
            router.setRoot(RouterTransaction.with(SplashScreenViewController(bundle)))
        }
    }
}

// Set language for app.
//private fun setLocaleForApp(locale: Locale) {
//    activity?.let { activity ->
//        LocaleChanger.setLocale(locale)
//        ActivityRecreationHelper.recreate(activity, true)
//    }
//}
