package basecode.com.domain.features

import basecode.com.domain.model.network.request.RequestDocumentRequest
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.MessageResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class RequestDocumentUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<RequestDocumentRequest, Int, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: RequestDocumentRequest): Observable<Int> {
        return appRepository.requestDocument(fullName = input.fullName,
                title = input.title,
                author = input.author,
                email = input.email,
                facebook = input.facebook,
                groupName = input.groupName,
                information = input.information,
                patronCode = input.patronCode,
                phone = input.phone,
                publisher = input.publisher,
                publishYear = input.publishYear,
                supplier = input.supplier)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }
}