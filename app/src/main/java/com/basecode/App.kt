package com.basecode

import android.app.Activity
import android.os.Bundle
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import androidx.appcompat.app.AppCompatDelegate
import basecode.com.data.di.DataKoinModules
import basecode.com.domain.di.DomainKoinModules
import basecode.com.presentation.di.PresentationKoinModules
import basecode.com.ui.di.UIKoinModules
import basecode.com.domain.model.epub.SkySetting
import com.franmontiel.localechanger.LocaleChanger
import io.paperdb.Paper
import org.koin.dsl.module.Module
import org.koin.standalone.StandAloneContext.startKoin
import timber.log.Timber
import java.util.*

class App : MultiDexApplication() {
//    var message = "We are the world."
//    var bis: ArrayList<BookInformation> = arrayListOf()
//    var setting: SkySetting? = null
//    var sd: SkyDatabase? = null
//    var sortType = 0
//    var keyManager: SkyKeyManager? = null

    fun getApplicationName(): String {
        val stringId = this.applicationInfo.labelRes
        return this.getString(stringId)
    }

    private val SUPPORTED_LOCALES = Arrays.asList(
            LOCALE_US,
            LOCALE_VN
    )

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    companion object {
        @JvmStatic
        lateinit var appContext: App
        val LOCALE_US = Locale("en", "US")
        val LOCALE_VN = Locale("vi", "VN")
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        MultiDex.install(this)
        Paper.init(this)
        initKoinDI()
        initLogger()
        initChangeLanguge()
        initLogActivity()

        val appName = this.getApplicationName()
        if (SkySetting.getStorageDirectory() == null) {
            SkySetting.setStorageDirectory(filesDir.absolutePath, appName)
        }
    }

    private fun initLogActivity() {
        if (BuildConfig.DEBUG) {
            this.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
                override fun onActivityPaused(activity: Activity?) {
                    activity?.let {
                        Timber.d("onActivityPaused ${activity.javaClass.simpleName}")
                    }
                }

                override fun onActivityResumed(activity: Activity?) {
                    activity?.let {
                        Timber.d("onActivityResumed ${activity.javaClass.simpleName}")
                    }
                }

                override fun onActivityStarted(activity: Activity?) {
                    activity?.let {
                        Timber.d("onActivityStarted ${activity.javaClass.simpleName}")
                    }
                }

                override fun onActivityDestroyed(activity: Activity?) {
                    activity?.let {
                        Timber.d("onActivityDestroyed ${activity.javaClass.simpleName}")
                    }
                }

                override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                    activity?.let {
                        Timber.d("onActivitySaveInstanceState ${activity.javaClass.simpleName}")
                    }
                }

                override fun onActivityStopped(activity: Activity?) {
                    activity?.let {
                        Timber.d("onActivityStopped ${activity.javaClass.simpleName}")
                    }
                }

                override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                    activity?.let {
                        Timber.d("onActivityCreated ${activity.javaClass.simpleName}")
                    }
                }

            })
        }
    }


    private fun initChangeLanguge() {
        LocaleChanger.initialize(applicationContext, SUPPORTED_LOCALES)
    }

    private fun initKoinDI() {
        val modules: MutableList<Module> = mutableListOf()
        modules.addAll(AppKoinModules.modules())
        modules.addAll(UIKoinModules.modules())
        modules.addAll(PresentationKoinModules.modules())
        modules.addAll(DomainKoinModules.modules())
        modules.addAll(DataKoinModules.modules())
        startKoin(modules)
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
