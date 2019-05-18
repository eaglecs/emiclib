package basecode.com.domain.features

import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.NewEBookResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class GetListNewEbookUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<NewEBookRequest, List<NewEBookResponse>, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: NewEBookRequest): Observable<List<NewEBookResponse>> {
        return appRepository.getListNewEBookItemsObservable(numberItem = input.numberItem, pageIndex = input.pageIndex, pageSize = input.pageSize)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse(msgError = "")
    }
}