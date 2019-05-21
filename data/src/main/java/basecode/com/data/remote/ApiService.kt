package basecode.com.data.remote

import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.domain.model.network.response.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    //Login
    @POST("/Api/Login")
    fun login(@Body loginRequest: LoginRequest): Observable<LoginResponse>
    //4 GetListNewItems
    @GET("/api/Item/GetListNewItems")
    fun getListNewItems(@Query("numberItem") numberItem: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Single<List<BookResponse>>

    //4.1 GetListNewEBookItems
    @GET("/api/Item/GetListNewEBookItems")
    fun getListNewEBookItems(@Query("numberItem") numberItem: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Single<List<NewEBookResponse>>

    //5 GetListBookRelated
    @GET("/api/Item/GetRelationItems")
    fun getListBookRelated(@Query("itemId") itemId: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Observable<List<BookResponse>>

    //13 GetCollectionRecomand
    @GET("/api/Cat/GetCollectionRecomand")
    fun getCollectionRecomand(): Single<List<CollectionRecomand>>

    //15 GetListNewNews
    @GET("/api/News/GetListNewNews")
    fun getListNewNews(@Query("numberItem") numberItem: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Single<List<NewNewsResponse>>


    //4 GetListNewItems
    @GET("/api/Item/GetListNewItems")
    fun getListNewItemsObservable(@Query("numberItem") numberItem: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Observable<List<BookResponse>>

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