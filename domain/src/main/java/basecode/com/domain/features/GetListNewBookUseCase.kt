package basecode.com.domain.features

import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.BookResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class GetListNewBookUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<NewEBookRequest, List<BookResponse>, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: NewEBookRequest): Observable<List<BookResponse>> {
        return appRepository.getListNewItemsObservable(numberItem = input.numberItem, pageSize = input.pageSize, pageIndex = input.pageIndex)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse(msgError = throwable.message.valueOrEmpty())
    }
}