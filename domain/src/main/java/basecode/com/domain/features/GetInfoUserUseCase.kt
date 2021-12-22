package basecode.com.domain.features

import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.mapper.UserModelMapper
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.UserModel
import basecode.com.domain.repository.CacheRespository
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class GetInfoUserUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository,
                         private val cacheRepository: CacheRespository) : UseCaseOutput<UserModel, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(): Observable<UserModel> {
        return Observable.create { e ->
            val userModel = cacheRepository.getUserModel()
            if (userModel == null || (userModel.email.isEmpty() && userModel.phone.isEmpty())) {
                if (cacheRepository.getLoginRequest() == null) {
                    e.onError(Throwable())
                } else {
                    val userResponse = appRepository.getInfoUser().blockingGet()
                    if (userResponse != null) {
                        val result = UserModelMapper().map(userResponse)
                        cacheRepository.saveUserModel(result)
                        e.onNext(result)
                    } else {
                        e.onError(Throwable())
                    }
                }
            } else {
                e.onNext(userModel)
            }
            e.onComplete()
        }
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse(throwable.message.valueOrEmpty())
    }
}