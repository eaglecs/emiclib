package basecode.com.domain.di

import basecode.com.domain.features.*
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
        factory {
            GetListBookRelatedUseCase(useCaseExecution = get(),
                    appRepository = get())
        }
        factory {
            GetListNewNewsUseCase(useCaseExecution = get(),
                    appRepository = get())
        }
        factory {
            GetUserTokenUseCase(useCaseExecution = get(), cacheRespository = get())
        }
        factory {
            LoginUseCase(useCaseExecution = get(), appRepository = get())
        }
        factory {
            SaveInfoLoginUseCase(useCaseExecution = get(), cacheRespository = get())
        }
        factory {
            FindAdvanceBookUseCase(useCaseExecution = get(), appRepository = get())
        }
        factory {
            FindBookUseCase(useCaseExecution = get(), appRepository = get())
        }
        factory {
            GetInfoBookUseCase(useCaseExecution = get(), appRepository = get())
        }
        factory {
            GetInfoUserUseCase(appRepository = get(), useCaseExecution = get())
        }

        factory {
            ReservationBookUseCase(appRepository = get(),
                    useCaseExecution = get())
        }
    }
}