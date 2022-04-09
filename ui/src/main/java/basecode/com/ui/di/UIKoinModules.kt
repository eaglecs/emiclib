package basecode.com.ui.di

import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.ui.base.executor.AndroidUseCaseExecution
import basecode.com.ui.util.*
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

object UIKoinModules {
    fun modules(): List<Module> {
        return listOf(uiModule)
    }

    private val uiModule = module {
        factory {
            DeviceUtil(context = get())
        }
        factory {
            DoubleTouchPrevent()
        }
        single {
            AndroidUseCaseExecution() as UseCaseExecution
        }

        single {
            PermissionUtil(context = get())
        }

        single {
            LocationUtil(context = get())
        }
    }
}