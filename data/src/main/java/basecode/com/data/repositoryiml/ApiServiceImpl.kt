package basecode.com.data.repositoryiml

import basecode.com.data.remote.ApiService
import basecode.com.domain.model.network.response.NewEBookResponse
import basecode.com.domain.repository.network.AppRepository
import io.reactivex.Observable

class ApiServiceImpl(private val apiServiceApp: ApiService) : AppRepository {
    override fun getListNewEBookItems(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<NewEBookResponse> {
        return apiServiceApp.getListNewEBookItems(numberItem, pageIndex, pageSize)
    }
}