package basecode.com.domain.repository.network

import basecode.com.domain.model.network.response.CollectionRecomand
import basecode.com.domain.model.network.response.NewEBookResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.model.network.response.NewBookResponse
import io.reactivex.Observable
import io.reactivex.Single

interface AppRepository {
    fun getListNewItems(numberItem: Int, pageIndex: Int, pageSize: Int): Single<List<NewBookResponse>>
    fun getListNewEBookItems(numberItem: Int, pageIndex: Int, pageSize: Int): Single<List<NewEBookResponse>>
    fun getCollectionRecomand(): Single<List<CollectionRecomand>>
    fun getListNewNews(numberItem: Int, pageIndex: Int, pageSize: Int): Single<List<NewNewsResponse>>


    fun getListNewItemsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<NewBookResponse>>
    fun getListNewEBookItemsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<NewEBookResponse>>
    fun getCollectionRecomandObservable(): Observable<List<CollectionRecomand>>
    fun getListNewNewsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<NewNewsResponse>>
}