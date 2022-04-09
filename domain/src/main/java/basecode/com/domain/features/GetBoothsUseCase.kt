package basecode.com.domain.features

import basecode.com.domain.model.network.response.BoothsResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class GetBoothsUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCaseOutput<BoothsResponse, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(): Observable<BoothsResponse> {
        return appRepository.getListBooth()
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }
}