package basecode.com.domain.repository.network

import basecode.com.domain.model.network.response.NewEBookResponse
import io.reactivex.Observable

interface AppRepository {
    fun getListNewEBookItems(numberItem: Int, pageIndex: Int, pageSize: Int): Observable<NewEBookResponse>
}