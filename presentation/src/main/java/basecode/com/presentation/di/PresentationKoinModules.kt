package basecode.com.presentation.di

import basecode.com.presentation.features.bookborrow.BookBorrowContract
import basecode.com.presentation.features.bookborrow.BookBorrowPresenter
import basecode.com.presentation.features.bookdetail.BookDetailContract
import basecode.com.presentation.features.bookdetail.BookDetailPresenter
import basecode.com.presentation.features.books.BooksContract
import basecode.com.presentation.features.books.BooksPresenter
import basecode.com.presentation.features.home.HomeContract
import basecode.com.presentation.features.home.HomePresenter
import basecode.com.presentation.features.login.LoginContract
import basecode.com.presentation.features.login.LoginPresenter
import basecode.com.presentation.features.newnews.NewNewsContract
import basecode.com.presentation.features.newnews.NewNewsPresenter
import basecode.com.presentation.features.searchbook.SearchBookContract
import basecode.com.presentation.features.searchbook.SearchBookPresenter
import basecode.com.presentation.features.user.UserContract
import basecode.com.presentation.features.user.UserPresenter
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

object PresentationKoinModules {
    fun modules(): List<Module> {
        return listOf(homeModule)
    }

    private val homeModule = module {
        factory {
            HomePresenter(getInfoHomeUseCase = get(),
                    getUserTokenUseCase = get()) as HomeContract.Presenter
        }
        factory {
            BooksPresenter(getListNewBookUseCase = get(),
                    getListNewEbookUseCase = get(),
                    getItemInCollectionRecomandUseCase = get()) as BooksContract.Presenter
        }
        factory {
            BookDetailPresenter(getListBookRelatedUseCase = get(),
                    getUserTokenUseCase = get(),
                    getInfoBookUseCase = get(),
                    reservationBookUseCase = get()) as BookDetailContract.Presenter
        }
        factory {
            NewNewsPresenter(getListNewNewsUseCase = get()) as NewNewsContract.Presenter
        }
        factory {
            LoginPresenter(loginUseCase = get(), saveInfoLoginUseCase = get()) as LoginContract.Presenter
        }
        factory {
            SearchBookPresenter(findAdvanceBookUseCase = get(), searchBookUseCase = get()) as SearchBookContract.Presenter
        }
        factory {
            UserPresenter(getInfoUserUseCase = get(),
                    saveInfoLoginUseCase = get()) as UserContract.Presenter
        }
        factory {
            BookBorrowPresenter(getLoanHoldingCurrentUseCase = get(),
                    getLoanHoldingHistoryUseCase = get()) as BookBorrowContract.Presenter
        }
    }
}