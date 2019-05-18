package basecode.com.domain.features

import basecode.com.domain.model.network.response.BookResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class GetListBookRelatedUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<Int, List<BookResponse>, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: Int): Observable<List<BookResponse>> {
        return appRepository.getListBookRelated(itemId = input, pageSize = 20, pageIndex = 1)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }
}