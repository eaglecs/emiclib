package basecode.com.domain.features

import basecode.com.domain.model.network.response.ErrorResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class GetItemInCollectionRecomandUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<GetItemInCollectionRecomandUseCase.Input, List<NewNewsResponse>, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(input: Input): Observable<List<NewNewsResponse>> {
        return appRepository.getItemInCollectionRecomand(collectionId = input.collectionId, pageSize = input.pageSize, pageIndex = input.pageIndex)
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse("")
    }

    class Input(val collectionId: Int,
                val pageIndex: Int,
                val pageSize: Int)
}