package basecode.com.data.remote

import basecode.com.domain.model.network.response.*
import basecode.com.domain.util.ConstApp
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
    @GET("/api/Item/GetRelationItemsBooth")
    fun getListBookRelated(
        @Query("itemId") itemId: Long,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int,
        @Query("docType") docType: Int
    ): Observable<List<BookResponse>>

    //7 Find Book
    @GET("/api/Item/SearchBooth")
    fun findBook(
//        @Query("docType") docType: Int? = null,
        @Query("itemType") itemType: Int? = null,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int,
        @Query("searchText") searchText: String? = null,
        @Query("booth") boothId: String
    ): Observable<List<BookResponse>>

    //8 Find Book Advance
    @GET("/api/Item/SearchAdvanceBooth")
    fun findBookAdvance(
//        @Query("docType") docType: Int,
        @Query("pageIndex") pageIndex: Int,
        @Query("pageSize") pageSize: Int,
        @Query("keyWord") searchText: String,
        @Query("title") title: String,
        @Query("author") author: String,
//        @Query("language") language: String,
        @Query("booth") boothId: String
    ): Observable<List<BookResponse>>

    //13 GetCollectionRecomand
    @GET("/api/Cat/GetCollectionRecomand")
    fun getCollectionRecomand(): Single<List<CollectionRecomand>>

    //13 GetCollectionRecomand
    @GET("/api/Item/GetItemReccomand")
    fun getBooksRecommend(@Query("pageIndex") pageIndex: Int = 1, @Query("pageSize") pageSize: Int = 20): Single<List<BookRecommendResponse>>

    @GET("/api/Item/GetItemReccomand")
    fun getBooksRecommendObservable(@Query("pageIndex") pageIndex: Int = 1, @Query("pageSize") pageSize: Int = 20): Observable<List<BookRecommendResponse>>

    //15 GetListNewNews
    @GET("/api/News/GetAllNews")
    fun getListNewNews(@Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Single<List<NewNewsResponse>>

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
    @GET("/api/News/GetAllNews")
    fun getListNewNewsObservable(@Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Observable<List<NewNewsResponse>>

    //6 Get Information book
    @GET("/api/Item/GetItemContents")
    fun getInfoBook(@Query("itemId") itemId: Long): Observable<InfoBookResponse>

    //2 Get Information user
    @GET("/api/user/GetUserInfor")
    fun getInfoUser(): Single<InfoUserResponse>

    //Reservation Book
    @POST("/api/Reservation/Reservation")
    fun reservationBook(@Query("itemId") bookId: Long): Observable<Int>

    //Reservation Book
    @POST("/api/Cir/Reserver")
    fun reserverBook(@Query("itemid") bookId: Long): Observable<Int>

    //Load List book GetItemInCollectionRecomand
    @GET("/Api/Item/GetItemInCollectionRecomand")
    fun getItemInCollectionRecomand(@Query("collectionId") collectionId: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Observable<List<NewNewsResponse>>

    //Sach dang muon
    @GET("/api/cir/GetLoanHoldingCurrent")
    fun getLoanHoldingCurrent(): Observable<List<BookBorrowResponse>>

    //Sach da muon
    @GET("/api/cir/GetLoanHoldingHistory")
    fun getLoanHoldingHistory(): Observable<List<BookBorrowResponse>>

    // Get list notify
    @GET("/api/User/GetMessages")
    fun getListMessage(): Observable<List<MessageResponse>>

    // Read Message
    @POST("/api/User/UpdateMessagesIsRead")
    fun readMessage(@Query("id") id: Long): Observable<Int>

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

    //Get danh sach gia han
    @GET("/api/cir/GetLoanHoldingRenew")
    fun getLoanHoldingRenew(): Observable<List<NewNewsResponse>>

    // Gia han

    @POST("/api/cir/LoanRenew")
    fun loanRenew(@Query("coppynumber") coppynumber: String): Observable<Int>

//    @GET("/api/Cir/LoanRenew")
//    fun loanRenew(@Query("coppynumber") coppynumber: String): Observable<Int>


    @FormUrlEncoded
    @POST("api/Request/Cataloger")
    fun requestDocument(@Field("FullName") fullName: String,
              @Field("PatronCode") patronCode: String,
              @Field("Email") email: String,
              @Field("Phone") phone: String,
              @Field("Facebook") facebook: String,
              @Field("Supplier") supplier: String,
              @Field("GroupName") groupName: String,
              @Field("Title") title: String,
              @Field("Author") author: String,
              @Field("Publisher") publisher: String,
              @Field("PublishYear") publishYear: String,
              @Field("Information") information: String): Observable<Int>

    //4.1 GetListNewEBookItems
    @GET("/api/News/GetNewsByCategory")
    fun getListNews(@Query("categoryId") categoryId: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Single<List<NewNewsResponse>>
    //4.1 GetListNewEBookItems
    @GET("/api/News/GetNewsByCategory")
    fun getListNewsObservable(@Query("categoryId") categoryId: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Observable<List<NewNewsResponse>>

    //Load List book GetItemInCollectionRecomand
    @GET("api/cir/GetLoanHoldingRenew")
    fun getLoanHoldingRenew(@Query("coppynumber") coppynumber: String): Observable<Any>

    @GET("api/Booth/GetListBooth")
    fun getListBooth(): Observable<BoothsResponse>

    @GET("api/Cir/CheckOutBook")
    fun borrowBook(@Query("coppynumber") coppynumber: String, @Query("lat") lat: Double = ConstApp.lat,
        @Query("lng") lng: Double = ConstApp.lng): Observable<BorrowReturnBookResponse>

    @GET("api/Cir/GetCheckOutCurrentLoanInfor")
    fun getCheckOutCurrentLoan(@Query("transactionIds") transactionIds: String): Observable<BooksDetailResponse>

    @GET("api/Cir/CheckInBook")
    fun returnBook(@Query("coppynumber") coppynumber: String, @Query("lat") lat: Double = ConstApp.lat,
                   @Query("lng") lng: Double = ConstApp.lng): Observable<BorrowReturnBookResponse>

    @GET("api/Cir/GetCheckInCurrentLoanInfor")
    fun getCheckInCurrentLoanInfor(@Query("transactionIds") transactionIds: String): Observable<BooksDetailResponse>

    @GET("api/Cir/GetPatronOnLoanCopies")
    fun getPatronOnLoanCopies(): Observable<GetPatronOnLoanCopiesResponse>



}