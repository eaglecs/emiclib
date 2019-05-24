package basecode.com.domain.features

import basecode.com.domain.model.network.response.BookResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class FindAdvanceBookUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<FindAdvanceBookUseCase.Input, List<BookResponse>, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: Input): Observable<List<BookResponse>> {
        return appRepository.findBookAdvance(docType = input.docType, pageIndex = input.pageIndex, pageSize = input.pageSize, searchText = input.searchText, title = input.title,
                author = input.author, language = input.language)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }

    class Input(val docType: Int, val pageIndex: Int = 1, val pageSize: Int = 20, val searchText: String, val title: Int, val author: Int, val language: Int)
}