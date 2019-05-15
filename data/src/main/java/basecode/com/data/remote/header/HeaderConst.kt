package basecode.com.data.remote.header

class HeaderConst {
    companion object {
        const val SECRET_KEY_LIVE = "2ueirh439djwus832jdnsi28s"
        const val SECRET_KEY_DEMO = "123456"

        //key
        const val KEY_FOODY_RANDOM_KEY = "X-Foody-Random-Key"
        const val KEY_FOODY_ACCESS_TIMESTAMP = "X-Foody-Access-Timestamp"
        const val KEY_FOODY_ACCESS_TOKEN = "X-Foody-Access-Token"
        const val KEY_FOODY_CLIENT_VERSION = "X-Foody-Client-Version"
        const val KEY_FOODY_CLIENT_TYPE = "X-Foody-Client-Type"
        const val KEY_FOODY_UDID = "X-Foody-UDID"
        const val KEY_FOODY_CLIENT_NAME = "X-Foody-Client-Name"
        const val KEY_FOODY_USER_TOKEN = "X-Foody-User-Token"
        const val KEY_ACCEPT = "Accept"
        const val KEY_X_CONTENT_TYPE = "Content-Type"
        const val KEY_ACCEPT_LANGUAGE = "Accept-Language"
        const val KEY_COOKIE = "Cookie"


        // value
        const val VALUE_FOODY_CLIENT_TYPE = "Android"
        const val VALUE_FOODY_CLIENT_NAME = "FoodyPOSApp_Android"
        const val VALUE_ACCEPT = "application/json"
        const val VALUE_X_CONTENT_TYPE = "application/json"

        const val VALUE_ACCEPT_LANGUAGE = "en-us"
        const val VALUE_COOKIE = "flg=vn"
    }
}