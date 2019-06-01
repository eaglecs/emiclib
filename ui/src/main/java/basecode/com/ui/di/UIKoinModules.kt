package basecode.com.ui.di

import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.ui.base.executor.AndroidUseCaseExecution
import basecode.com.data.epub.SkyDatabase
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
        single {
            AndroidUseCaseExecution() as UseCaseExecution
        }

        single {
            basecode.com.data.epub.SkyDatabase(get())
        }


    }
}