package basecode.com.data.repositoryiml

import basecode.com.data.cache.pager.ConfigUtil
import basecode.com.domain.model.network.request.LoginRequest
import basecode.com.domain.model.network.response.UserModel
import basecode.com.domain.repository.CacheRespository
import io.reactivex.Observable

class CacheServiceImpl : CacheRespository {
    override fun saveLoginRequest(loginRequest: LoginRequest) {
        ConfigUtil.saveLoginRequest(loginRequest)
    }

    override fun getLoginRequest(): LoginRequest? {
        return ConfigUtil.getLoginRequest()
    }

    override fun getUserModel(): UserModel? {
        return ConfigUtil.getUserModel()
    }

    override fun saveUserModel(userModel: UserModel) {
        ConfigUtil.saveUserModel(userModel)
    }

    override fun saveInfoLogin(accessToken: String, loginType: String): Observable<Boolean> {
        return Observable.create { e ->
            if (accessToken.isEmpty() && loginType.isEmpty()) {
                ConfigUtil.saveUserModel(null)
            }
            ConfigUtil.saveUserToken(accessToken)
            ConfigUtil.saveLoginType(loginType)
            e.onNext(true)
            e.onComplete()
        }
    }


    override fun getUserToken(): Observable<String> {
        return Observable.create { e ->
            val userToken = ConfigUtil.getUserToken()
            e.onNext(userToken)
            e.onComplete()
        }
    }
}