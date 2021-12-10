package basecode.com.domain.features

import basecode.com.domain.model.network.response.BookRecommendResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class GetBooksRecommendUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<GetBooksRecommendUseCase.Input, List<BookRecommendResponse>, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: Input): Observable<List<BookRecommendResponse>> {
        return appRepository.getBooksRecommendObservable(pageIndex = input.pageIndex, pageSize = input.pageSize)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }
    class Input(val pageIndex: Int, val pageSize: Int )
}