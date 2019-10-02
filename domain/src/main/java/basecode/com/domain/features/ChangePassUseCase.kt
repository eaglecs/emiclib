package basecode.com.domain.features

import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.MessageResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class ChangePassUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<ChangePassUseCase.Input, Int, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: Input): Observable<Int> {
        return appRepository.changePass(newPassword = input.newPassword, oldPassword = input.oldPassword)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }

    class Input(val newPassword: String, val oldPassword: String)
}