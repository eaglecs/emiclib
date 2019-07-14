package basecode.com.domain.features

import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.InfoBookResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class GetInfoBookUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<Long, InfoBookResponse, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: Long): Observable<InfoBookResponse> {
        return appRepository.getInfoBook(input)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse(throwable.message.valueOrEmpty())
    }
}