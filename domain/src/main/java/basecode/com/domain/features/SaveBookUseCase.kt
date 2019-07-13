package basecode.com.domain.features

import basecode.com.domain.model.dbflow.EBookModel
import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.repository.dbflow.BookDataService
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class SaveBookUseCase(useCaseExecution: UseCaseExecution, private val bookDataService: BookDataService) : UseCase<EBookModel, Boolean, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: EBookModel): Observable<Boolean> {
        return Observable.create { e ->
            bookDataService.saveDB(input)
            e.onNext(true)
            e.onComplete()
        }
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }
}