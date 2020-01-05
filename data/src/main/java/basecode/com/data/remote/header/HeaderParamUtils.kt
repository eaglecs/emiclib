package basecode.com.data.remote.header

import android.content.Context
import basecode.com.data.cache.pager.ConfigUtil

class HeaderParamUtils {
    companion object {
        @JvmStatic
        fun getListHeader(context: Context, method: String, url: String, requestBody: String?): Map<String, String> {
            val headers = mutableMapOf<String, String>()
            val userToken = ConfigUtil.getUserToken()
            val loginType = ConfigUtil.getLoginType()
            if (userToken.isNotEmpty()) {
                headers[HeaderConst.KEY_AUTHORIZATION] = "$loginType $userToken"
            }
            return headers
        }
    }
}