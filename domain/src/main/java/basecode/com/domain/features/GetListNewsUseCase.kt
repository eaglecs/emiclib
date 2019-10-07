package basecode.com.domain.features

import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class GetListNewsUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<Int, List<NewNewsResponse>, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: Int): Observable<List<NewNewsResponse>> {
        return appRepository.getListNewsObservable(pageIndex = input, pageSize = 10, categoryId = 152)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }
}