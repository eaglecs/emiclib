package basecode.com.domain.features

import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.MessageResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class FeedbackUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<FeedbackUseCase.Input, Int, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: FeedbackUseCase.Input): Observable<Int> {
        return appRepository.feedback(content = input.content, email = input.email, title = input.title,
                mobilePhone = input.mobilePhone, userFullName = input.userFullName)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }

    class Input(val content: String, val email: String,
                val mobilePhone: String, val title: String,
                val userFullName: String)
}