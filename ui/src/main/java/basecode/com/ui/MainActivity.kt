package basecode.com.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import basecode.com.ui.features.home.HomeScreenViewController
import basecode.com.ui.util.DoubleTouchPrevent
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.franmontiel.localechanger.LocaleChanger
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val timeDelayWhenClickBack = 2000
    private var timeBackPressed: Long = 0
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
            val bundle = HomeScreenViewController.BundleOption.create(resId = 1)
            router.setRoot(RouterTransaction.with(HomeScreenViewController(bundle)))
        }
    }

    override fun onBackPressed() {
        if (router.backstackSize == 1) {
            val isHandleBack: Boolean
            if (timeBackPressed + timeDelayWhenClickBack > System.currentTimeMillis()) {
                isHandleBack = false
            } else {
                isHandleBack = true
                Toasty.warning(this, this.getString(R.string.check_double_click_exit_app)).show()
            }
            timeBackPressed = System.currentTimeMillis()
            if (!isHandleBack) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask()
                } else {
                    super.onBackPressed()
                }
            }
        } else {
            if (!router.handleBack()) {
                super.onBackPressed()
            }
        }
    }
}
