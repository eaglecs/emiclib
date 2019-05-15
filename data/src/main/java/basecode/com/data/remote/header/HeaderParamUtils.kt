package basecode.com.data.remote.header

import android.content.Context
import android.util.Base64
import basecode.com.data.BuildConfig
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
            val randomKey = getRandomString()
            val timeStamp = getTimeStamp()
            val headers = mutableMapOf<String, String>()
            headers[HeaderConst.KEY_X_CONTENT_TYPE] = HeaderConst.VALUE_X_CONTENT_TYPE
            headers[HeaderConst.KEY_ACCEPT_LANGUAGE] = HeaderConst.VALUE_ACCEPT_LANGUAGE
            headers[HeaderConst.KEY_COOKIE] = HeaderConst.VALUE_COOKIE
            headers[HeaderConst.KEY_X_CONTENT_TYPE] = HeaderConst.VALUE_X_CONTENT_TYPE
            headers[HeaderConst.KEY_ACCEPT] = HeaderConst.VALUE_ACCEPT
            headers[HeaderConst.KEY_FOODY_CLIENT_VERSION] = BuildConfig.VERSION_NAME_REQUEST_HEADER
            headers[HeaderConst.KEY_FOODY_CLIENT_TYPE] = HeaderConst.VALUE_FOODY_CLIENT_TYPE
            headers[HeaderConst.KEY_FOODY_CLIENT_NAME] = HeaderConst.VALUE_FOODY_CLIENT_NAME
            headers[HeaderConst.KEY_FOODY_ACCESS_TIMESTAMP] = timeStamp
            headers[HeaderConst.KEY_FOODY_RANDOM_KEY] = randomKey
            headers[HeaderConst.KEY_FOODY_UDID] = UDIDUtil.getUDID(context)

//            val loginUserConfigModel = ConfigUtil.currentLoginUserConfigModel
//            if(loginUserConfigModel.isLogin) {
//                val userToken = loginUserConfigModel.token
//                if (userToken.isNotEmpty()) {
//                    headers[HeaderConst.KEY_FOODY_USER_TOKEN] = userToken
//                }
//            }
            try {
                headers[HeaderConst.KEY_FOODY_ACCESS_TOKEN] = getAccessToken(timeStamp, randomKey, method, url, requestBody)
            } catch (e: Exception) {
            }
            return headers
        }

        private fun getAccessToken(timeStamp: String, randomKey: String, method: String, url: String, requestBody: String?): String {
            val message = if ("GET" == method) {
                url + randomKey + timeStamp
            } else {
                url + requestBody + randomKey + timeStamp
            }

            val secretKey = when {
                BuildConfig.USE_DATA_DEMO -> HeaderConst.SECRET_KEY_DEMO
                BuildConfig.USE_DATA_LIVE -> HeaderConst.SECRET_KEY_LIVE
                else -> ""
            }
            return encode_HMAC_SHA256(secretKey, message)
        }

        private fun encode_HMAC_SHA256(secret: String, message: String): String {
            val sha256HMAC = Mac.getInstance("HmacSHA256")
            val secretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
            sha256HMAC.init(secretKey)
            val bytes = sha256HMAC.doFinal(message.toByteArray(charset("UTF-8")))
            return Base64.encodeToString(bytes, Base64.DEFAULT).trim { it <= ' ' }
        }

        private fun getTimeStamp(): String {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat(DateTimeFormat.YY_MM_DD_HHMMSS.value, Locale.US)
            return df.format(c)
        }

        private fun getRandomString(): String {
            val sizeOfRandomString = 10
            val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
            val random = Random()
            val sb = StringBuilder()
            for (i in 0 until sizeOfRandomString) {
                sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
            }
            return sb.toString()
        }
    }
}