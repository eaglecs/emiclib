package basecode.com.ui.di

import basecode.com.ui.util.DoubleTouchPrevent
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

object UIKoinModules {
    fun modules(): List<Module> {
        return listOf(uiModule)
    }

    private val uiModule = module {
        factory {
            DoubleTouchPrevent()
        }
    }
}