package basecode.com.domain.repository

import io.reactivex.Observable

interface CacheRespository {
    fun getUserToken(): Observable<String>
}