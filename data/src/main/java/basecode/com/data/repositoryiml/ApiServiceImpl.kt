package basecode.com.data.repositoryiml

import basecode.com.data.remote.ApiService
import basecode.com.domain.model.network.response.CollectionRecomand
import basecode.com.domain.model.network.response.NewEBookResponse
import basecode.com.domain.model.network.response.NewNewsResponse
import basecode.com.domain.model.network.response.NewBookResponse
import basecode.com.domain.repository.network.AppRepository
import io.reactivex.Observable
import io.reactivex.Single

class ApiServiceImpl(private val apiServiceApp: ApiService) : AppRepository {
    override fun getListNewItemsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<NewBookResponse>> {
        return apiServiceApp.getListNewItemsObservable(numberItem, pageIndex, pageSize)
    }

    override fun getListNewEBookItemsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<NewEBookResponse>> {
        return apiServiceApp.getListNewEBookItemsObservable(numberItem, pageIndex, pageSize)
    }

    override fun getCollectionRecomandObservable(): Observable<List<CollectionRecomand>> {
        return apiServiceApp.getCollectionRecomandObservable()
    }

    override fun getListNewNewsObservable(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<List<NewNewsResponse>> {
        return apiServiceApp.getListNewNewsObservable(numberItem, pageIndex, pageSize)
    }

    override fun getListNewItems(numberItem: Int, pageIndex: Int, pageSize: Int): Single<List<NewBookResponse>> {
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
}