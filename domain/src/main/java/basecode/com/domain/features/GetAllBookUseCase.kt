package basecode.com.domain.features

import basecode.com.domain.model.dbflow.EBookModel
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.repository.dbflow.BookDataService
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class GetAllBookUseCase(useCaseExecution: UseCaseExecution, private val bookDataService: BookDataService) : UseCaseOutput<List<EBookModel>, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(): Observable<List<EBookModel>> {
        return Observable.create { e ->
            e.onNext(bookDataService.getAllDB())
            e.onComplete()
        }
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }
}