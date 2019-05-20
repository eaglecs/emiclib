package basecode.com.domain.features

import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.repository.CacheRespository
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class GetUserTokenUseCase(useCaseExecution: UseCaseExecution, private val cacheRespository: CacheRespository) : UseCaseOutput<String, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(): Observable<String> {
        return cacheRespository.getUserToken()
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }
}