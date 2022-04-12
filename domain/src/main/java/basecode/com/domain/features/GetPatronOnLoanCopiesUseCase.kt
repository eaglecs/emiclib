package basecode.com.domain.features

import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.GetPatronOnLoanCopiesResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class GetPatronOnLoanCopiesUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCaseOutput<GetPatronOnLoanCopiesResponse, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(): Observable<GetPatronOnLoanCopiesResponse> {
        return appRepository.getPatronOnLoanCopies()
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }
}