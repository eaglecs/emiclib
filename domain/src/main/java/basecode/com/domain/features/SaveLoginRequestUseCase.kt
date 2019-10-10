package basecode.com.domain.features

import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.repository.CacheRespository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class SaveLoginRequestUseCase(useCaseExecution: UseCaseExecution, private val cacheRespository: CacheRespository) : UseCase<LoginRequest, Boolean, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: LoginRequest): Observable<Boolean> {
        return Observable.create{ e ->
            cacheRespository.saveLoginRequest(input)
            e.onNext(true)
            e.onComplete()
        }
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }

}