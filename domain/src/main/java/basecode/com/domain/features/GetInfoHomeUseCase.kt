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
            val infoHomeResponse = InfoHomeResponse()
//            val newNewsBottomResponse = appRepository.getListNewNews(10, 1, 10).blockingGet()
            val listNewBooks = appRepository.getListNewItems(10, 1, 10).blockingGet()
            val listNewEBooks = appRepository.getListNewEBookItems(10, 1, 10).blockingGet()
            val collectionRecomand = appRepository.getCollectionRecomand().blockingGet()
//            var lstNewNews: List<NewNewsResponse>? = null
            try {
//                lstNewNews = if(isTDN){
//                    appRepository.getListNews(categoryId = 178 , pageIndex = 1, pageSize = 6).blockingGet()
//                } else {
//                    appRepository.getListNews(categoryId = 152, pageIndex = 1, pageSize = 10).blockingGet()
//                }
            } catch (ex: Exception){
//                lstNewNews?.let {
//                    lstNewNews.forEach { newNews ->
//                        val newNewsModel = NewNewsModel(id = newNews.id.valueOrZero(), picture = newNews.picture.valueOrEmpty(), title = newNews.title.valueOrEmpty(), content = newNews.details.valueOrEmpty(), summary = newNews.summary.valueOrEmpty())
//                        infoHomeResponse.lstNewNews.add(newNewsModel)
//                    }
//                }
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

//                newNewsBottomResponse?.let {
//                    newNewsBottomResponse.forEach { newNews ->
//                        val newNewsModel = NewNewsModel(id = newNews.id.valueOrZero(), picture = newNews.picture.valueOrEmpty(), title = newNews.title.valueOrEmpty(), content = newNews.details.valueOrEmpty(), summary = newNews.summary.valueOrEmpty())
//                        infoHomeResponse.lstNewNewsBottom.add(newNewsModel)
//                    }
//                }

                e.onNext(infoHomeResponse)
                e.onComplete()
            }



//            lstNewNews?.let {
//                lstNewNews.forEach { newNews ->
//                    val newNewsModel = NewNewsModel(id = newNews.id.valueOrZero(), picture = newNews.picture.valueOrEmpty(), title = newNews.title.valueOrEmpty(), content = newNews.details.valueOrEmpty(), summary = newNews.summary.valueOrEmpty())
//                    infoHomeResponse.lstNewNews.add(newNewsModel)
//                }
//            }
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

//            newNewsBottomResponse?.let {
//                newNewsBottomResponse.forEach { newNews ->
//                    val newNewsModel = NewNewsModel(id = newNews.id.valueOrZero(), picture = newNews.picture.valueOrEmpty().trim(), title = newNews.title.valueOrEmpty(), content = newNews.details.valueOrEmpty(), summary = newNews.summary.valueOrEmpty())
//                    infoHomeResponse.lstNewNewsBottom.add(newNewsModel)
//                }
//            }

            e.onNext(infoHomeResponse)
            e.onComplete()
        }
    }

    override fun createFailOutput(throwable: Throwable): ErrorResponse {
        return ErrorResponse(msgError = throwable.message.valueOrEmpty())
    }

}