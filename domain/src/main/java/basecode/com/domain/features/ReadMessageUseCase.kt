package basecode.com.domain.features

import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.MessageResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class ReadMessageUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<Long, Any, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: Long): Observable<Any> {
        return appRepository.readMessage(input)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }
}