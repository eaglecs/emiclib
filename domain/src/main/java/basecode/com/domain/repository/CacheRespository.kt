package basecode.com.domain.repository

import basecode.com.domain.model.network.response.UserModel
import io.reactivex.Observable

interface CacheRespository {
    fun getUserToken(): Observable<String>
    fun getUserModel(): UserModel?
    fun saveInfoLogin(accessToken: String, loginType: String): Observable<Boolean>
    fun saveUserModel(userModel: UserModel)
}