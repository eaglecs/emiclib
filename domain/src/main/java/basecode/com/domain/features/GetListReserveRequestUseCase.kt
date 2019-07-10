package basecode.com.domain.features

import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.MessageResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.model.network.response.ReserveResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class GetListReserveRequestUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCaseOutput<List<ReserveResponse>, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(): Observable<List<ReserveResponse>> {
        return appRepository.getListReserveRequest()
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }

}