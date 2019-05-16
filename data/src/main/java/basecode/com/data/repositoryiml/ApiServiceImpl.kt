package basecode.com.data.repositoryiml

import basecode.com.data.remote.ApiService
import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.domain.model.network.response.NewEBookResponse
import basecode.com.domain.repository.network.AppRepository
import io.reactivex.Observable

class ApiServiceImpl(private val apiServiceApp: ApiService) : AppRepository {
    override fun getListNewEBookItems(newEBookRequest: NewEBookRequest): Observable<NewEBookResponse> {
        return apiServiceApp.getListNewEBookItems(newEBookRequest)
    }
}