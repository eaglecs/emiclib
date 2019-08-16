package basecode.com.domain.features

import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.features.mapper.UserModelMapper
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.InfoBookResponse
import basecode.com.domain.model.network.response.InfoUserResponse
import basecode.com.domain.model.network.response.UserModel
import basecode.com.domain.repository.CacheRespository
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class GetInfoUserUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository,
                         private val cacheRespository: CacheRespository) : UseCaseOutput<UserModel, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(): Observable<UserModel> {
        return Observable.create { e ->
            val userModel = cacheRespository.getUserModel()
            if (userModel == null) {
                val userResponse = appRepository.getInfoUser().blockingGet()
                if (userResponse != null) {
                    val result = UserModelMapper().map(userResponse)
                    cacheRespository.saveUserModel(result)
                    e.onNext(result)
                } else {
                    e.onError(Throwable())
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