package basecode.com.data.remote

import basecode.com.domain.model.network.response.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {
    //Login
    @FormUrlEncoded
    @POST("/Api/Login")
    fun login(@Field("username") username: String, @Field("password") password: String, @Field("grant_type") grantType: String = "password"): Observable<LoginResponse>

    //4 GetListNewItems
    @GET("/api/Item/GetListNewItems")
    fun getListNewItems(@Query("numberItem") numberItem: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Single<List<BookResponse>>

    //4.1 GetListNewEBookItems
    @GET("/api/Item/GetListNewEBookItems")
    fun getListNewEBookItems(@Query("numberItem") numberItem: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Single<List<NewEBookResponse>>

    //5 GetListBookRelated
    @GET("/api/Item/GetRelationItems")
    fun getListBookRelated(@Query("itemId") itemId: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Observable<List<BookResponse>>

    //7 Find Book
    @GET("/api/Item/Search")
    fun findBook(@Query("docType") docType: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int, @Query("searchText") searchText: String? = null): Observable<List<BookResponse>>

    //8 Find Book Advance
    @GET("/api/Item/SearchAdvance")
    fun findBookAdvance(@Query("docType") docType: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int, @Query("searchText") searchText: String,
                        @Query("title") title: String, @Query("author") author: String, @Query("language") language: String): Observable<List<BookResponse>>

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

    //6 Get Information book
    @GET("/api/Item/GetItemContents")
    fun getInfoBook(@Query("itemId") itemId: Int): Observable<InfoBookResponse>

    //2 Get Information user
    @GET("/api/user/GetUserInfor")
    fun getInfoUser(): Observable<InfoUserResponse>

    //Reservation Book
    @POST("/api/Reservation/Reservation")
    fun reservationBook(@Query("itemId") bookId: Int): Observable<Int>

    //Load List book GetItemInCollectionRecomand
    @GET("/Api/Item/GetItemInCollectionRecomand")
    fun getItemInCollectionRecomand(@Query("collectionId") collectionId: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Observable<List<NewNewsResponse>>

    //Sach dang muon
    @GET("/api/cir/GetLoanHoldingCurrent")
    fun getLoanHoldingCurrent(): Observable<List<NewNewsResponse>>

    //Sach da muon
    @GET("/api/cir/GetLoanHoldingHistory")
    fun getLoanHoldingHistory(): Observable<List<NewNewsResponse>>

    // Get list notify
    @GET("/api/User/GetMessages")
    fun getListMessage(): Observable<List<MessageResponse>>

    // Read Message
    @FormUrlEncoded
    @POST("/api/User/UpdateMessagesIsRead")
    fun readMessage(@Field("Id") id: Long): Observable<Any>

    // Change Pass
    @FormUrlEncoded
    @POST("Api/User/UpdatePassword")
    fun changePass(@Field("NewPassword") newPassword: String, @Field("OldPassword") oldPassword: String): Observable<Int>

    // Feedback
    @FormUrlEncoded
    @POST("/api/Contact/Insert")
    fun feedback(@Field("Content") content: String, @Field("Email") email: String,
                 @Field("MobilePhone") mobilePhone: String, @Field("Title") title: String,
                 @Field("UserFullName") userFullName: String): Observable<Int>

    // GetListFQA
    @GET("api/FQA/GetListFQA")
    fun getListFQA(): Observable<List<QAResponse>>

    // Dat muon
    @GET("api/Cir/GetListReserveQueue")
    fun getListReserveQueue(): Observable<List<ReserveResponse>>

    // Dat cho
    @GET("api/Cir/GetListReserveRequest")
    fun getListReserveRequest(): Observable<List<ReserveResponse>>






}