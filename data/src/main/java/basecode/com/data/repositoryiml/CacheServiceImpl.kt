package basecode.com.data.repositoryiml

import basecode.com.data.cache.pager.ConfigUtil
import basecode.com.domain.repository.CacheRespository
import io.reactivex.Observable

class CacheServiceImpl : CacheRespository {
    override fun getUserToken(): Observable<String> {
        return Observable.create { e ->
            val userToken = ConfigUtil.getUserToken()
            e.onNext(userToken)
            e.onComplete()
        }
    }
}