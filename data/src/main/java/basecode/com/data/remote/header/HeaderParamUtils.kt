package basecode.com.data.remote.header

import android.content.Context
import android.util.Base64
import basecode.com.data.BuildConfig
import basecode.com.data.cache.pager.ConfigUtil
import basecode.com.data.remote.UDIDUtil
import basecode.com.domain.util.DateTimeFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

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