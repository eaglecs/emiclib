package basecode.com.domain.features

import basecode.com.domain.extention.number.valueOrZero
import basecode.com.domain.extention.valueOrEmpty
import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.domain.model.network.response.*
import basecode.com.domain.repository.network.AppRepository
import basecode.com.domain.usecase.base.UseCase
import basecode.com.domain.usecase.base.UseCaseExecution
import basecode.com.domain.usecase.base.UseCaseOutput
import io.reactivex.Observable

class GetInfoHomeUseCase(useCaseExecution: UseCaseExecution, private val appRepository: AppRepository) : UseCaseOutput<InfoHomeResponse, ErrorResponse>(useCaseExecution) {
    override fun buildUseCaseObservable(): Observable<InfoHomeResponse> {
        return Observable.create { e ->
            val infoHomeResponse = InfoHomeResponse()
            val newNewsResponse = appRepository.getListNewNews(5, 1, 5).blockingGet()
            val listNewBooks = appRepository.getListNewItems(10, 1, 10).blockingGet()
            val listNewEBooks = appRepository.getListNewEBookItems(10, 1, 10).blockingGet()
            val collectionRecomand = appRepository.getCollectionRecomand().blockingGet()
            newNewsResponse?.let {
                newNewsResponse.forEach { newNews ->
                    val newNewsModel = NewNewsModel(id = newNews.id.valueOrZero(), picture = newNews.picture.valueOrEmpty())
                    infoHomeResponse.lstNewNews.add(newNewsModel)
                }
            }
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
            collectionRecomand?.let {
                collectionRecomand.forEach { collection ->
                    val collectionRecommendModel = CollectionRecommendModel(id = collection.id.valueOrZero(), name = collection.name.valueOrEmpty(), coverPicture = collection.coverPicture.valueOrEmpty())
                    infoHomeResponse.lstCollectionRecommend.add(collectionRecommendModel)
                }
            }
            e.onNext(infoHomeResponse)
            e.onComplete()
        }
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse(msgError = throwable.message.valueOrEmpty())
    }

}