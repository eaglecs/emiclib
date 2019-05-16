package basecode.com.domain.di

import basecode.com.domain.features.GetListNewEbookItemsUseCase
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

object DomainKoinModules {
    @JvmStatic
    fun modules(): List<Module> {
        return listOf(appModule)
    }

    private val appModule = module {
        factory {
            GetListNewEbookItemsUseCase(useCaseExecution = get(),
                    appRepository = get())
        }
    }
}