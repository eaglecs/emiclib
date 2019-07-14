package basecode.com.presentation.di

import basecode.com.presentation.features.bookborrow.BookBorrowContract
import basecode.com.presentation.features.bookborrow.BookBorrowPresenter
import basecode.com.presentation.features.bookdetail.BookDetailContract
import basecode.com.presentation.features.bookdetail.BookDetailPresenter
import basecode.com.presentation.features.books.BooksContract
import basecode.com.presentation.features.books.BooksPresenter
import basecode.com.presentation.features.changepass.ChangePassContract
import basecode.com.presentation.features.changepass.ChangePassPresenter
import basecode.com.presentation.features.downloadboook.DownloadBookContract
import basecode.com.presentation.features.downloadboook.DownloadBookPresenter
import basecode.com.presentation.features.feedback.FeedbackContract
import basecode.com.presentation.features.feedback.FeedbackPresenter
import basecode.com.presentation.features.fqa.FQAContract
import basecode.com.presentation.features.fqa.FQAPresenter
import basecode.com.presentation.features.home.HomeContract
import basecode.com.presentation.features.home.HomePresenter
import basecode.com.presentation.features.login.LoginContract
import basecode.com.presentation.features.login.LoginPresenter
import basecode.com.presentation.features.newnews.NewNewsContract
import basecode.com.presentation.features.newnews.NewNewsPresenter
import basecode.com.presentation.features.notify.NotifyContract
import basecode.com.presentation.features.notify.NotifyPresenter
import basecode.com.presentation.features.renew.RenewContract
import basecode.com.presentation.features.renew.RenewPresenter
import basecode.com.presentation.features.searchbook.SearchBookContract
import basecode.com.presentation.features.searchbook.SearchBookPresenter
import basecode.com.presentation.features.setting.SettingContract
import basecode.com.presentation.features.setting.SettingPresenter
import basecode.com.presentation.features.user.*
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
                    reservationBookUseCase = get(),
                    saveBookUseCase = get()) as BookDetailContract.Presenter
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
        factory {
            NotifyPresenter(getListMessageUseCase = get(),
                    readMessageUseCase = get()) as NotifyContract.Presenter
        }
        factory {
            ChangePassPresenter(changePassUseCase = get()) as ChangePassContract.Presenter
        }

        factory {
            RenewPresenter(getLoanHoldingRenewUseCase = get(),
                    loanRenewUseCase = get()) as RenewContract.Presenter
        }

        factory {
            ReserveQueueRequestPresenter(getListReserveQueueUseCase = get(),
                    getListReserveRequestUseCase = get()) as ReserveQueueRequestContract.Presenter
        }

        factory {
            FQAPresenter(getListFQAUseCase = get()) as FQAContract.Presenter
        }

        factory {
            FeedbackPresenter(feedbackUseCase = get()) as FeedbackContract.Presenter
        }

        factory {
            DownloadBookPresenter(getAllBookUseCase = get()) as DownloadBookContract.Presenter
        }

        factory {
            SettingPresenter(getUserTokenUseCase = get()) as SettingContract.Presenter
        }
    }
}