package basecode.com.domain.features

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.network.response.*
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import io.reactivex.Observable

class GetInfoHomeUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCase<Boolean, InfoHomeResponse, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(isTDN: Boolean): Observable<InfoHomeResponse> {
        return Observable.create { e ->
            try {
                val infoHomeResponse = InfoHomeResponse()
                val listNewBooks = appRepository.getListNewItems(10, 1, 10).blockingGet()
                val listNewEBooks = appRepository.getListNewEBookItems(10, 1, 10).blockingGet()
                val collectionRecommend = appRepository.getBooksRecommend().blockingGet()
                listNewBooks?.let {
                    listNewBooks.forEach { newBook ->
                        val newBooksModel = NewBooksModel(id = newBook.id.valueOrZero(), title = newBook.title.valueOrEmpty(),
                            coverPicture = newBook.coverPicture.valueOrEmpty(), author = newBook.author.valueOrEmpty())
                        infoHomeResponse.lstNewBook.add(newBooksModel)
                    }
                }
                listNewEBooks?.let {
                    listNewEBooks.forEach { eBook ->
                        val newEBooksModel = NewEBookModel(id = eBook.id.valueOrZero(), title = eBook.title.valueOrEmpty(),
                            coverPicture = eBook.coverPicture.valueOrEmpty(), author = eBook.author.valueOrEmpty())
                        infoHomeResponse.lstNewEBook.add(newEBooksModel)
                    }
                }
                collectionRecommend?.let {
                    collectionRecommend.forEach { collection ->
                        val collectionRecommendModel = CollectionRecommendModel(id = collection.id.valueOrZero(), name = collection.title.valueOrEmpty(), coverPicture = collection.coverPicture.valueOrEmpty())
                        infoHomeResponse.lstCollectionRecommend.add(collectionRecommendModel)
                    }
                }
                e.onNext(infoHomeResponse)
                e.onComplete()
            } catch (ex: Exception){
                e.onError(ex)
                e.onComplete()
            }
        }
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse(msgError = throwable.message.valueOrEmpty())
    }

}