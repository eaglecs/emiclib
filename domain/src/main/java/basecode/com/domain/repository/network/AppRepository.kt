package basecode.com.domain.repository.network

import basecode.com.domain.model.network.response.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AppRepository {
    fun getListNewItems(numberItem: Int, pageIndex: Int, pageSize: Int): Single<List<BookResponse>>
    fun getListNewEBookItems(numberItem: Int, pageIndex: Int, pageSize: Int): Single<List<NewEBookResponse>>
    fun getCollectionRecomand(): Single<List<CollectionRecomand>>
    fun getListNewNews(numberItem: Int, pageIndex: Int, pageSize: Int): Single<List<NewNewsResponse>>


    fun getListNewItemsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<BookResponse>>
    fun getListNewEBookItemsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<NewEBookResponse>>
    fun getCollectionRecomandObservable(): Observable<List<CollectionRecomand>>
    fun getListNewNewsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<NewNewsResponse>>

    fun getListBookRelated(itemId: Long, pageIndex: Int, pageSize: Int, docType: Int): Observable<List<BookResponse>>
    fun login(username: String, password: String): Observable<LoginResponse>
    fun findBook(docType: Int, pageIndex: Int, pageSize: Int, searchText: String, boothId: Long): Observable<List<BookResponse>>
    fun findBookAdvance(
        docType: Int,
        pageIndex: Int,
        pageSize: Int,
        searchText: String,
        title: String,
        author: String,
        language: String,
        boothId: String
    ): Observable<List<BookResponse>>
    fun getInfoBook(itemId: Long): Observable<InfoBookResponse>
    fun getInfoUser(): Single<InfoUserResponse>
    fun reservationBook(bookId: Long): Observable<Int>
    fun reserverBook(bookId: Long): Observable<Int>
    fun getItemInCollectionRecomand(collectionId: Int, pageIndex: Int, pageSize: Int): Observable<List<NewNewsResponse>>
    fun getLoanHoldingCurrent(): Observable<List<BookBorrowResponse>>
    fun getLoanHoldingHistory(): Observable<List<BookBorrowResponse>>
    fun getListMessage(): Observable<List<MessageResponse>>
    fun readMessage(id: Long): Observable<Int>
    fun changePass(newPassword: String, oldPassword: String): Observable<Int>
    fun feedback(content: String, email: String,
                 mobilePhone: String, title: String,
                 userFullName: String): Observable<Int>

    fun getListFQA(): Observable<List<QAResponse>>

    fun getListReserveQueue(): Observable<List<ReserveResponse>>

    fun getListReserveRequest(): Observable<List<ReserveResponse>>
    fun getLoanHoldingRenew(): Observable<List<NewNewsResponse>>
    fun loanRenew(coppynumber: String): Observable<Int>
    fun requestDocument(fullName: String,
                        patronCode: String,
                        email: String,
                        phone: String,
                        facebook: String,
                        supplier: String,
                        groupName: String,
                        title: String,
                        author: String,
                        publisher: String,
                        publishYear: String,
                        information: String): Observable<Int>
    fun getListNews(categoryId: Int, pageIndex: Int, pageSize: Int): Single<List<NewNewsResponse>>
    fun getListNewsObservable(categoryId: Int, pageIndex: Int, pageSize: Int): Observable<List<NewNewsResponse>>
    fun getLoanHoldingRenew(coppynumber: String): Observable<Any>

    fun getBooksRecommend(): Single<List<BookRecommendResponse>>
    fun getBooksRecommendObservable(pageIndex: Int, pageSize: Int): Observable<List<BookRecommendResponse>>
    fun getListBooth(): Observable<BoothsResponse>
    fun borrowBook(coppynumber: String): Observable<BorrowReturnBookResponse>
    fun getCheckOutCurrentLoan(transactionIds: String): Observable<BooksDetailResponse>
    fun returnBook(coppynumber: String): Observable<BorrowReturnBookResponse>
    fun getCheckInCurrentLoanInfor(transactionIds: String): Observable<BooksDetailResponse>
    fun getPatronOnLoanCopies(): Observable<GetPatronOnLoanCopiesResponse>
}