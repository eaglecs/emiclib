package basecode.com.data.remote

import basecode.com.domain.model.network.response.NewEBookResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    //4 GetListNewEBookItems
    @GET("/api/Item/GetListNewEBookItems")
    fun getListNewEBookItems(@Query("numberItem") numberItem: Int, @Query("pageIndex") pageIndex: Int, @Query("pageSize") pageSize: Int): Observable<NewEBookResponse>
}