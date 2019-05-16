package basecode.com.presentation.di

import basecode.com.presentation.features.home.HomeContract
import basecode.com.presentation.features.home.HomePresenter
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

object PresentationKoinModules {
    fun modules(): List<Module> {
        return listOf(homeModule)
    }

    private val homeModule = module {
        factory {
            HomePresenter(getListNewEbookItemsUseCase = get()) as HomeContract.Presenter
        }
    }
}