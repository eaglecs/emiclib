package basecode.com.domain.features

import basecode.com.domain.model.network.response.BooksDetailResponse
import basecode.com.domain.model.network.response.BoothsResponse
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class GetCheckInCurrentLoanInforUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<String, BooksDetailResponse, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: String): Observable<BooksDetailResponse> {
        return appRepository.getCheckInCurrentLoanInfor(input)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }
}