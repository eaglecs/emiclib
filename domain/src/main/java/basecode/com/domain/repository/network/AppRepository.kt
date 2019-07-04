package basecode.com.domain.repository.network

import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.domain.model.network.response.*
import io.reactivex.Observable
import io.reactivex.Single

interface AppRepository {
    fun getListNewItems(numberItem: Int, pageIndex: Int, pageSize: Int): Single<List<BookResponse>>
    fun getListNewEBookItems(numberItem: Int, pageIndex: Int, pageSize: Int): Single<List<NewEBookResponse>>
    fun getCollectionRecomand(): Single<List<CollectionRecomand>>
    fun getListNewNews(numberItem: Int, pageIndex: Int, pageSize: Int): Single<List<NewNewsResponse>>


    fun getListNewItemsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<BookResponse>>
    fun getListNewEBookItemsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<NewEBookResponse>>
    fun getCollectionRecomandObservable(): Observable<List<CollectionRecomand>>
    fun getListNewNewsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<NewNewsResponse>>

    fun getListBookRelated(itemId: Int, pageIndex: Int, pageSize: Int): Observable<List<BookResponse>>
    fun login(username: String, password: String): Observable<LoginResponse>
    fun findBook(docType: Int, pageIndex: Int, pageSize: Int, searchText: String): Observable<List<BookResponse>>
    fun findBookAdvance(docType: Int, pageIndex: Int, pageSize: Int, searchText: String, title: Int, author: Int, language: Int): Observable<List<BookResponse>>
    fun getInfoBook(itemId: Int): Observable<InfoBookResponse>
    fun getInfoUser(): Observable<InfoUserResponse>
}