package basecode.com.domain.features

import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class LoanRenewUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<String, Any, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: String): Observable<Any> {
        return appRepository.loanRenew(input)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }
}