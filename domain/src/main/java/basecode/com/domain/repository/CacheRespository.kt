package basecode.com.domain.repository

import io.reactivex.Observable

interface CacheRespository {
    fun getUserToken(): Observable<String>
    fun saveInfoLogin(accessToken: String, loginType: String): Observable<Boolean>
}