package basecode.com.domain.features

import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.NewEBookResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class GetListNewEbookItemsUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<NewEBookRequest, NewEBookResponse, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: NewEBookRequest): Observable<NewEBookResponse> {
        return appRepository.getListNewEBookItems(input)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse(msgError = "")
    }
}