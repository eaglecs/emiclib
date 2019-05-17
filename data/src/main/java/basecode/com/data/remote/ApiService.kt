package basecode.com.data.remote

import basecode.com.domain.model.network.response.CollectionRecomand
import basecode.com.domain.model.network.response.NewEBookResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.model.network.response.NewBookResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    //4 GetListNewItems
    @GET("/api/Item/GetListNewItems")
    fun getListNewItems(@Query("numberItem") numberItem: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Single<List<NewBookResponse>>

    //4.1 GetListNewEBookItems
    @GET("/api/Item/GetListNewEBookItems")
    fun getListNewEBookItems(@Query("numberItem") numberItem: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Single<List<NewEBookResponse>>

    //13 GetCollectionRecomand
    @GET("/api/Cat/GetCollectionRecomand")
    fun getCollectionRecomand(): Single<List<CollectionRecomand>>

    //15 GetListNewNews
    @GET("/api/News/GetListNewNews")
    fun getListNewNews(@Query("numberItem") numberItem: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Single<List<NewNewsResponse>>


    //4 GetListNewItems
    @GET("/api/Item/GetListNewItems")
    fun getListNewItemsObservable(@Query("numberItem") numberItem: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Observable<List<NewBookResponse>>

    //4.1 GetListNewEBookItems
    @GET("/api/Item/GetListNewEBookItems")
    fun getListNewEBookItemsObservable(@Query("numberItem") numberItem: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Observable<List<NewEBookResponse>>

    //13 GetCollectionRecomand
    @GET("/api/Cat/GetCollectionRecomand")
    fun getCollectionRecomandObservable(): Observable<List<CollectionRecomand>>

    //15 GetListNewNews
    @GET("/api/News/GetListNewNews")
    fun getListNewNewsObservable(@Query("numberItem") numberItem: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Observable<List<NewNewsResponse>>
}