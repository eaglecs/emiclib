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
            ReserverBookUseCase(useCaseExecution = get(),
                    appRepository = get())
        }
        factory {
            RequestDocumentUseCase(useCaseExecution = get(), appRepository = get())
        }
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
            GetInfoUserUseCase(appRepository = get(), useCaseExecution = get(), cacheRespository = get())
        }

        factory {
            ReservationBookUseCase(appRepository = get(),
                    useCaseExecution = get())
        }

        factory {
            GetItemInCollectionRecomandUseCase(appRepository = get(),
                    useCaseExecution = get())
        }

        factory {
            GetLoanHoldingCurrentUseCase(useCaseExecution = get(),
                    appRepository = get())
        }

        factory {
            GetLoanHoldingHistoryUseCase(useCaseExecution = get(),
                    appRepository = get())
        }
        factory {
            ReadMessageUseCase(useCaseExecution = get(),
                    appRepository = get())
        }
        factory {
            GetListMessageUseCase(useCaseExecution = get(),
                    appRepository = get())
        }
        factory {
            ChangePassUseCase(useCaseExecution = get(),
                    appRepository = get())
        }
        factory {
            GetListFQAUseCase(appRepository = get(),
                    useCaseExecution = get())
        }
        factory {
            GetListReserveRequestUseCase(appRepository = get(),
                    useCaseExecution = get())
        }
        factory {
            GetListReserveQueueUseCase(useCaseExecution = get(),
                    appRepository = get())
        }
        factory {
            FeedbackUseCase(useCaseExecution = get(),
                    appRepository = get())
        }
        factory {
            LoanRenewUseCase(appRepository = get(),
                    useCaseExecution = get())
        }
        factory {
            GetLoanHoldingRenewUseCase(appRepository = get(),
                    useCaseExecution = get())
        }
        factory {
            SaveBookUseCase(useCaseExecution = get(),
                    bookDataService = get())
        }
        factory {
            GetAllBookUseCase(useCaseExecution = get(),
                    bookDataService = get())
        }

        factory {
            GetListNewsUseCase(useCaseExecution = get(),
                    appRepository = get())
        }
        factory {
            SaveLoginRequestUseCase(useCaseExecution = get(), cacheRespository = get())
        }

        factory {
            GetLoginRequestUseCase(useCaseExecution = get(), cacheRespository = get())
        }
        factory {
            GetLoanHoldingRenewBookUseCase(useCaseExecution = get(), appRepository = get())
        }
    }
}