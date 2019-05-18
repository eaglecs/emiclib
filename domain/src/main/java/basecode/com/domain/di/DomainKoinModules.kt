package basecode.com.domain.di

import basecode.com.domain.features.GetInfoHomeUseCase
import basecode.com.domain.features.GetListNewBookUseCase
import basecode.com.domain.features.GetListNewEbookUseCase
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

object DomainKoinModules {
    @JvmStatic
    fun modules(): List<Module> {
        return listOf(appModule)
    }

    private val appModule = module {
        factory {
            GetListNewEbookUseCase(useCaseExecution = get(),
                    appRepository = get())
        }
        factory {
            GetInfoHomeUseCase(useCaseExecution = get(),
                    appRepository = get())
        }

        factory {
            GetListNewBookUseCase(useCaseExecution = get(),
                    appRepository = get())
        }
    }
}