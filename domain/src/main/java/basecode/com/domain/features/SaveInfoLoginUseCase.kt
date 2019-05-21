package basecode.com.domain.features

import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.repository.CacheRespository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class SaveInfoLoginUseCase(useCaseExecution: UseCaseExecution, private val cacheRespository: CacheRespository) : UseCase<SaveInfoLoginUseCase.Input, Boolean, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: Input): Observable<Boolean> {
        return cacheRespository.saveInfoLogin(input.accessToken, input.tokenType)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }

    class Input(val accessToken: String, val tokenType: String)
}