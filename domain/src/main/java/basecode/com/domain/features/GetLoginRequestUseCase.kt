package basecode.com.domain.features

import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.repository.CacheRespository
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class GetLoginRequestUseCase(useCaseExecution: UseCaseExecution, private val cacheRepository: CacheRespository) : UseCaseOutput<LoginRequest, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(): Observable<LoginRequest> {
        return Observable.create { e ->
            val loginRequest = cacheRepository.getLoginRequest()
            if (loginRequest == null) {
                e.onError(Throwable())
            } else {
                loginRequest.let {
                    if (it.username.isEmpty() || it.password.isEmpty()){
                        e.onError(Throwable())
                    }else {
                        e.onNext(loginRequest)
                    }
                }
            }
            e.onComplete()
        }
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }

}