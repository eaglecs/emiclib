package basecode.com.domain.repository.network

import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.domain.model.network.response.NewEBookResponse
import io.reactivex.Observable

interface AppRepository {
    fun getListNewEBookItems(newEBookRequest: NewEBookRequest): Observable<NewEBookResponse>
}