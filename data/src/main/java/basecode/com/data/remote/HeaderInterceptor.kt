package basecode.com.data.remote

import android.content.Context
import basecode.com.data.remote.header.HeaderParamUtils
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException

class HeaderInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()

        val method = original.method()
        original.url()?.let {
            val listHeader: Map<String, String> = HeaderParamUtils.getListHeader(context, method, it.toString(), bodyToString(original.body()))
            listHeader.entries.forEach { entry ->
                requestBuilder.header(entry.key, entry.value)
            }
        }
        requestBuilder.method(method, original.body())
        requestBuilder.build()
        val request = requestBuilder.build()
        return chain.proceed(request)
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }

    }
}