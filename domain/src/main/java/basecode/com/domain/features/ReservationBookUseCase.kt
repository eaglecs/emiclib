package basecode.com.domain.features

import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.InfoBookResponse
import basecode.com.domain.model.network.response.InfoUserResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class ReservationBookUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<Int, Int, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: Int): Observable<Int> {
        return appRepository.reservationBook(input)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse(throwable.message.valueOrEmpty())
    }
}