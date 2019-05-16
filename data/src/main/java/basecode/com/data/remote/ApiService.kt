package basecode.com.data.remote

import basecode.com.domain.model.network.request.NewEBookRequest
import basecode.com.domain.model.network.response.NewEBookResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    //4 GetListNewEBookItems
    @POST("/api/Item/GetListNewEBookItems")
    fun getListNewEBookItems(@Body newEBookRequest: NewEBookRequest): Observable<NewEBookResponse>
}