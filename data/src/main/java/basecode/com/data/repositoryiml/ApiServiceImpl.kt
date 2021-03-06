package basecode.com.data.repositoryiml

import basecode.com.data.remote.ApiService
import basecode.com.domain.model.network.response.*
import basecode.com.domain.repository.network.AppRepository
import io.reactivex.Observable
import io.reactivex.Single

class ApiServiceImpl(private val apiServiceApp: ApiService) : AppRepository {
    override fun getLoanHoldingRenew(coppynumber: String): Observable<Any> {
        return apiServiceApp.getLoanHoldingRenew(coppynumber)
    }

    override fun requestDocument(fullName: String, patronCode: String, email: String, phone: String, facebook: String, supplier: String, groupName: String, title: String, author: String, publisher: String, publishYear: String, information: String): Observable<Int> {
        return apiServiceApp.requestDocument(fullName, patronCode, email, phone, facebook, supplier, groupName, title, author, publisher, publishYear, information)
    }

    override fun login(username: String, password: String): Observable<LoginResponse> {
        return apiServiceApp.login(username = username, password = password)
    }

    override fun getListBookRelated(itemId: Long, pageIndex: Int, pageSize: Int, docType: Int): Observable<List<BookResponse>> {
        return apiServiceApp.getListBookRelated(itemId = itemId, pageIndex = pageIndex, pageSize = pageSize, docType = docType)
    }

    override fun getListNewItemsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<BookResponse>> {
        return apiServiceApp.getListNewItemsObservable(numberItem, pageIndex, pageSize)
    }

    override fun getListNewEBookItemsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<NewEBookResponse>> {
        return apiServiceApp.getListNewEBookItemsObservable(numberItem, pageIndex, pageSize)
    }

    override fun getCollectionRecomandObservable(): Observable<List<CollectionRecomand>> {
        return apiServiceApp.getCollectionRecomandObservable()
    }

    override fun getListNewNewsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<NewNewsResponse>> {
        return apiServiceApp.getListNewNewsObservable(pageIndex, pageSize)
    }

    override fun getListNewItems(numberItem: Int, pageIndex: Int, pageSize: Int): Single<List<BookResponse>> {
        return apiServiceApp.getListNewItems(numberItem, pageIndex, pageSize)
    }

    override fun getCollectionRecomand(): Single<List<CollectionRecomand>> {
        return apiServiceApp.getCollectionRecomand()
    }

    override fun getListNewNews(numberItem: Int, pageIndex: Int, pageSize: Int): Single<List<NewNewsResponse>> {
        return apiServiceApp.getListNewNews(numberItem, pageIndex, pageSize)
    }

    override fun getListNewEBookItems(numberItem: Int, pageIndex: Int, pageSize: Int): Single<List<NewEBookResponse>> {
        return apiServiceApp.getListNewEBookItems(numberItem, pageIndex, pageSize)
    }

    override fun findBook(
        docType: Int,
        pageIndex: Int,
        pageSize: Int,
        searchText: String,
        boothId: Long
    ): Observable<List<BookResponse>> {
        return if (searchText.isNotEmpty()) {
            if(docType == -1){
                apiServiceApp.findBook(
//                    docType = docType,
                    itemType = 3,
                    pageIndex = pageIndex,
                    pageSize = pageSize,
                    searchText = searchText,
                    boothId = boothId.toString()
                )
            } else {
                apiServiceApp.findBook(
//                    docType = docType,
                    pageIndex = pageIndex, pageSize = pageSize, searchText = searchText, boothId = boothId.toString())
            }
        } else {
            if(docType == -1){
                apiServiceApp.findBook(
//                    docType = docType,
                    itemType = 3,
                    pageIndex = pageIndex,
                    pageSize = pageSize,
                    boothId = boothId.toString()
                )
            } else {
                apiServiceApp.findBook(
//                    docType = docType,
                    pageIndex = pageIndex,
                    pageSize = pageSize,
                    boothId = boothId.toString()
                )
            }
        }
    }

    override fun findBookAdvance(
        docType: Int,
        pageIndex: Int,
        pageSize: Int,
        searchText: String,
        title: String,
        author: String,
        language: String,
        boothId: String
    ): Observable<List<BookResponse>> {
        return apiServiceApp.findBookAdvance(
//            docType = docType,
            pageIndex = pageIndex, pageSize = pageSize,
            searchText = searchText, title = title, author = author,
//            language = language,
            boothId = boothId)
    }

    override fun getInfoBook(itemId: Long): Observable<InfoBookResponse> {
        return apiServiceApp.getInfoBook(itemId)
    }

    override fun getInfoUser(): Single<InfoUserResponse> {
        return apiServiceApp.getInfoUser()
    }

    override fun reservationBook(bookId: Long): Observable<Int> {
        return apiServiceApp.reservationBook(bookId)
    }

    override fun reserverBook(bookId: Long): Observable<Int> {
        return apiServiceApp.reserverBook(bookId)
    }

    override fun getItemInCollectionRecomand(collectionId: Int, pageIndex: Int, pageSize: Int): Observable<List<NewNewsResponse>> {
        return apiServiceApp.getItemInCollectionRecomand(collectionId, pageIndex, pageSize)
    }

    override fun getLoanHoldingCurrent(): Observable<List<BookBorrowResponse>> {
        return apiServiceApp.getLoanHoldingCurrent()
    }

    override fun getLoanHoldingHistory(): Observable<List<BookBorrowResponse>> {
        return apiServiceApp.getLoanHoldingHistory()
    }

    override fun getListMessage(): Observable<List<MessageResponse>> {
        return apiServiceApp.getListMessage()
    }

    override fun readMessage(id: Long): Observable<Int> {
        return apiServiceApp.readMessage(id)
    }

    override fun changePass(newPassword: String, oldPassword: String): Observable<Int> {
        return apiServiceApp.changePass(newPassword, oldPassword)
    }

    override fun feedback(content: String, email: String, mobilePhone: String, title: String, userFullName: String): Observable<Int> {
        return apiServiceApp.feedback(content, email, mobilePhone, title, userFullName)
    }

    override fun getListFQA(): Observable<List<QAResponse>> {
        return apiServiceApp.getListFQA()
    }

    override fun getListReserveQueue(): Observable<List<ReserveResponse>> {
        return apiServiceApp.getListReserveQueue()
    }

    override fun getListReserveRequest(): Observable<List<ReserveResponse>> {
        return apiServiceApp.getListReserveRequest()
    }

    override fun getLoanHoldingRenew(): Observable<List<NewNewsResponse>> {
        return apiServiceApp.getLoanHoldingRenew()
    }

    override fun loanRenew(coppynumber: String): Observable<Int> {
        return apiServiceApp.loanRenew(coppynumber)
    }

    override fun getListNews(categoryId: Int, pageIndex: Int, pageSize: Int): Single<List<NewNewsResponse>> {
        return apiServiceApp.getListNews(categoryId, pageIndex, pageSize)
    }

    override fun getListNewsObservable(categoryId: Int, pageIndex: Int, pageSize: Int): Observable<List<NewNewsResponse>> {
        return apiServiceApp.getListNewsObservable(categoryId, pageIndex, pageSize)
    }

    override fun getBooksRecommend(): Single<List<BookRecommendResponse>> {
        return apiServiceApp.getBooksRecommend()
    }

    override fun getBooksRecommendObservable(pageIndex: Int, pageSize: Int): Observable<List<BookRecommendResponse>> {
        return apiServiceApp.getBooksRecommendObservable(pageIndex, pageSize)
    }

    override fun getListBooth(): Observable<BoothsResponse> {
        return apiServiceApp.getListBooth()
    }

    override fun borrowBook(coppynumber: String): Observable<BorrowReturnBookResponse> {
        return apiServiceApp.borrowBook(coppynumber)
    }

    override fun getCheckOutCurrentLoan(transactionIds: String): Observable<BooksDetailResponse> {
        return apiServiceApp.getCheckOutCurrentLoan(transactionIds)
    }

    override fun returnBook(coppynumber: String): Observable<BorrowReturnBookResponse> {
        return apiServiceApp.returnBook(coppynumber)
    }

    override fun getCheckInCurrentLoanInfor(transactionIds: String): Observable<BooksDetailResponse> {
        return apiServiceApp.getCheckInCurrentLoanInfor(transactionIds)
    }

    override fun getPatronOnLoanCopies(): Observable<GetPatronOnLoanCopiesResponse> {
        return apiServiceApp.getPatronOnLoanCopies()
    }
}