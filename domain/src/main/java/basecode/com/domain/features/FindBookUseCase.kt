package basecode.com.domain.features

import basecode.com.domain.model.network.response.BookResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class FindBookUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<FindBookUseCase.Input, List<BookResponse>, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: Input): Observable<List<BookResponse>> {
        return appRepository.findBook(docType = input.docType, pageIndex = input.pageIndex, pageSize = input.pageType, searchText = input.searchText)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }

    class Input(val docType: Int, val pageIndex: Int, val pageType: Int, val searchText: String)
}